package fherkin.io.impl;

import fherkin.model.entry.Examples;
import fherkin.model.location.Location;
import fherkin.model.location.SpreadsheetLocation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  AbstractSpreadsheetGherkinParser
 * 
 * @author Mike Clemens
 */
public class AbstractSpreadsheetGherkinParserTest extends AbstractSpreadsheetGherkinParser {
	
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	
	private AbstractSpreadsheetGherkinParserTest mockedInstance;
	private List<SortedMap<Location, String>> linesToProcess;
	private List<SortedMap<Location, String>> dataTableToProcess;
	
	@Before
	public void before() {
		filename = "abc.xlsx";
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("Sheet");
		row = sheet.createRow(0);
		
		linesToProcess = new ArrayList<SortedMap<Location, String>>();
		dataTableToProcess = new ArrayList<SortedMap<Location, String>>();
		
		mockedInstance = new AbstractSpreadsheetGherkinParserTest() {
			@Override
			protected void processLine(SortedMap<Location, String> tokens) {
				linesToProcess.add(tokens);
			}
			
			@Override
			protected void processDataTable(List<SortedMap<Location, String>> tokens) {
				dataTableToProcess = tokens;
			}
		};
		
		mockedInstance.filename = filename;
		mockedInstance.workbook = workbook;
		mockedInstance.sheet = sheet;
		mockedInstance.row = row;
		
		stack.clear();
	}
	
	///// processSheet
	
	@Test
	public void testProcessSheetBlankLine() {
		sheet.removeRow(row);
		addRow("", " \t \t \t ", "# comment");
		mockedInstance.processSheet(sheet);
		
		Assert.assertEquals(1, linesToProcess.size());
		Assert.assertEquals("# comment", format(linesToProcess.get(0)));
	}
	
	@Test
	public void testProcessSheetNormalLine() {
		sheet.removeRow(row);
		addRow("", "Given", "this is a given statement");
		mockedInstance.processSheet(sheet);
		
		Assert.assertEquals(1, linesToProcess.size());
		Assert.assertEquals("Given this is a given statement", format(linesToProcess.get(0)));
	}
	
	@Test
	public void testProcessSheetNormalLines() {
		sheet.removeRow(row);
		addRow("", "Given", "this is a given statement");
		addRow("", "When", "the when statement is run");
		addRow("", "Then", "the then statement will be run");
		mockedInstance.processSheet(sheet);
		
		Assert.assertEquals(3, linesToProcess.size());
		Assert.assertEquals("Given this is a given statement", format(linesToProcess.get(0)));
		Assert.assertEquals("When the when statement is run", format(linesToProcess.get(1)));
		Assert.assertEquals("Then the then statement will be run", format(linesToProcess.get(2)));
	}
	
	@Test
	public void testProcessSheetDataTableAtEnd() {
		sheet.removeRow(row);
		addRow("", "Examples:", "", "# comment");
		addRow("", "", "a", "b", "c");
		addRow("", "", "1", "2", "3");
		addRow("", "", "2", "3", "5");
		addRow("", "", "3", "5", "8");
		mockedInstance.processSheet(sheet);
		
		Assert.assertEquals(1, linesToProcess.size());
		Assert.assertEquals("Examples: # comment", format(linesToProcess.get(0)));
		
		Assert.assertEquals(4, dataTableToProcess.size());
		Assert.assertEquals("a b c", format(dataTableToProcess.get(0)));
		Assert.assertEquals("1 2 3", format(dataTableToProcess.get(1)));
		Assert.assertEquals("2 3 5", format(dataTableToProcess.get(2)));
		Assert.assertEquals("3 5 8", format(dataTableToProcess.get(3)));
	}
	
	@Test
	public void testProcessSheetDataTableInMiddle() {
		sheet.removeRow(row);
		addRow("", "Given", "there are some values", "# comment");
		addRow("", "", "a", "b", "c");
		addRow("", "", "1", "2", "3");
		addRow("", "", "2", "3", "5");
		addRow("", "", "3", "5", "8");
		addRow("", "When", "I do something with them");
		addRow("", "Then", "something will happen");
		mockedInstance.processSheet(sheet);
		
		Assert.assertEquals(3, linesToProcess.size());
		Assert.assertEquals("Given there are some values # comment", format(linesToProcess.get(0)));
		Assert.assertEquals("When I do something with them", format(linesToProcess.get(1)));
		Assert.assertEquals("Then something will happen", format(linesToProcess.get(2)));
		
		Assert.assertEquals(4, dataTableToProcess.size());
		Assert.assertEquals("a b c", format(dataTableToProcess.get(0)));
		Assert.assertEquals("1 2 3", format(dataTableToProcess.get(1)));
		Assert.assertEquals("2 3 5", format(dataTableToProcess.get(2)));
		Assert.assertEquals("3 5 8", format(dataTableToProcess.get(3)));
	}
	
