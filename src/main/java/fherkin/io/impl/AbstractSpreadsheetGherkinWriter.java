package fherkin.io.impl;

import fherkin.LogHelper;
import fherkin.model.GherkinEntry;
import fherkin.model.datatable.DataTable;
import fherkin.model.datatable.DataTable.CellType;
import fherkin.model.datatable.DataTableCell;
import fherkin.model.datatable.DataTableRow;
import fherkin.model.entry.Comment;
import fherkin.model.entry.Examples;
import fherkin.model.entry.Feature;
import fherkin.model.entry.Scenario;
import fherkin.model.entry.Step;
import fherkin.model.entry.Tag;
import fherkin.style.CellStyleConfig;
import fherkin.style.FontConfig;
import fherkin.style.SpreadsheetStyleConfig;
import fherkin.style.SpreadsheetStyleConfigFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Base class for a gherkin writer that writes to a spreadsheet.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public abstract class AbstractSpreadsheetGherkinWriter extends AbstractGherkinWriter {

	protected static final Pattern VARIABLE_PATTERN = Pattern.compile("\\<\\S.*?\\S\\>");
	
	protected Log log = LogFactory.getLog(getClass());
	
	protected Workbook workbook;
	protected OutputStream outputStream;
	protected String filename;
	protected SpreadsheetStyleConfig spreadsheetStyleConfig;
	protected Map<Pair<CellStyleConfig, FontConfig>, CellStyle> fontAndCellStyles;
	protected Map<CellStyleConfig, CellStyle> cellStyles;
	protected Map<FontConfig, Font> fonts;
	protected int maxColumn;
	
	public AbstractSpreadsheetGherkinWriter(File file) throws IOException {
		this();
		
		outputStream = new FileOutputStream(file);
		
		filename = file.getName();
		if(filename.indexOf('.') > -1)
			filename = filename.substring(0, filename.lastIndexOf('.'));
	}
	
	public AbstractSpreadsheetGherkinWriter(String filename, OutputStream outputStream) throws IOException {
		this();
		
		this.outputStream = outputStream;
		this.filename = filename;
	}
	
	protected AbstractSpreadsheetGherkinWriter() {
		spreadsheetStyleConfig = SpreadsheetStyleConfigFactory.getInstance().getStyleConfig();
		
		fontAndCellStyles = new HashMap<Pair<CellStyleConfig, FontConfig>, CellStyle>();
		cellStyles = new HashMap<CellStyleConfig, CellStyle>();
		fonts = new HashMap<FontConfig, Font>();
	}
	
	@Override
	public void write(List<GherkinEntry> entries) throws IOException {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "write");
		
		Workbook workbook = newWorkbook();
		Sheet sheet = newSheet(workbook, filename);
		maxColumn = 0;
		
		for(GherkinEntry entry : entries)
			if(entry instanceof Feature)
				writeFeature(sheet, (Feature) entry);
			else
			if(entry instanceof Scenario)
				writeScenario(sheet, (Scenario) entry);
			else
			if(entry instanceof Step)
				writeStep(sheet, (Step) entry);
			else
			if(entry instanceof Examples)
				writeExamples(sheet, (Examples) entry);
			else
			if(entry instanceof DataTableRow)
				writeDataTableRow(sheet, (DataTableRow) entry);
			else
			if(entry instanceof Comment)
				writeComment(sheet, (Comment) entry);
		
		adjustColumnWidths(sheet);
		doWrite(workbook);
	}
	
	protected void adjustColumnWidths(Sheet sheet) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "adjustColumnWidths", sheet);
		
		sheet.autoSizeColumn(0);
		for(int i = 2; i < maxColumn; i++)
			sheet.autoSizeColumn(i);
	}
		
	protected void writeFeature(Sheet sheet, Feature feature) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "writeFeature", sheet, feature);
		
		// if the feature has any tags, write them prior to the feature line
		if(feature.getTags() != null && feature.getTags().size() > 0)
			for(Tag tag : feature.getTags())
				writeTag(sheet, tag);
		
		// initial row
		Row row = newRow(sheet);
		
		Cell a = newCell(row, 0);
		a.setCellValue(feature.getKeyword() + ":");
		setCellStyleAndFont(a, spreadsheetStyleConfig.getFeatureCellStyle(), spreadsheetStyleConfig.getFeatureFont());
		
		Cell b = newCell(row, 1);
		if(feature.getComment() == null) {
			b.setCellValue(feature.getText().trim());
			setCellStyleAndFont(b, spreadsheetStyleConfig.getFeatureTextCellStyle(), spreadsheetStyleConfig.getFeatureTextFont());
		}
		else {
			setCellStyle(b, spreadsheetStyleConfig.getFeatureTextCellStyle());
			setRichText(b, new String[] {feature.getText().trim(), "  #" + feature.getComment().getText()}, new FontConfig[] {spreadsheetStyleConfig.getFeatureTextFont(), spreadsheetStyleConfig.getCommentFont()});
		}
		
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), b.getColumnIndex(), 25));
		
		// additional text rows
		if(feature.getAdditionalText() != null && feature.getAdditionalText().size() > 0)
			for(String text : feature.getAdditionalText()) {
				row = newRow(sheet);
				
				b = newCell(row, 1);
				b.setCellValue(text.trim());
				setCellStyleAndFont(b, spreadsheetStyleConfig.getFeatureTextCellStyle(), spreadsheetStyleConfig.getFeatureTextFont());
				
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), b.getColumnIndex(), 25));
			}
	}
	
	protected void writeScenario(Sheet sheet, Scenario scenario) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "writeScenario", sheet, scenario);
		
		newRow(sheet);
		
		// if the scenario has any tags, write them prior to the feature line
		if(scenario.getTags() != null && scenario.getTags().size() > 0)
			for(Tag tag : scenario.getTags())
				writeTag(sheet, tag);
		
		CellStyleConfig cellStyle;
		FontConfig font;
		CellStyleConfig textCellStyle;
		FontConfig textFont;
		switch(scenario.getType()) {
			case BACKGROUND:
				cellStyle = spreadsheetStyleConfig.getBackgroundCellStyle();
				font = spreadsheetStyleConfig.getBackgroundFont();
				textCellStyle = spreadsheetStyleConfig.getBackgroundTextCellStyle();
				textFont = spreadsheetStyleConfig.getBackgroundTextFont();
				break;
			case SCENARIO:
				cellStyle = spreadsheetStyleConfig.getScenarioCellStyle();
				font = spreadsheetStyleConfig.getScenarioFont();
				textCellStyle = spreadsheetStyleConfig.getScenarioTextCellStyle();
				textFont = spreadsheetStyleConfig.getScenarioTextFont();
				break;
			case SCENARIO_OUTLINE:
				cellStyle = spreadsheetStyleConfig.getScenarioOutlineCellStyle();
				font = spreadsheetStyleConfig.getScenarioOutlineFont();
				textCellStyle = spreadsheetStyleConfig.getScenarioOutlineTextCellStyle();
				textFont = spreadsheetStyleConfig.getScenarioOutlineTextFont();
				break;
			default:
				throw new IllegalArgumentException();
		}
		
		Row row = newRow(sheet);
		
		Cell a = newCell(row, 0);
		setCellStyleAndFont(a, cellStyle, font);
		a.setCellValue(scenario.getKeyword() + ":");
		
		Cell b = newCell(row, 1);
		if(scenario.getComment() == null) {
			b.setCellValue(scenario.getText().trim());
			setCellStyleAndFont(b, textCellStyle, textFont);
		}
		else {
			setCellStyle(b, textCellStyle);
			setRichText(b, new String[] {scenario.getText().trim(), "  #" + scenario.getComment().getText()}, new FontConfig[] {textFont, spreadsheetStyleConfig.getCommentFont()});
		}
		
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), b.getColumnIndex(), 25));
	}
	
	protected void writeStep(Sheet sheet, Step step) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "writeStep", sheet, step);
		
		Pair<String, Boolean>[] tokens = extractTextVariables(step.getText().trim());
		CellStyleConfig cellStyle;
		FontConfig font;
		CellStyleConfig textCellStyle;
		FontConfig textFont;
		FontConfig variableTextFont;
		switch(step.getType()) {
			case GIVEN:
				cellStyle = spreadsheetStyleConfig.getGivenCellStyle();
				font = spreadsheetStyleConfig.getGivenFont();
				textCellStyle = spreadsheetStyleConfig.getGivenTextCellStyle();
				textFont = spreadsheetStyleConfig.getGivenTextFont();
				variableTextFont = spreadsheetStyleConfig.getGivenTextVariableFont();
				break;
			case WHEN:
				cellStyle = spreadsheetStyleConfig.getWhenCellStyle();
				font = spreadsheetStyleConfig.getWhenFont();
				textCellStyle = spreadsheetStyleConfig.getWhenTextCellStyle();
				textFont = spreadsheetStyleConfig.getWhenTextFont();
				variableTextFont = spreadsheetStyleConfig.getWhenTextVariableFont();
				break;
			case THEN:
				cellStyle = spreadsheetStyleConfig.getThenCellStyle();
				font = spreadsheetStyleConfig.getThenFont();
				textCellStyle = spreadsheetStyleConfig.getThenTextCellStyle();
				textFont = spreadsheetStyleConfig.getThenTextFont();
				variableTextFont = spreadsheetStyleConfig.getThenTextVariableFont();
				break;
			case AND:
				cellStyle = spreadsheetStyleConfig.getAndCellStyle();
				font = spreadsheetStyleConfig.getAndFont();
				textCellStyle = spreadsheetStyleConfig.getAndTextCellStyle();
				textFont = spreadsheetStyleConfig.getAndTextFont();
				variableTextFont = spreadsheetStyleConfig.getAndTextVariableFont();
				break;
			case BUT:
				cellStyle = spreadsheetStyleConfig.getButCellStyle();
				font = spreadsheetStyleConfig.getButFont();
				textCellStyle = spreadsheetStyleConfig.getButTextCellStyle();
				textFont = spreadsheetStyleConfig.getButTextFont();
				variableTextFont = spreadsheetStyleConfig.getButTextVariableFont();
				break;
			default:
				throw new IllegalArgumentException();
		}
		
		Row row = newRow(sheet);
		
		Cell a = newCell(row, 0);
		setCellStyleAndFont(a, cellStyle, font);
		a.setCellValue(step.getKeyword());
		
		Cell b = newCell(row, 1);
		if(step.getComment() == null && tokens.length == 1) {
			b.setCellValue(step.getText().trim());
			setCellStyleAndFont(b, textCellStyle, tokens[0].getSecond().booleanValue() ? variableTextFont : textFont);
		}
		else {
			int length = tokens.length;
			if(step.getComment() != null)
				length++;
			
			String[] texts = new String[length];
			FontConfig[] fonts = new FontConfig[length];
			for(int i = 0; i < tokens.length; i++) {
				texts[i] = tokens[i].getFirst();
				fonts[i] = tokens[i].getSecond() ? variableTextFont : textFont;
			}
			
			if(step.getComment() != null) {
				texts[length - 1] = "  #" + step.getComment().getText();
				fonts[length - 1] = spreadsheetStyleConfig.getCommentFont();
			}

			setCellStyle(b, textCellStyle);
			setRichText(b, texts, fonts);
		}
		
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), b.getColumnIndex(), 25));
	}
	
	protected void writeExamples(Sheet sheet, Examples examples) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "writeExamples", sheet, examples);
		
		Row row = newRow(sheet);
		
		Cell b = newCell(row, 1);
		if(examples.getComment() == null) {
			b.setCellValue(examples.getKeyword() + ":");
			setCellStyleAndFont(b, spreadsheetStyleConfig.getExamplesCellStyle(), spreadsheetStyleConfig.getExamplesFont());
		}
		else {
			setCellStyle(b, spreadsheetStyleConfig.getExamplesCellStyle());
			setRichText(b, new String[] {examples.getKeyword() + ":", "  #" + examples.getComment().getText()}, new FontConfig[] {spreadsheetStyleConfig.getExamplesFont(), spreadsheetStyleConfig.getCommentFont()});
		}
		
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), b.getColumnIndex(), 25));
	}
	
	protected void writeDataTableRow(Sheet sheet, DataTableRow dataTableRow) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "writeDataTableRow", sheet, dataTableRow);
		
		Row row = newRow(sheet);
		
		DataTable dataTable = dataTableRow.getDataTable();
		int width = dataTable.getWidth();
		Iterator<DataTableCell> iterator = dataTableRow.getCells().iterator();
		DataTableCell dataTableCell;
		Cell cell;
		CellType cellType;
		for(int i = 0; i < width; i++) {
			cell = newCell(row, i + 2);

			dataTableCell = iterator.hasNext() ? iterator.next() : null;
			cellType = dataTable.getColumnType(i);
			if(cellType == CellType.INTEGER || cellType == CellType.FLOAT) {
				if(dataTableRow.getRowNumber() == 0)
					setCellStyleAndFont(cell, spreadsheetStyleConfig.getDataTableHeadingCellStyleAlignRight(), spreadsheetStyleConfig.getDataTableHeadingFontAlignRight());
				else
					setCellStyleAndFont(cell, spreadsheetStyleConfig.getDataTableRowCellStyleAlignRight(), spreadsheetStyleConfig.getDataTableRowFontAlignRight());
			}
			else {
				if(dataTableRow.getRowNumber() == 0)
					setCellStyleAndFont(cell, spreadsheetStyleConfig.getDataTableHeadingCellStyleAlignLeft(), spreadsheetStyleConfig.getDataTableHeadingFontAlignLeft());
				else
					setCellStyleAndFont(cell, spreadsheetStyleConfig.getDataTableRowCellStyleAlignLeft(), spreadsheetStyleConfig.getDataTableRowFontAlignLeft());
			}
			
			if(dataTableCell != null && dataTableCell.getValue() != null && dataTableCell.getValue().trim().length() > 0) {
				if(dataTableRow.getRowNumber() == 0)
					cell.setCellValue(dataTableCell.getValue());
				else
					switch(cellType) {
						case BOOLEAN:
							cell.setCellValue(Boolean.parseBoolean(dataTableCell.getValue().trim().toLowerCase()));
							break;
						case INTEGER:
							cell.setCellValue(new BigInteger(dataTableCell.getValue().trim()).doubleValue());
							break;
						case FLOAT:
							cell.setCellValue(new BigDecimal(dataTableCell.getValue().trim()).doubleValue());
							break;
						default:
							cell.setCellValue(dataTableCell.getValue());
					}
			}
		}
		
		if(dataTableRow.getComment() != null) {
			cell = newCell(row, width + 4);
			setCellStyleAndFont(cell, spreadsheetStyleConfig.getCommentCellStyle(), spreadsheetStyleConfig.getCommentFont());
			cell.setCellValue("#" + dataTableRow.getComment().getText());
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), 25));
		}
		
		if(maxColumn < width + 2)
			maxColumn = width + 2;
	}
	
	protected void writeTag(Sheet sheet, Tag tag) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "writeTag", sheet, tag);
		
		Row row = newRow(sheet);
		Cell cell = newCell(row, 1);
		
		if(tag.getComment() == null) {
			cell.setCellValue("@" + tag.getTag());
			setCellStyleAndFont(cell, spreadsheetStyleConfig.getTagCellStyle(), spreadsheetStyleConfig.getTagFont());
		}
		else {
			setCellStyle(cell, spreadsheetStyleConfig.getTagCellStyle());
			setRichText(cell, new String[] {"@" + tag.getTag(), "  # " + tag.getComment().getText()}, new FontConfig[] {spreadsheetStyleConfig.getTagFont(), spreadsheetStyleConfig.getCommentFont()});
		}
		
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), 25));
	}
	
	protected void writeComment(Sheet sheet, Comment comment) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "writeComment", sheet, comment);
		
		Row row = newRow(sheet);
		Cell cell = newCell(row, 0);
		setCellStyleAndFont(cell, spreadsheetStyleConfig.getCommentCellStyle(), spreadsheetStyleConfig.getCommentFont());
		cell.setCellValue("#" + comment.getText());
		
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), 25));
	}
	
	@SuppressWarnings("unchecked")
	protected Pair<String, Boolean>[] extractTextVariables(String text) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "extractTextVariables", text);
		
		Matcher matcher = VARIABLE_PATTERN.matcher(text);
		List<Pair<String, Boolean>> list = new ArrayList<Pair<String, Boolean>>();
		int position = 0;
		int start, end;
		while(matcher.find()) {
			start = matcher.start();
			end = matcher.end();
			if(start > position)
				list.add(new Pair<String, Boolean>(text.substring(position, start), false));
			list.add(new Pair<String, Boolean>(text.substring(start, end), true));
			position = end;
		}
		
		if(position < text.length())
			list.add(new Pair<String, Boolean>(text.substring(position), false));
		
		return list.toArray(new Pair[list.size()]);
	}

	protected void setCellStyleAndFont(Cell cell, CellStyleConfig styleConfig, FontConfig fontConfig) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "setCellStyleAndFont", cell, styleConfig, fontConfig);
		
		CellStyle style = fontAndCellStyles.get(new Pair<CellStyleConfig, FontConfig>(styleConfig, fontConfig));
		if(style == null) {
			style = getCellStyleAndFont(styleConfig, fontConfig);
			fontAndCellStyles.put(new Pair<CellStyleConfig, FontConfig>(styleConfig, fontConfig), style);
		}
		
		cell.setCellStyle(style); 
	}
	
	protected void setCellStyle(Cell cell, CellStyleConfig styleConfig) {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "setCellStyle", cell, styleConfig);
		
		CellStyle style = cellStyles.get(styleConfig);
		if(style == null) {
			style = getCellStyle(styleConfig);
			cellStyles.put(styleConfig, style);
		}
		
		cell.setCellStyle(style); 
	}
	
	protected void doWrite(Workbook workbook) throws IOException {
		LogHelper.trace(log, AbstractSpreadsheetGherkinWriter.class, "doWrite", workbook);
		
		workbook.write(outputStream);
		outputStream.close();
		
		log.debug("Wrote file: " + filename);
	}
	
	///// worksheet methods
	
	protected Sheet newSheet(Workbook workbook, String name) {
		return workbook.createSheet(name);
	}
	
	protected Row newRow(Sheet sheet) {
		return sheet.createRow(sheet.getLastRowNum() + 1);
	}
	
	protected Cell newCell(Row row, int column) {
		return row.createCell(column);
	}
	
	protected CellStyle getCellStyleAndFont(CellStyleConfig styleConfig, FontConfig fontConfig) {
		CellStyle style = fontAndCellStyles.get(new Pair<CellStyleConfig, FontConfig>(styleConfig, fontConfig));
		if(style == null) {
			style = createCellStyle(styleConfig);
			style.setFont(createFont(fontConfig));
			fontAndCellStyles.put(new Pair<CellStyleConfig, FontConfig>(styleConfig, fontConfig), style);
		}
		
		return style;
	}
	
	protected CellStyle getCellStyle(CellStyleConfig styleConfig) {
		CellStyle style = cellStyles.get(styleConfig);
		if(style == null) {
			style = createCellStyle(styleConfig);
			cellStyles.put(styleConfig, style);
		}
		
		return style;
	}

	protected CellStyle createCellStyle(CellStyleConfig styleConfig) {
		CellStyle style = workbook.createCellStyle();
		if(styleConfig != null)
			styleConfig.setTo(style);
		return style;
	}
	
	protected Font getFont(FontConfig fontConfig) {
		Font font = fonts.get(fontConfig);
		if(font == null) {
			font = createFont(fontConfig);
			fonts.put(fontConfig, font);
		}
		
		return font;
	}
	
	protected Font createFont(FontConfig fontConfig) {
		Font font = workbook.createFont();
		if(font != null && fontConfig != null)
			fontConfig.setTo(font);
		return font;
	}
	
	///// abstract methods
	
	protected abstract Workbook newWorkbook();
	protected abstract void setRichText(Cell cell, String[] texts, FontConfig[] fonts);

}