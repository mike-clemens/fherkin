package fherkin.io.impl;

import fherkin.io.impl.csv.CSVCell;
import fherkin.io.impl.csv.CSVWorkbook;
import fherkin.lang.GherkinKeywordType;
import fherkin.model.GherkinEntry;
import fherkin.model.datatable.DataTable;
import fherkin.model.datatable.DataTableCell;
import fherkin.model.datatable.DataTableRow;
import fherkin.model.entry.Background;
import fherkin.model.entry.Comment;
import fherkin.model.entry.Examples;
import fherkin.model.entry.Feature;
import fherkin.model.entry.Scenario;
import fherkin.model.entry.ScenarioOutline;
import fherkin.model.entry.Step;
import fherkin.style.CellStyleConfig;
import fherkin.style.FontConfig;
import fherkin.style.SpreadsheetStyleConfig;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  AbstractSpreadsheetGherkinWriter
 * 
 * @author Mike Clemens
 */
public class AbstractSpreadsheetGherkinWriterTest extends AbstractSpreadsheetGherkinWriter {
	
	private Log log = LogFactory.getLog(getClass());
	private DataTable dataTable;
	private ClassLoader classLoader;
	
	private List<String> cellStyles = new ArrayList<String>();
	private List<String> fonts = new ArrayList<String>();
	private List<String> texts = new ArrayList<String>();
	private List<Map<String, String>> richTexts = new ArrayList<Map<String, String>>();
	
	public AbstractSpreadsheetGherkinWriterTest() {
		classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
			classLoader = getClass().getClassLoader();
		
		// create a generic data table
		dataTable = new DataTable();
		DataTableCell cell;
		
		DataTableRow headings = new DataTableRow();
		headings.setRowNumber(0);
		headings.setDataTable(dataTable);
		dataTable.addRow(headings);
		
		cell = new DataTableCell();
		cell.setRow(headings);
		cell.setValue("String Value");
		headings.addCell(cell);
		
		cell = new DataTableCell();
		cell.setRow(headings);
		cell.setValue("Boolean Value");
		headings.addCell(cell);
		
		cell = new DataTableCell();
		cell.setRow(headings);
		cell.setValue("Integer Value");
		headings.addCell(cell);
		
		cell = new DataTableCell();
		cell.setRow(headings);
		cell.setValue("Float Value");
		headings.addCell(cell);
		
		DataTableRow row = new DataTableRow();
		row.setRowNumber(1);
		row.setDataTable(dataTable);
		dataTable.addRow(row);
		
		cell = new DataTableCell();
		cell.setRow(row);
		cell.setValue("abc");
		row.addCell(cell);
		
		cell = new DataTableCell();
		cell.setRow(row);
		cell.setValue("true");
		row.addCell(cell);
		
		cell = new DataTableCell();
		cell.setRow(row);
		cell.setValue("12345");
		row.addCell(cell);
		
		cell = new DataTableCell();
		cell.setRow(row);
		cell.setValue("123.45");
		row.addCell(cell);
		
		// create a mock spreadsheet style config instance
		spreadsheetStyleConfig = (SpreadsheetStyleConfig) Proxy.newProxyInstance(classLoader, new Class<?>[] {SpreadsheetStyleConfig.class}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if(method.getName().startsWith("get")) {
					if(CellStyleConfig.class.equals(method.getReturnType()))
						return new MockCellStyleConfig(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
					else
					if(FontConfig.class.equals(method.getReturnType()))
						return new MockFontConfig(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
					else
						return null;
				}
				else
					return null;
			}
		});
	}
	
	@Before
	public void before() {
		for(DataTableRow row : dataTable.getRows())
			row.setComment(null);
		
		cellStyles.clear();
		fonts.clear();
		texts.clear();
		richTexts.clear();
	}
	
	///// feature
	