	///// processDataTable
	
	@Test
	public void testProcessDataTable() {
		stack.push(new Examples());
		
		sheet.removeRow(row);
		List<SortedMap<Location, String>> dataTable = new ArrayList<SortedMap<Location, String>>();
		dataTable.add(tokenize(addRow("a", "b", "c")).getSecond());
		dataTable.add(tokenize(addRow("1", "2", "3")).getSecond());
		dataTable.add(tokenize(addRow("2", "3", "5")).getSecond());
		dataTable.add(tokenize(addRow("3", "5", "8")).getSecond());
		processDataTable(dataTable);
		
		String[][] expected = new String[][] {
				new String[] {"|", "a", "\t", "|", "b", "\t", "|", "c", "|"},
				new String[] {"|", "1", "\t", "|", "2", "\t", "|", "3", "|"},
				new String[] {"|", "2", "\t", "|", "3", "\t", "|", "5", "|"},
				new String[] {"|", "3", "\t", "|", "5", "\t", "|", "8", "|"}
		};
		
		int rowNum = -1;
		int cellNum, i;
		SpreadsheetLocation location;
		String[] expectedRow;
		String expectedCell;
		Assert.assertEquals(expected.length, dataTable.size());
		for(SortedMap<Location, String> tokens : dataTable) {
			expectedRow = expected[++rowNum];
			Assert.assertEquals(expectedRow.length, tokens.size());
			
			cellNum = 0;
			i = -1;
			for(Map.Entry<Location, String> entry : tokens.entrySet()) {
				location = (SpreadsheetLocation) entry.getKey();
				expectedCell = expectedRow[++i];
				
				Assert.assertEquals(expectedCell, entry.getValue());
				Assert.assertEquals(rowNum, location.getRow());
				
				if(i + 1 < expectedRow.length)
					Assert.assertEquals(cellNum, location.getColumn());
				else
					Assert.assertEquals(cellNum + 1, location.getColumn());
				
				if(expectedCell.equals("|"))
					Assert.assertEquals(-1, location.getPosition());
				else
				if(expectedCell.equals("\t")) {
					Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
					cellNum++;
				}
				else
					Assert.assertEquals(0, location.getPosition());
			}
		}
	}
	
	@Test
	public void testProcessDataTableLeadingSpaces() {
		stack.push(new Examples());
		
		sheet.removeRow(row);
		List<SortedMap<Location, String>> dataTable = new ArrayList<SortedMap<Location, String>>();
		dataTable.add(tokenize(addRow("", "", "a", "b", "c")).getSecond());
		dataTable.add(tokenize(addRow("", "", "1", "2", "3")).getSecond());
		dataTable.add(tokenize(addRow("", "", "2", "3", "5")).getSecond());
		dataTable.add(tokenize(addRow("", "", "3", "5", "8")).getSecond());
		processDataTable(dataTable);
		
		String[][] expected = new String[][] {
				new String[] {"", "\t", "", "\t", "|", "a", "\t", "|", "b", "\t", "|", "c", "|"},
				new String[] {"", "\t", "", "\t", "|", "1", "\t", "|", "2", "\t", "|", "3", "|"},
				new String[] {"", "\t", "", "\t", "|", "2", "\t", "|", "3", "\t", "|", "5", "|"},
				new String[] {"", "\t", "", "\t", "|", "3", "\t", "|", "5", "\t", "|", "8", "|"}
		};
		
		int rowNum = -1;
		int cellNum, i;
		SpreadsheetLocation location;
		String[] expectedRow;
		String expectedCell;
		Assert.assertEquals(expected.length, dataTable.size());
		for(SortedMap<Location, String> tokens : dataTable) {
			expectedRow = expected[++rowNum];
			Assert.assertEquals(expectedRow.length, tokens.size());
			
			cellNum = 0;
			i = -1;
			for(Map.Entry<Location, String> entry : tokens.entrySet()) {
				location = (SpreadsheetLocation) entry.getKey();
				expectedCell = expectedRow[++i];
				
				Assert.assertEquals(expectedCell, entry.getValue());
				Assert.assertEquals(rowNum, location.getRow());
				
				if(i + 1 < expectedRow.length)
					Assert.assertEquals(cellNum, location.getColumn());
				else
					Assert.assertEquals(cellNum + 1, location.getColumn());
				
				if(expectedCell.equals("|"))
					Assert.assertEquals(-1, location.getPosition());
				else
				if(expectedCell.equals("\t")) {
					Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
					cellNum++;
				}
				else
					Assert.assertEquals(0, location.getPosition());
			}
		}
	}
	
