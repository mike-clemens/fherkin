package fherkin.io.impl;

import fherkin.model.GherkinEntry;
import fherkin.model.entry.Comment;
import fherkin.model.location.Location;
import fherkin.model.location.SpreadsheetLocation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Base class for a gherkin parser that reads from a spreadsheet.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public abstract class AbstractSpreadsheetGherkinParser extends AbstractGherkinParser {

	protected String filename;
	protected InputStream inputStream;
	
	public AbstractSpreadsheetGherkinParser(File file) throws IOException {
		filename = file.getName();
		inputStream = new FileInputStream(file);
	}
	
	public AbstractSpreadsheetGherkinParser(String filename, InputStream inputStream) throws IOException {
		this.filename = filename;
		this.inputStream = inputStream;
	}
	
	protected AbstractSpreadsheetGherkinParser() {
	}
	
	@Override
	public List<GherkinEntry> parse() throws IOException {
		Workbook workbook = parseWorkbook();
		
		int sheetCount = workbook.getNumberOfSheets();
		for(int i = 0; i < sheetCount; i++)
			processSheet(workbook.getSheetAt(i));
		
		return entries;
	}
	
	protected abstract Workbook parseWorkbook() throws IOException;
	
	protected void processSheet(Sheet sheet) {
		Iterator<Row> iterator = sheet.rowIterator();
		Row row;
		Pair<String, SortedMap<Location, String>> result;
		String prior = null;
		List<SortedMap<Location, String>> dataTable = null;
		while(iterator.hasNext()) {
			row = iterator.next();
			result = tokenize(row);
			
			if(result.getFirst() == null || isKeyword(result.getFirst())) {
				if(dataTable != null) {
					processDataTable(dataTable);
					dataTable = null;
				}
				
				processLine(result.getSecond());
				prior = result.getFirst();
			}
			else
			if(prior == null)
				processLine(result.getSecond());
			else
			if(isDataTableKeyword(prior)) {
				if(dataTable == null)
					dataTable = new ArrayList<SortedMap<Location, String>>();
				dataTable.add(result.getSecond());
			}
			else
				processLine(result.getSecond());
		}
		
		if(dataTable != null)
			processDataTable(dataTable);
	}
	
	protected boolean isKeyword(String value) {
		if(value == null)
			return false;
		
		if(value.startsWith("@"))
			return true;
		
		if(value.endsWith(":")) {
			value = value.substring(0, value.length() - 1);
			return keywords.getFeature().contains(value)
				|| keywords.getBackground().contains(value)
				|| keywords.getScenario().contains(value)
				|| keywords.getScenarioOutline().contains(value)
				|| keywords.getRule().contains(value)
				|| keywords.getExamples().contains(value);
		}
		
		return keywords.getGiven().contains(value)
			|| keywords.getWhen().contains(value)
			|| keywords.getThen().contains(value)
			|| keywords.getAnd().contains(value)
			|| keywords.getBut().contains(value);
	}
	
	protected boolean isDataTableKeyword(String value) {
		if(value == null)
			return false;
		
		if(value.endsWith(":")) {
			value = value.substring(0, value.length() - 1);
			return keywords.getExamples().contains(value);
		}
		
		return keywords.getGiven().contains(value)
			|| keywords.getWhen().contains(value)
			|| keywords.getThen().contains(value)
			|| keywords.getAnd().contains(value)
			|| keywords.getBut().contains(value);
	}

	protected Pair<String, SortedMap<Location, String>> tokenize(Row row) {
		SpreadsheetLocation location = new SpreadsheetLocation();
		location.setFilename(filename);
		location.setSheet(row.getSheet().getSheetName());
		location.setRow(row.getRowNum());
		
		SortedMap<Location, String> map = new TreeMap<Location, String>();
		Iterator<Cell> iterator = row.cellIterator();
		Cell cell;
		String value;
		int index;
		boolean comment = false;
		String firstNonBlankValue = null;
		while(iterator.hasNext()) {
			cell = iterator.next();
			value = getCellValue(cell);
			
			location = location.clone();
			location.setColumn(cell.getColumnIndex());
			location.setPosition(0);
			
			if(comment)
				map.put(location, value);
			else {
				index = value.indexOf('#');
				if(index > -1) {
					if(index > 0) {
						map.put(location, value.substring(0, index));
						if(value.substring(0, index).trim().length() > 0 && firstNonBlankValue == null)
							firstNonBlankValue = value.substring(0, index).trim();
					}
					
					location = location.clone();
					location.setPosition(index);
					map.put(location, "#");
					
					if(value.length() > index + 1) {
						location = location.clone();
						location.setPosition(index + 1);
						map.put(location, value.substring(index + 1));
					}
					
					comment = true;
				}
				else {
					map.put(location, value);
					if(value.trim().length() > 0 && firstNonBlankValue == null)
						firstNonBlankValue = value.trim();
				}
			}
			
			if(iterator.hasNext()) {
				location = location.clone();
				location.setPositionAfter();
				map.put(location, "\t");
			}
		}

		return new Pair<String, SortedMap<Location, String>>(firstNonBlankValue, map);
	}
	
	protected String getCellValue(Cell cell) {
		switch(cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case BOOLEAN:
				return Boolean.toString(cell.getBooleanCellValue());
			case NUMERIC:
				String value = Double.toString(cell.getNumericCellValue());
				return value.endsWith(".0") ? value.substring(0, value.length() - 2) : value;
			default:
				return "";
		}
	}
	
	protected void processDataTable(List<SortedMap<Location, String>> dataTable) {
		// determine which columns of the data table have data
		Set<Integer> columns = new TreeSet<Integer>();
		int lastColumn = -1;
		SpreadsheetLocation location;
		for(SortedMap<Location, String> tokens : dataTable)
			for(Map.Entry<Location, String> entry : tokens.entrySet())
				if(entry.getValue().trim().length() > 0) {
					location = (SpreadsheetLocation) entry.getKey();
					columns.add(location.getColumn());
					
					if(location.getColumn() > lastColumn)
						lastColumn = location.getColumn();
				}
		
		// insert pipes around each pertinent column in the data table
		for(SortedMap<Location, String> tokens : dataTable) {
			location = (SpreadsheetLocation) tokens.keySet().iterator().next();
			for(Integer column : columns) {
				location = location.clone();
				location.setColumn(column);
				location.setPositionBefore();
				tokens.put(location, "|");
			}
			
			location = location.clone();
			location.setColumn(lastColumn + 1);
			location.setPositionBefore();
			tokens.put(location, "|");
		}
		
		// process the data table tokens
		for(SortedMap<Location, String> tokens : dataTable)
			processLine(tokens);
	}
	
	@Override
	protected String getMatch(String text, Set<String> keywords) {
		for(String keyword : keywords)
			if(text.equals(keyword)
			|| (text.length() > keyword.length() && text.startsWith(keyword) && (
					Character.isWhitespace(text.charAt(keyword.length()))
				||	text.charAt(keyword.length()) == ':'
			)))
				return keyword;
		return null;
	}
	
	@Override
	protected Comment processComment(SortedMap<Location, String> tokens) {
		Comment comment = super.processComment(tokens);
		if(comment != null)
			comment.setText(formatText(rtrim(comment.getText())));
		return comment;
	}
	
	protected String formatText(String s) {
		if(s == null)
			return null;
		
		boolean whitespace = false;
		StringBuffer buffer = new StringBuffer();
		for(char c : s.toCharArray())
			if(Character.isWhitespace(c)) {
				if(!whitespace)
					buffer.append(' ');
				whitespace = true;
			}
			else {
				buffer.append(c);
				whitespace = false;
			}
		
		return buffer.toString();
	}
	
	protected String rtrim(String s) {
		if(s == null)
			return null;
		
		int i = s.length() - 1;
		while(i >= 0 && Character.isWhitespace(s.charAt(i)))
			i--;
		
		return i == s.length() - 1 ? s : s.substring(0, i + 1);
	}
	
}