	@Test
	public void testFeatureSingleLineNoComment() {
		Feature feature = new Feature();
		feature.setKeyword("Feature");
		feature.setText("This is a feature");
		doWrite(feature);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("featureCellStyle", cellStyles.get(0));
		Assert.assertEquals("featureTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(2, fonts.size());
		Assert.assertEquals("featureFont", fonts.get(0));
		Assert.assertEquals("featureTextFont", fonts.get(1));
		
		Assert.assertEquals(2, texts.size());
		Assert.assertEquals("Feature:", texts.get(0));
		Assert.assertEquals("This is a feature", texts.get(1));
	}
	
	@Test
	public void testFeatureSingleLineWithComment() {
		Comment comment = new Comment();
		comment.setText(" This is a comment");

		Feature feature = new Feature();
		feature.setKeyword("Feature");
		feature.setText("This is a feature");
		feature.setComment(comment);
		doWrite(feature);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("featureCellStyle", cellStyles.get(0));
		Assert.assertEquals("featureTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(1, fonts.size());
		Assert.assertEquals("featureFont", fonts.get(0));
		
		Assert.assertEquals(1, texts.size());
		Assert.assertEquals("Feature:", texts.get(0));
		
		Assert.assertEquals(1, richTexts.size());
		Assert.assertEquals(2, richTexts.get(0).size());
		Assert.assertEquals("featureTextFont", richTexts.get(0).get("This is a feature"));
		Assert.assertEquals("commentFont", richTexts.get(0).get("  # This is a comment"));
	}
	
	@Test
	public void testFeatureAdditionalLines() {
		Feature feature = new Feature();
		feature.setKeyword("Feature");
		feature.setText("This is a feature");
		feature.addAdditionalText("This is the second line");
		feature.addAdditionalText("And this is the third line");
		doWrite(feature);
		
		Assert.assertEquals(4, cellStyles.size());
		Assert.assertEquals("featureCellStyle", cellStyles.get(0));
		Assert.assertEquals("featureTextCellStyle", cellStyles.get(1));
		Assert.assertEquals("featureTextCellStyle", cellStyles.get(2));
		Assert.assertEquals("featureTextCellStyle", cellStyles.get(3));
		
		Assert.assertEquals(4, fonts.size());
		Assert.assertEquals("featureFont", fonts.get(0));
		Assert.assertEquals("featureTextFont", fonts.get(1));
		Assert.assertEquals("featureTextFont", fonts.get(2));
		Assert.assertEquals("featureTextFont", fonts.get(3));
		
		Assert.assertEquals(4, texts.size());
		Assert.assertEquals("Feature:", texts.get(0));
		Assert.assertEquals("This is a feature", texts.get(1));
		Assert.assertEquals("This is the second line", texts.get(2));
		Assert.assertEquals("And this is the third line", texts.get(3));
	}
	
	///// background/scenario/scenario outline
	
	@Test
	public void testBackgroundNoComment() {
		Background background = new Background();
		background.setKeyword("Background");
		background.setText("This is a background");
		doWrite(background);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("backgroundCellStyle", cellStyles.get(0));
		Assert.assertEquals("backgroundTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(2, fonts.size());
		Assert.assertEquals("backgroundFont", fonts.get(0));
		Assert.assertEquals("backgroundTextFont", fonts.get(1));
		
		Assert.assertEquals(2, texts.size());
		Assert.assertEquals("Background:", texts.get(0));
		Assert.assertEquals("This is a background", texts.get(1));
	}
	
	@Test
	public void testBackgroundWithComment() {
		Comment comment = new Comment();
		comment.setText(" This is a comment");

		Background background = new Background();
		background.setKeyword("Background");
		background.setText("This is a background");
		background.setComment(comment);
		doWrite(background);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("backgroundCellStyle", cellStyles.get(0));
		Assert.assertEquals("backgroundTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(1, fonts.size());
		Assert.assertEquals("backgroundFont", fonts.get(0));

		Assert.assertEquals(1, texts.size());
		Assert.assertEquals("Background:", texts.get(0));
		
		Assert.assertEquals(1, richTexts.size());
		Assert.assertEquals(2, richTexts.get(0).size());
		Assert.assertEquals("backgroundTextFont", richTexts.get(0).get("This is a background"));
		Assert.assertEquals("commentFont", richTexts.get(0).get("  # This is a comment"));
	}
	
	@Test
	public void testScenarioNoComment() {
		Scenario scenario = new Scenario();
		scenario.setKeyword("Scenario");
		scenario.setText("This is a scenario");
		doWrite(scenario);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("scenarioCellStyle", cellStyles.get(0));
		Assert.assertEquals("scenarioTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(2, fonts.size());
		Assert.assertEquals("scenarioFont", fonts.get(0));
		Assert.assertEquals("scenarioTextFont", fonts.get(1));
		
		Assert.assertEquals(2, texts.size());
		Assert.assertEquals("Scenario:", texts.get(0));
		Assert.assertEquals("This is a scenario", texts.get(1));
	}
	
	@Test
	public void testScenarioWithComment() {
		Comment comment = new Comment();
		comment.setText(" This is a comment");

		Scenario scenario = new Scenario();
		scenario.setKeyword("Scenario");
		scenario.setText("This is a scenario");
		scenario.setComment(comment);
		doWrite(scenario);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("scenarioCellStyle", cellStyles.get(0));
		Assert.assertEquals("scenarioTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(1, fonts.size());
		Assert.assertEquals("scenarioFont", fonts.get(0));

		Assert.assertEquals(1, texts.size());
		Assert.assertEquals("Scenario:", texts.get(0));
		
		Assert.assertEquals(1, richTexts.size());
		Assert.assertEquals(2, richTexts.get(0).size());
		Assert.assertEquals("scenarioTextFont", richTexts.get(0).get("This is a scenario"));
		Assert.assertEquals("commentFont", richTexts.get(0).get("  # This is a comment"));
	}
	
	@Test
	public void testScenarioOutlineNoComment() {
		ScenarioOutline outline = new ScenarioOutline();
		outline.setKeyword("Scenario Outline");
		outline.setText("This is a scenario outline");
		doWrite(outline);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("scenarioOutlineCellStyle", cellStyles.get(0));
		Assert.assertEquals("scenarioOutlineTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(2, fonts.size());
		Assert.assertEquals("scenarioOutlineFont", fonts.get(0));
		Assert.assertEquals("scenarioOutlineTextFont", fonts.get(1));
		
		Assert.assertEquals(2, texts.size());
		Assert.assertEquals("Scenario Outline:", texts.get(0));
		Assert.assertEquals("This is a scenario outline", texts.get(1));
	}
	
	@Test
	public void testScenarioOutlineWithComment() {
		Comment comment = new Comment();
		comment.setText(" This is a comment");

		ScenarioOutline outline = new ScenarioOutline();
		outline.setKeyword("Scenario Outline");
		outline.setText("This is a scenario outline");
		outline.setComment(comment);
		doWrite(outline);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("scenarioOutlineCellStyle", cellStyles.get(0));
		Assert.assertEquals("scenarioOutlineTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(1, fonts.size());
		Assert.assertEquals("scenarioOutlineFont", fonts.get(0));

		Assert.assertEquals(1, texts.size());
		Assert.assertEquals("Scenario Outline:", texts.get(0));
		
		Assert.assertEquals(1, richTexts.size());
		Assert.assertEquals(2, richTexts.get(0).size());
		Assert.assertEquals("scenarioOutlineTextFont", richTexts.get(0).get("This is a scenario outline"));
		Assert.assertEquals("commentFont", richTexts.get(0).get("  # This is a comment"));
	}
	
	///// given/when/then/and/but
	
	@Test
	public void testGivenNoVariablesNoComment() {
		Step step = new Step();
		step.setType(GherkinKeywordType.GIVEN);
		step.setKeyword("Given");
		step.setText("this is a given statement");
		doWrite(step);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("givenCellStyle", cellStyles.get(0));
		Assert.assertEquals("givenTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(2, fonts.size());
		Assert.assertEquals("givenFont", fonts.get(0));
		Assert.assertEquals("givenTextFont", fonts.get(1));
		
		Assert.assertEquals(2, texts.size());
		Assert.assertEquals("Given", texts.get(0));
		Assert.assertEquals("this is a given statement", texts.get(1));
	}
	
	@Test
	public void testGivenNoVariablesWithComment() {
		Comment comment = new Comment();
		comment.setText(" This is a comment");

		Step step = new Step();
		step.setType(GherkinKeywordType.GIVEN);
		step.setKeyword("Given");
		step.setText("this is a given statement");
		step.setComment(comment);
		doWrite(step);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("givenCellStyle", cellStyles.get(0));
		Assert.assertEquals("givenTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(1, fonts.size());
		Assert.assertEquals("givenFont", fonts.get(0));

		Assert.assertEquals(1, texts.size());
		Assert.assertEquals("Given", texts.get(0));
		
		Assert.assertEquals(1, richTexts.size());
		Assert.assertEquals(2, richTexts.get(0).size());
		Assert.assertEquals("givenTextFont", richTexts.get(0).get("this is a given statement"));
		Assert.assertEquals("commentFont", richTexts.get(0).get("  # This is a comment"));
	}
	
	@Test
	public void testGivenWithVariablesButNoComment() {
		Step step = new Step();
		step.setType(GherkinKeywordType.GIVEN);
		step.setKeyword("Given");
		step.setText("abc is <abc> and def is <def>");
		doWrite(step);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("givenCellStyle", cellStyles.get(0));
		Assert.assertEquals("givenTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(1, fonts.size());
		Assert.assertEquals("givenFont", fonts.get(0));

		Assert.assertEquals(1, texts.size());
		Assert.assertEquals("Given", texts.get(0));
		
		Assert.assertEquals(1, richTexts.size());
		Assert.assertEquals(4, richTexts.get(0).size());
		Assert.assertEquals("givenTextFont", richTexts.get(0).get("abc is "));
		Assert.assertEquals("givenTextVariableFont", richTexts.get(0).get("<abc>"));
		Assert.assertEquals("givenTextFont", richTexts.get(0).get(" and def is "));
		Assert.assertEquals("givenTextVariableFont", richTexts.get(0).get("<def>"));
	}
	
	@Test
	public void testGivenWithVariablesAndComment() {
		Comment comment = new Comment();
		comment.setText(" This is a comment");

		Step step = new Step();
		step.setType(GherkinKeywordType.GIVEN);
		step.setKeyword("Given");
		step.setText("abc is <abc> and def is <def>");
		step.setComment(comment);
		doWrite(step);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("givenCellStyle", cellStyles.get(0));
		Assert.assertEquals("givenTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(1, fonts.size());
		Assert.assertEquals("givenFont", fonts.get(0));

		Assert.assertEquals(1, texts.size());
		Assert.assertEquals("Given", texts.get(0));
		
		Assert.assertEquals(1, richTexts.size());
		Assert.assertEquals(5, richTexts.get(0).size());
		Assert.assertEquals("givenTextFont", richTexts.get(0).get("abc is "));
		Assert.assertEquals("givenTextVariableFont", richTexts.get(0).get("<abc>"));
		Assert.assertEquals("givenTextFont", richTexts.get(0).get(" and def is "));
		Assert.assertEquals("givenTextVariableFont", richTexts.get(0).get("<def>"));
		Assert.assertEquals("commentFont", richTexts.get(0).get("  # This is a comment"));
	}
	
	@Test
	public void testWhenNoVariablesNoComment() {
		Step step = new Step();
		step.setType(GherkinKeywordType.WHEN);
		step.setKeyword("When");
		step.setText("this is a when statement");
		doWrite(step);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("whenCellStyle", cellStyles.get(0));
		Assert.assertEquals("whenTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(2, fonts.size());
		Assert.assertEquals("whenFont", fonts.get(0));
		Assert.assertEquals("whenTextFont", fonts.get(1));
		
		Assert.assertEquals(2, texts.size());
		Assert.assertEquals("When", texts.get(0));
		Assert.assertEquals("this is a when statement", texts.get(1));
	}
	
	@Test
	public void testThenNoVariablesNoComment() {
		Step step = new Step();
		step.setType(GherkinKeywordType.THEN);
		step.setKeyword("Then");
		step.setText("this is a then statement");
		doWrite(step);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("thenCellStyle", cellStyles.get(0));
		Assert.assertEquals("thenTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(2, fonts.size());
		Assert.assertEquals("thenFont", fonts.get(0));
		Assert.assertEquals("thenTextFont", fonts.get(1));
		
		Assert.assertEquals(2, texts.size());
		Assert.assertEquals("Then", texts.get(0));
		Assert.assertEquals("this is a then statement", texts.get(1));
	}
	
	@Test
	public void testAndNoVariablesNoComment() {
		Step step = new Step();
		step.setType(GherkinKeywordType.AND);
		step.setKeyword("And");
		step.setText("this is an and statement");
		doWrite(step);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("andCellStyle", cellStyles.get(0));
		Assert.assertEquals("andTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(2, fonts.size());
		Assert.assertEquals("andFont", fonts.get(0));
		Assert.assertEquals("andTextFont", fonts.get(1));
		
		Assert.assertEquals(2, texts.size());
		Assert.assertEquals("And", texts.get(0));
		Assert.assertEquals("this is an and statement", texts.get(1));
	}
	
	@Test
	public void testButNoVariablesNoComment() {
		Step step = new Step();
		step.setType(GherkinKeywordType.BUT);
		step.setKeyword("But");
		step.setText("this is a but statement");
		doWrite(step);
		
		Assert.assertEquals(2, cellStyles.size());
		Assert.assertEquals("butCellStyle", cellStyles.get(0));
		Assert.assertEquals("butTextCellStyle", cellStyles.get(1));
		
		Assert.assertEquals(2, fonts.size());
		Assert.assertEquals("butFont", fonts.get(0));
		Assert.assertEquals("butTextFont", fonts.get(1));
		
		Assert.assertEquals(2, texts.size());
		Assert.assertEquals("But", texts.get(0));
		Assert.assertEquals("this is a but statement", texts.get(1));
	}
	
	///// examples
	
	@Test
	public void testExamplesNoComment() {
		Examples examples = new Examples();
		examples.setKeyword("Examples");
		doWrite(examples);
		
		Assert.assertEquals(1, cellStyles.size());
		Assert.assertEquals("examplesCellStyle", cellStyles.get(0));
		
		Assert.assertEquals(1, fonts.size());
		Assert.assertEquals("examplesFont", fonts.get(0));
		
		Assert.assertEquals(1, texts.size());
		Assert.assertEquals("Examples:", texts.get(0));
	}
	
	@Test
	public void testExamplesWithComment() {
		Comment comment = new Comment();
		comment.setText(" This is a comment");

		Examples examples = new Examples();
		examples.setKeyword("Examples");
		examples.setComment(comment);
		doWrite(examples);

		Assert.assertEquals(1, cellStyles.size());
		Assert.assertEquals("examplesCellStyle", cellStyles.get(0));
		
		Assert.assertEquals(1, richTexts.size());
		Assert.assertEquals(2, richTexts.get(0).size());
		Assert.assertEquals("examplesFont", richTexts.get(0).get("Examples:"));
		Assert.assertEquals("commentFont", richTexts.get(0).get("  # This is a comment"));
	}
	
	///// data table
	
	@Test
	public void testDataTableRowHeadingsWithoutComment() {
		doWrite(dataTable.getRows().get(0));
		
		Assert.assertEquals(4, cellStyles.size());
		Assert.assertEquals("dataTableHeadingCellStyleAlignLeft", cellStyles.get(0));
		Assert.assertEquals("dataTableHeadingCellStyleAlignLeft", cellStyles.get(1));
		Assert.assertEquals("dataTableHeadingCellStyleAlignRight", cellStyles.get(2));
		Assert.assertEquals("dataTableHeadingCellStyleAlignRight", cellStyles.get(3));
		
		Assert.assertEquals(4, fonts.size());
		Assert.assertEquals("dataTableHeadingFontAlignLeft", fonts.get(0));
		Assert.assertEquals("dataTableHeadingFontAlignLeft", fonts.get(1));
		Assert.assertEquals("dataTableHeadingFontAlignRight", fonts.get(2));
		Assert.assertEquals("dataTableHeadingFontAlignRight", fonts.get(3));
		
		Assert.assertEquals(4, texts.size());
		Assert.assertEquals("String Value", texts.get(0));
		Assert.assertEquals("Boolean Value", texts.get(1));
		Assert.assertEquals("Integer Value", texts.get(2));
		Assert.assertEquals("Float Value", texts.get(3));
	}
	
	@Test
	public void testDataTableRowHeadingsWithComment() {
		Comment comment = new Comment();
		comment.setText(" This is a comment");

		dataTable.getRows().get(0).setComment(comment);
		doWrite(dataTable.getRows().get(0));
		
		Assert.assertEquals(5, cellStyles.size());
		Assert.assertEquals("dataTableHeadingCellStyleAlignLeft", cellStyles.get(0));
		Assert.assertEquals("dataTableHeadingCellStyleAlignLeft", cellStyles.get(1));
		Assert.assertEquals("dataTableHeadingCellStyleAlignRight", cellStyles.get(2));
		Assert.assertEquals("dataTableHeadingCellStyleAlignRight", cellStyles.get(3));
		Assert.assertEquals("commentCellStyle", cellStyles.get(4));
		
		Assert.assertEquals(5, fonts.size());
		Assert.assertEquals("dataTableHeadingFontAlignLeft", fonts.get(0));
		Assert.assertEquals("dataTableHeadingFontAlignLeft", fonts.get(1));
		Assert.assertEquals("dataTableHeadingFontAlignRight", fonts.get(2));
		Assert.assertEquals("dataTableHeadingFontAlignRight", fonts.get(3));
		Assert.assertEquals("commentFont", fonts.get(4));
		
		Assert.assertEquals(5, texts.size());
		Assert.assertEquals("String Value", texts.get(0));
		Assert.assertEquals("Boolean Value", texts.get(1));
		Assert.assertEquals("Integer Value", texts.get(2));
		Assert.assertEquals("Float Value", texts.get(3));
		Assert.assertEquals("# This is a comment", texts.get(4));
	}
	
	@Test
	public void testDataTableRowWithoutComment() {
		doWrite(dataTable.getRows().get(1));
		
		Assert.assertEquals(4, cellStyles.size());
		Assert.assertEquals("dataTableRowCellStyleAlignLeft", cellStyles.get(0));
		Assert.assertEquals("dataTableRowCellStyleAlignLeft", cellStyles.get(1));
		Assert.assertEquals("dataTableRowCellStyleAlignRight", cellStyles.get(2));
		Assert.assertEquals("dataTableRowCellStyleAlignRight", cellStyles.get(3));
		
		Assert.assertEquals(4, fonts.size());
		Assert.assertEquals("dataTableRowFontAlignLeft", fonts.get(0));
		Assert.assertEquals("dataTableRowFontAlignLeft", fonts.get(1));
		Assert.assertEquals("dataTableRowFontAlignRight", fonts.get(2));
		Assert.assertEquals("dataTableRowFontAlignRight", fonts.get(3));
		
		Assert.assertEquals(4, texts.size());
		Assert.assertEquals("abc", texts.get(0));
		Assert.assertEquals("true", texts.get(1));
		Assert.assertEquals("12345.0", texts.get(2));
		Assert.assertEquals("123.45", texts.get(3));
	}
	
	@Test
	public void testDataTableRowWithComment() {
		Comment comment = new Comment();
		comment.setText(" This is a comment");

		dataTable.getRows().get(1).setComment(comment);
		doWrite(dataTable.getRows().get(1));
		
		Assert.assertEquals(5, cellStyles.size());
		Assert.assertEquals("dataTableRowCellStyleAlignLeft", cellStyles.get(0));
		Assert.assertEquals("dataTableRowCellStyleAlignLeft", cellStyles.get(1));
		Assert.assertEquals("dataTableRowCellStyleAlignRight", cellStyles.get(2));
		Assert.assertEquals("dataTableRowCellStyleAlignRight", cellStyles.get(3));
		Assert.assertEquals("commentCellStyle", cellStyles.get(4));
		
		Assert.assertEquals(5, fonts.size());
		Assert.assertEquals("dataTableRowFontAlignLeft", fonts.get(0));
		Assert.assertEquals("dataTableRowFontAlignLeft", fonts.get(1));
		Assert.assertEquals("dataTableRowFontAlignRight", fonts.get(2));
		Assert.assertEquals("dataTableRowFontAlignRight", fonts.get(3));
		Assert.assertEquals("commentFont", fonts.get(4));
		
		Assert.assertEquals(5, texts.size());
		Assert.assertEquals("abc", texts.get(0));
		Assert.assertEquals("true", texts.get(1));
		Assert.assertEquals("12345.0", texts.get(2));
		Assert.assertEquals("123.45", texts.get(3));
		Assert.assertEquals("# This is a comment", texts.get(4));
	}
	
	///// extractTextVariables
	
	@Test
	public void testExtractTextVariablesNoVariables() {
		Pair<String, Boolean>[] x = extractTextVariables("this is some regular text");
		Assert.assertEquals(1, x.length);
		Assert.assertEquals("this is some regular text", x[0].getFirst());
		Assert.assertFalse(x[0].getSecond());
	}
	
	@Test
	public void testExtractTextVariablesOnlyVariable() {
		Pair<String, Boolean>[] x = extractTextVariables("<abc>");
		Assert.assertEquals(1, x.length);
		Assert.assertEquals("<abc>", x[0].getFirst());
		Assert.assertTrue(x[0].getSecond());
	}
	
	@Test
	public void testExtractTextVariablesOnlyVariableWithSpace() {
		Pair<String, Boolean>[] x = extractTextVariables("<abc def>");
		Assert.assertEquals(1, x.length);
		Assert.assertEquals("<abc def>", x[0].getFirst());
		Assert.assertTrue(x[0].getSecond());
	}
	
	@Test
	public void testExtractTextVariablesGreaterThanAndLessThan() {
		Pair<String, Boolean>[] x = extractTextVariables("validate that < and > with spaces don't count");
		Assert.assertEquals(1, x.length);
		Assert.assertEquals("validate that < and > with spaces don't count", x[0].getFirst());
		Assert.assertFalse(x[0].getSecond());
	}
	
	@Test
	public void testExtractTextVariablesEndsWithVariable() {
		Pair<String, Boolean>[] x = extractTextVariables("abc is <abc> and def is <def> and ghi is <ghi>");
		Assert.assertEquals(6, x.length);
		
		Assert.assertEquals("abc is ", x[0].getFirst());
		Assert.assertFalse(x[0].getSecond());
		
		Assert.assertEquals("<abc>", x[1].getFirst());
		Assert.assertTrue(x[1].getSecond());
		
		Assert.assertEquals(" and def is ", x[2].getFirst());
		Assert.assertFalse(x[2].getSecond());
		
		Assert.assertEquals("<def>", x[3].getFirst());
		Assert.assertTrue(x[3].getSecond());
		
		Assert.assertEquals(" and ghi is ", x[4].getFirst());
		Assert.assertFalse(x[4].getSecond());
		
		Assert.assertEquals("<ghi>", x[5].getFirst());
		Assert.assertTrue(x[5].getSecond());
	}
	
	@Test
	public void testExtractTextVariablesStartsWithVariable() {
		Pair<String, Boolean>[] x = extractTextVariables("<abc> is abc and <def> is def and <ghi> is ghi");
		Assert.assertEquals(6, x.length);
		
		Assert.assertEquals("<abc>", x[0].getFirst());
		Assert.assertTrue(x[0].getSecond());
		
		Assert.assertEquals(" is abc and ", x[1].getFirst());
		Assert.assertFalse(x[1].getSecond());
		
		Assert.assertEquals("<def>", x[2].getFirst());
		Assert.assertTrue(x[2].getSecond());
		
		Assert.assertEquals(" is def and ", x[3].getFirst());
		Assert.assertFalse(x[3].getSecond());
		
		Assert.assertEquals("<ghi>", x[4].getFirst());
		Assert.assertTrue(x[4].getSecond());
		
		Assert.assertEquals(" is ghi", x[5].getFirst());
		Assert.assertFalse(x[5].getSecond());
	}
	
	@Test
	public void testExtractTextVariablesBackToBackVariables() {
		Pair<String, Boolean>[] x = extractTextVariables("abc is <abc><def> and ghi is something else");
		Assert.assertEquals(4, x.length);
		
		Assert.assertEquals("abc is ", x[0].getFirst());
		Assert.assertFalse(x[0].getSecond());
		
		Assert.assertEquals("<abc>", x[1].getFirst());
		Assert.assertTrue(x[1].getSecond());
		
		Assert.assertEquals("<def>", x[2].getFirst());
		Assert.assertTrue(x[2].getSecond());
		
		Assert.assertEquals(" and ghi is something else", x[3].getFirst());
		Assert.assertFalse(x[3].getSecond());
	}
	
	///// overridden/helper methods
	
	@Override
	protected void adjustColumnWidths(Sheet sheet) {
	}
		
	protected void doWrite(GherkinEntry entry) {
		List<GherkinEntry> list = new ArrayList<GherkinEntry>();
		list.add(entry);
		
		try {
			write(list);
		}
		catch(IOException e) {
			log.error(e.getClass().getSimpleName() + " caught:", e);
			Assert.fail("Unexpected IOException");
		}
	}

	@Override
	protected void setCellStyleAndFont(Cell cell, CellStyleConfig styleConfig, FontConfig fontConfig) {
		String styleName = ((MockCellStyleConfig) styleConfig).name;
		log.debug("Adding cell style: " + styleName);
		cellStyles.add(styleName);
		
		String fontName = ((MockFontConfig) fontConfig).name;
		log.debug("Adding font: " + fontName);
		fonts.add(fontName);
	}

	@Override
	protected void setCellStyle(Cell cell, CellStyleConfig styleConfig) {
		String styleName = ((MockCellStyleConfig) styleConfig).name;
		log.debug("Adding cell style: " + styleName);
		cellStyles.add(styleName);
	}

	@Override
	protected void doWrite(Workbook workbook) throws IOException {
	}

	@Override
	protected Workbook newWorkbook() {
		return new CSVWorkbook();
	}

	@Override
	protected Cell newCell(Row row, int column) {
		return new CSVCell(row, column) {
			@Override
			public void setCellValue(String value) {
				doSetCellValue(value);
			}
			
			@Override
			public void setCellValue(boolean value) {
				doSetCellValue(value);
			}
			
			@Override
			public void setCellValue(double value) {
				doSetCellValue(value);
			}
			
			protected void doSetCellValue(Object value) {
				log.debug("Adding text: " + value);
				texts.add(value.toString());
			}
		};
	}

	@Override
	protected CellStyle getCellStyleAndFont(CellStyleConfig styleConfig, FontConfig fontConfig) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected CellStyle getCellStyle(CellStyleConfig style) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void setRichText(Cell cell, String[] texts, FontConfig[] fonts) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		String fontName;
		for(int i = 0; i < texts.length; i++) {
			fontName = ((MockFontConfig) fonts[i]).name;
			log.debug("Adding rich text: " + fontName + ": " + texts[i]);
			map.put(texts[i], fontName);
		}
		
		richTexts.add(map);
	}

	class MockCellStyleConfig extends CellStyleConfig {
		public String name;
		
		public MockCellStyleConfig(String name) {
			this.name = name;
		}
		
		@Override
		public boolean equals(Object o) {
			return name.equals(((MockCellStyleConfig) o).name);
		}
		
		@Override
		public int hashCode() {
			return name.hashCode();
		}
	}
	
	class MockFontConfig extends FontConfig {
		public String name;
		
		public MockFontConfig(String name) {
			this.name = name;
		}
		
		@Override
		public boolean equals(Object o) {
			return name.equals(((MockCellStyleConfig) o).name);
		}
		
		@Override
		public int hashCode() {
			return name.hashCode();
		}
	}

}