	@Test
	public void testProcessDataTableTrailingSpaces() {
		stack.push(new Examples());
		
		sheet.removeRow(row);
		List<SortedMap<Location, String>> dataTable = new ArrayList<SortedMap<Location, String>>();
		dataTable.add(tokenize(addRow("a", "b", "c", "", "")).getSecond());
		dataTable.add(tokenize(addRow("1", "2", "3", "", "")).getSecond());
		dataTable.add(tokenize(addRow("2", "3", "5", "", "")).getSecond());
		dataTable.add(tokenize(addRow("3", "5", "8", "", "")).getSecond());
		processDataTable(dataTable);
		
		String[][] expected = new String[][] {
				new String[] {"|", "a", "\t", "|", "b", "\t", "|", "c", "\t", "|", "", "\t", ""},
				new String[] {"|", "1", "\t", "|", "2", "\t", "|", "3", "\t", "|", "", "\t", ""},
				new String[] {"|", "2", "\t", "|", "3", "\t", "|", "5", "\t", "|", "", "\t", ""},
				new String[] {"|", "3", "\t", "|", "5", "\t", "|", "8", "\t", "|", "", "\t", ""}
		};
		
		int rowNum = -1;
		int cellNum, i;
		SpreadsheetLocation location;
		String[] expectedRow;
		String expectedCell;
		Assert.assertEquals(expected.length, dataTable.size());
		for(SortedMap<Location, String> tokens : dataTable) {
			expectedRow = expected[++rowNum];
			Assert.assertEquals(expectedRow.length, tokens.size());
			
			cellNum = 0;
			i = -1;
			for(Map.Entry<Location, String> entry : tokens.entrySet()) {
				location = (SpreadsheetLocation) entry.getKey();
				expectedCell = expectedRow[++i];
				
				Assert.assertEquals(expectedCell, entry.getValue());
				Assert.assertEquals(rowNum, location.getRow());
				Assert.assertEquals(cellNum, location.getColumn());
				
				if(expectedCell.equals("|"))
					Assert.assertEquals(-1, location.getPosition());
				else
				if(expectedCell.equals("\t")) {
					Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
					cellNum++;
				}
				else
					Assert.assertEquals(0, location.getPosition());
			}
		}
	}
	
	@Test
	public void testProcessDataTableSpacesInTable() {
		stack.push(new Examples());
		
		sheet.removeRow(row);
		List<SortedMap<Location, String>> dataTable = new ArrayList<SortedMap<Location, String>>();
		dataTable.add(tokenize(addRow("a", "", "", "b", "", "c")).getSecond());
		dataTable.add(tokenize(addRow("1", "", "", "2", "", "3")).getSecond());
		dataTable.add(tokenize(addRow("2", "", "", "3", "", "5")).getSecond());
		dataTable.add(tokenize(addRow("3", "", "", "5", "", "8")).getSecond());
		processDataTable(dataTable);
		
		String[][] expected = new String[][] {
				new String[] {"|", "a", "\t", "", "\t", "", "\t", "|", "b", "\t", "", "\t", "|", "c", "|"},
				new String[] {"|", "1", "\t", "", "\t", "", "\t", "|", "2", "\t", "", "\t", "|", "3", "|"},
				new String[] {"|", "2", "\t", "", "\t", "", "\t", "|", "3", "\t", "", "\t", "|", "5", "|"},
				new String[] {"|", "3", "\t", "", "\t", "", "\t", "|", "5", "\t", "", "\t", "|", "8", "|"}
		};
		
		int rowNum = -1;
		int cellNum, i;
		SpreadsheetLocation location;
		String[] expectedRow;
		String expectedCell;
		Assert.assertEquals(expected.length, dataTable.size());
		for(SortedMap<Location, String> tokens : dataTable) {
			expectedRow = expected[++rowNum];
			Assert.assertEquals(expectedRow.length, tokens.size());
			
			cellNum = 0;
			i = -1;
			for(Map.Entry<Location, String> entry : tokens.entrySet()) {
				location = (SpreadsheetLocation) entry.getKey();
				expectedCell = expectedRow[++i];
				
				Assert.assertEquals(expectedCell, entry.getValue());
				Assert.assertEquals(rowNum, location.getRow());
				
				if(i + 1 < expectedRow.length)
					Assert.assertEquals(cellNum, location.getColumn());
				else
					Assert.assertEquals(cellNum + 1, location.getColumn());
				
				if(expectedCell.equals("|"))
					Assert.assertEquals(-1, location.getPosition());
				else
				if(expectedCell.equals("\t")) {
					Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
					cellNum++;
				}
				else
					Assert.assertEquals(0, location.getPosition());
			}
		}
	}
	
	@Test
	public void testProcessDataTableBlankCellValues() {
		stack.push(new Examples());
		
		sheet.removeRow(row);
		List<SortedMap<Location, String>> dataTable = new ArrayList<SortedMap<Location, String>>();
		dataTable.add(tokenize(addRow("a", "b", "c")).getSecond());
		dataTable.add(tokenize(addRow("",  "2", "3")).getSecond());
		dataTable.add(tokenize(addRow("2", "",  "5")).getSecond());
		dataTable.add(tokenize(addRow("3", "5", "" )).getSecond());
		processDataTable(dataTable);
		
		String[][] expected = new String[][] {
				new String[] {"|", "a", "\t", "|", "b", "\t", "|", "c", "|"},
				new String[] {"|", "",  "\t", "|", "2", "\t", "|", "3", "|"},
				new String[] {"|", "2", "\t", "|", "",  "\t", "|", "5", "|"},
				new String[] {"|", "3", "\t", "|", "5", "\t", "|", "",  "|"}
		};
		
		int rowNum = -1;
		int cellNum, i;
		SpreadsheetLocation location;
		String[] expectedRow;
		String expectedCell;
		Assert.assertEquals(expected.length, dataTable.size());
		for(SortedMap<Location, String> tokens : dataTable) {
			expectedRow = expected[++rowNum];
			Assert.assertEquals(expectedRow.length, tokens.size());
			
			cellNum = 0;
			i = -1;
			for(Map.Entry<Location, String> entry : tokens.entrySet()) {
				location = (SpreadsheetLocation) entry.getKey();
				expectedCell = expectedRow[++i];
				
				Assert.assertEquals(expectedCell, entry.getValue());
				Assert.assertEquals(rowNum, location.getRow());
				
				if(i + 1 < expectedRow.length)
					Assert.assertEquals(cellNum, location.getColumn());
				else
					Assert.assertEquals(cellNum + 1, location.getColumn());
				
				if(expectedCell.equals("|"))
					Assert.assertEquals(-1, location.getPosition());
				else
				if(expectedCell.equals("\t")) {
					Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
					cellNum++;
				}
				else
					Assert.assertEquals(0, location.getPosition());
			}
		}
	}
	
	@Test
	public void testProcessDataTableTooFewColumns() {
		stack.push(new Examples());
		
		sheet.removeRow(row);
		List<SortedMap<Location, String>> dataTable = new ArrayList<SortedMap<Location, String>>();
		dataTable.add(tokenize(addRow("a", "b", "c")).getSecond());
		dataTable.add(tokenize(addRow("1", "2"     )).getSecond());
		dataTable.add(tokenize(addRow("2"          )).getSecond());
		dataTable.add(tokenize(addRow("3", "5", "8")).getSecond());
		processDataTable(dataTable);
		
		String[][] expected = new String[][] {
				new String[] {"|", "a", "\t", "|", "b", "\t", "|", "c", "|"},
				new String[] {"|", "1", "\t", "|", "2", "|", "|"},
				new String[] {"|", "2", "|", "|", "|"},
				new String[] {"|", "3", "\t", "|", "5", "\t", "|", "8", "|"}
		};
		
		int rowNum = -1;
		int cellNum, i;
		SpreadsheetLocation location;
		String[] expectedRow;
		String expectedCell;
		Assert.assertEquals(expected.length, dataTable.size());
		for(SortedMap<Location, String> tokens : dataTable) {
			expectedRow = expected[++rowNum];
			Assert.assertEquals(expectedRow.length, tokens.size());
			
			cellNum = 0;
			i = -1;
			for(Map.Entry<Location, String> entry : tokens.entrySet()) {
				location = (SpreadsheetLocation) entry.getKey();
				expectedCell = expectedRow[++i];
				
				Assert.assertEquals(expectedCell, entry.getValue());
				Assert.assertEquals(rowNum, location.getRow());
				
				switch(rowNum) {
					case 0:
					case 3:
						if(i + 1 < expectedRow.length)
							Assert.assertEquals(cellNum, location.getColumn());
						else
							Assert.assertEquals(cellNum + 1, location.getColumn());
						break;
					case 1:
						if(i + 2 < expectedRow.length)
							Assert.assertEquals(cellNum, location.getColumn());
						else
						if(i + 1 < expectedRow.length)
							Assert.assertEquals(cellNum + 1, location.getColumn());
						else
							Assert.assertEquals(cellNum + 2, location.getColumn());
						break;
					case 2:
						if(i + 3 < expectedRow.length)
							Assert.assertEquals(cellNum, location.getColumn());
						else
						if(i + 2 < expectedRow.length)
							Assert.assertEquals(cellNum + 1, location.getColumn());
						else
						if(i + 1 < expectedRow.length)
							Assert.assertEquals(cellNum + 2, location.getColumn());
						else
							Assert.assertEquals(cellNum + 3, location.getColumn());
				}
				
				if(expectedCell.equals("|"))
					Assert.assertEquals(-1, location.getPosition());
				else
				if(expectedCell.equals("\t")) {
					Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
					cellNum++;
				}
				else
					Assert.assertEquals(0, location.getPosition());
			}
		}
	}
	
	///// isKeyword
	
	@Test
	public void testIsKeywordNull() {
		Assert.assertFalse(isKeyword(null));
	}
	
	@Test
	public void testIsKeywordWithColon() {
		Assert.assertTrue(isKeyword("Feature:"));
		Assert.assertTrue(isKeyword("Background:"));
		Assert.assertTrue(isKeyword("Scenario:"));
		Assert.assertTrue(isKeyword("Scenario Outline:"));
		Assert.assertTrue(isKeyword("Rule:"));
		Assert.assertTrue(isKeyword("Examples:"));
		
		Assert.assertFalse(isKeyword("Feature"));
		Assert.assertFalse(isKeyword("Background"));
		Assert.assertFalse(isKeyword("Scenario"));
		Assert.assertFalse(isKeyword("Scenario Outline"));
		Assert.assertFalse(isKeyword("Rule"));
		Assert.assertFalse(isKeyword("Examples"));

		Assert.assertFalse(isKeyword("Not a Real Keyword:"));
	}
		
	@Test
	public void testIsKeywordWithoutColon() {
		Assert.assertTrue(isKeyword("Given"));
		Assert.assertTrue(isKeyword("When"));
		Assert.assertTrue(isKeyword("Then"));
		Assert.assertTrue(isKeyword("And"));
		Assert.assertTrue(isKeyword("But"));
		
		Assert.assertFalse(isKeyword("Given:"));
		Assert.assertFalse(isKeyword("When:"));
		Assert.assertFalse(isKeyword("Then:"));
		Assert.assertFalse(isKeyword("And:"));
		Assert.assertFalse(isKeyword("But:"));
		
		Assert.assertFalse(isKeyword("Not a Real Keyword"));
	}
	
	///// isDataTableKeyword
	
	@Test
	public void testIsDataTableKeywordNull() {
		Assert.assertFalse(isDataTableKeyword(null));
	}
	
	@Test
	public void testIsDataTableKeywordWithColon() {
		Assert.assertTrue(isDataTableKeyword("Examples:"));
		Assert.assertFalse(isDataTableKeyword("Examples"));

		Assert.assertFalse(isDataTableKeyword("Feature:"));
		Assert.assertFalse(isDataTableKeyword("Background:"));
		Assert.assertFalse(isDataTableKeyword("Scenario:"));
		Assert.assertFalse(isDataTableKeyword("Scenario Outline:"));
		Assert.assertFalse(isDataTableKeyword("Rule:"));
		Assert.assertFalse(isDataTableKeyword("Not a Real Keyword:"));
	}
		
	@Test
	public void testIsDataTableKeywordWithoutColon() {
		Assert.assertTrue(isDataTableKeyword("Given"));
		Assert.assertTrue(isDataTableKeyword("When"));
		Assert.assertTrue(isDataTableKeyword("Then"));
		Assert.assertTrue(isDataTableKeyword("And"));
		Assert.assertTrue(isDataTableKeyword("But"));
		
		Assert.assertFalse(isDataTableKeyword("Given:"));
		Assert.assertFalse(isDataTableKeyword("When:"));
		Assert.assertFalse(isDataTableKeyword("Then:"));
		Assert.assertFalse(isDataTableKeyword("And:"));
		Assert.assertFalse(isDataTableKeyword("But:"));
		
		Assert.assertFalse(isDataTableKeyword("Not a Real Keyword"));
	}
	
	///// tokenize
	
	@Test
	public void testTokenize() {
		String[] expected = new String[] {
				"",
				" \t \t \t ",
				"Given ",
				"this is a line of tokens",
				"",
				"# comment"
		};
		
		String[] actual = new String[] {
				"",
				"\t",
				" \t \t \t ",
				"\t",
				"Given ",
				"\t",
				"this is a line of tokens",
				"\t",
				"",
				"\t",
				"#",
				" comment"
		};
		
		addCells((Object[]) expected);
		Pair<String, SortedMap<Location, String>> result = tokenize(row);
		Assert.assertEquals("Given", result.getFirst());
		
		SortedMap<Location, String> tokens = result.getSecond();
		Assert.assertEquals(actual.length, tokens.size());
		int i = -1;
		SpreadsheetLocation location;
		for(Map.Entry<Location, String> entry : tokens.entrySet()) {
			location = (SpreadsheetLocation) entry.getKey();
			i++;
			
			Assert.assertEquals("abc.xlsx", location.getFilename());
			Assert.assertEquals("Sheet", location.getSheet());
			Assert.assertEquals(0, location.getRow());
			Assert.assertEquals(i / 2, location.getColumn());
			Assert.assertEquals(actual[i], entry.getValue());
			
			if(i == 11)
				Assert.assertEquals(1, location.getPosition());
			else
			if(actual[i].equals("\t"))
				Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
			else
				Assert.assertEquals(0, location.getPosition());
		}
	}
	
	@Test
	public void testTokenizeCommentAtBeginningOfCell() {
		addCells("", "", "# comment");

		String[] actual = new String[] {
				"",
				"\t",
				"",
				"\t",
				"#",
				" comment"
		};
		
		Pair<String, SortedMap<Location, String>> result = tokenize(row);
		Assert.assertNull(result.getFirst());
		
		SortedMap<Location, String> tokens = result.getSecond();
		Assert.assertEquals(actual.length, tokens.size());
		int i = -1;
		SpreadsheetLocation location;
		for(Map.Entry<Location, String> entry : tokens.entrySet()) {
			location = (SpreadsheetLocation) entry.getKey();
			i++;
			
			Assert.assertEquals("abc.xlsx", location.getFilename());
			Assert.assertEquals("Sheet", location.getSheet());
			Assert.assertEquals(0, location.getRow());
			Assert.assertEquals(i / 2, location.getColumn());
			Assert.assertEquals(actual[i], entry.getValue());
			
			if(i == 5)
				Assert.assertEquals(1, location.getPosition());
			else
			if(actual[i].equals("\t"))
				Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
			else
				Assert.assertEquals(0, location.getPosition());
		}
	}
	
	@Test
	public void testTokenizeCommentAtEndOfCell() {
		addCells("", "", "#", "comment");

		String[] actual = new String[] {
				"",
				"\t",
				"",
				"\t",
				"#",
				"\t",
				"comment"
		};
		
		Pair<String, SortedMap<Location, String>> result = tokenize(row);
		Assert.assertNull(result.getFirst());
		
		SortedMap<Location, String> tokens = result.getSecond();
		Assert.assertEquals(actual.length, tokens.size());
		int i = -1;
		SpreadsheetLocation location;
		for(Map.Entry<Location, String> entry : tokens.entrySet()) {
			location = (SpreadsheetLocation) entry.getKey();
			i++;
			
			Assert.assertEquals("abc.xlsx", location.getFilename());
			Assert.assertEquals("Sheet", location.getSheet());
			Assert.assertEquals(0, location.getRow());
			Assert.assertEquals(i / 2, location.getColumn());
			Assert.assertEquals(actual[i], entry.getValue());
			
			if(actual[i].equals("\t"))
				Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
			else
				Assert.assertEquals(0, location.getPosition());
		}
	}
	
	@Test
	public void testTokenizeCommentInMiddleOfCell() {
		addCells("", "", "123 ### this is a", "comment");

		String[] actual = new String[] {
				"",
				"\t",
				"",
				"\t",
				"123 ",
				"#",
				"## this is a",
				"\t",
				"comment"
		};
		
		Pair<String, SortedMap<Location, String>> result = tokenize(row);
		Assert.assertEquals("123", result.getFirst());
		
		SortedMap<Location, String> tokens = result.getSecond();
		Assert.assertEquals(actual.length, tokens.size());
		int i = -1;
		SpreadsheetLocation location;
		for(Map.Entry<Location, String> entry : tokens.entrySet()) {
			location = (SpreadsheetLocation) entry.getKey();
			i++;
			
			Assert.assertEquals("abc.xlsx", location.getFilename());
			Assert.assertEquals("Sheet", location.getSheet());
			Assert.assertEquals(0, location.getRow());
			Assert.assertEquals(actual[i], entry.getValue());

			if(i < 5)
				Assert.assertEquals(i / 2, location.getColumn());
			else
			if(i == 5) {
				Assert.assertEquals(2, location.getColumn());
				Assert.assertEquals(4, location.getPosition());
			}
			else
			if(i == 6) {
				Assert.assertEquals(2, location.getColumn());
				Assert.assertEquals(5, location.getPosition());
			}
			else
			if(i == 7) {
				Assert.assertEquals(2, location.getColumn());
				Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
			}
			else
			if(i == 8) {
				Assert.assertEquals(3, location.getColumn());
				Assert.assertEquals(0, location.getPosition());
			}
			else {
				Assert.assertEquals(i / 2, location.getColumn());
				if(actual[i].equals("\t"))
					Assert.assertEquals(Integer.MAX_VALUE, location.getPosition());
				else
					Assert.assertEquals(0, location.getPosition());
			}
		}
	}
	
	@Test
	public void testTokenizeBlank() {
		Pair<String, SortedMap<Location, String>> result = tokenize(row);
		Assert.assertNull(result.getFirst());
		Assert.assertEquals(0, result.getSecond().size());
	}
	
	///// formatText
	
	@Test
	public void testFormatText() {
		Assert.assertNull(formatText(null));
		Assert.assertEquals("abc", formatText("abc"));
		Assert.assertEquals("a b c", formatText("a b c"));
		Assert.assertEquals("a b c", formatText("a  b  c"));
		Assert.assertEquals("a b c", formatText("a \t\n\r b \t\n\r c"));
		Assert.assertEquals(" a b c ", formatText(" \t\n\r a \t\n\r b \t\n\r c \t\n\r "));
	}
	
	///// rtrim
	
	@Test
	public void testRtrim() {
		Assert.assertNull(rtrim(null));
		Assert.assertEquals("abc", rtrim("abc"));
		Assert.assertEquals("abc", rtrim("abc "));
		Assert.assertEquals("abc", rtrim("abc \t\n\r "));
		Assert.assertEquals(" \t\n\r abc", rtrim(" \t\n\r abc \t\n\r "));
	}
	
	///// overridden/helper methods

	@Override
	protected Workbook parseWorkbook() throws IOException {
		return null;
	}
	
	protected Row addRow(Object... values) {
		row = sheet.createRow(sheet.getPhysicalNumberOfRows());
		addCells(values);
		return row;
	}
	
	protected void addCells(Object... values) {
		Cell cell;
		int i = 0;
		for(Object value : values) {
			cell = row.createCell(i++);
			
			if(value == null)
				cell.setBlank();
			else
			if(value instanceof Number)
				cell.setCellValue((Double) value);
			else
			if(value instanceof Boolean)
				cell.setCellValue((Boolean) value);
			else
			if(value instanceof String)
				cell.setCellValue(value.toString());
		}
	}
	
	protected String format(SortedMap<Location, String> map) {
		StringBuffer buffer = new StringBuffer();
		for(Map.Entry<Location, String> entry : map.entrySet()) {
			buffer.append(entry.getValue());
			buffer.append("\t");
		}
		
		return buffer.toString().replaceAll("\\s{1,}", " ").trim();
	}

}