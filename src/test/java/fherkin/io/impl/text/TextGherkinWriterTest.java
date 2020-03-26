package fherkin.io.impl.text;

import fherkin.model.GherkinEntry;
import fherkin.model.datatable.DataTable;
import fherkin.model.datatable.DataTableCell;
import fherkin.model.datatable.DataTableRow;
import fherkin.model.datatable.HasDataTable;
import fherkin.model.entry.Background;
import fherkin.model.entry.Comment;
import fherkin.model.entry.Examples;
import fherkin.model.entry.Feature;
import fherkin.model.entry.Scenario;
import fherkin.model.entry.ScenarioOutline;
import fherkin.model.entry.Step;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  TextGherkinWriter
 * 
 * @author Mike Clemens
 */
public class TextGherkinWriterTest extends TextGherkinWriter {
	
	private Log log = LogFactory.getLog(getClass());
	private StringWriter stringWriter;
	private Scenario stepScenario;
	
	@Before
	public void before() {
		stringWriter = new StringWriter();
		out = new PrintWriter(stringWriter);
		
		stepScenario = new Scenario();
		Step step;
		for(String keyword : new String[] {"Given", "And", "When", "But", "Then"}) {
			step = new Step();
			step.setKeyword(keyword);
			step.setText("this is a " + keyword + " statement");
			step.setScenario(stepScenario);
			stepScenario.addStep(step);
		}
	}
	
	///// feature
	
	@Test
	public void testWriteFeatureSingleLineNoComment() {
		Feature feature = new Feature();
		feature.setKeyword("Feature");
		feature.setText("This is a feature");
		
		String[] output = doWrite(feature);
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("Feature: This is a feature", output[0]);
	}
	
	@Test
	public void testWriteFeatureSingleLineWithComment() {
		Feature feature = new Feature();
		feature.setKeyword("Feature");
		feature.setText("This is a feature");
		
		Comment comment = new Comment();
		comment.setText(" This is a comment");
		feature.setComment(comment);
		
		String[] output = doWrite(feature);
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("Feature: This is a feature  # This is a comment", output[0]);
	}
	
	@Test
	public void testWriteFeatureMultiLineNoComment() {
		Feature feature = new Feature();
		feature.setKeyword("Feature");
		feature.setText("This is a feature");
		feature.addAdditionalText("This is the second line");
		feature.addAdditionalText("And this is the third line");
		
		String[] output = doWrite(feature);
		Assert.assertEquals(3, output.length);
		Assert.assertEquals("Feature: This is a feature", output[0]);
		Assert.assertEquals("         This is the second line", output[1]);
		Assert.assertEquals("         And this is the third line", output[2]);
	}
	
	@Test
	public void testWriteFeatureMultiLineWithComment() {
		Feature feature = new Feature();
		feature.setKeyword("Feature");
		feature.setText("This is a feature");
		feature.addAdditionalText("This is the second line");
		feature.addAdditionalText("And this is the third line");
		
		Comment comment = new Comment();
		comment.setText(" This is a comment");
		feature.setComment(comment);
		
		String[] output = doWrite(feature);
		Assert.assertEquals(3, output.length);
		Assert.assertEquals("Feature: This is a feature  # This is a comment", output[0]);
		Assert.assertEquals("         This is the second line", output[1]);
		Assert.assertEquals("         And this is the third line", output[2]);
	}
	
	///// background/scenario/scenario outline
	
	@Test
	public void testWriteBackground() {
		Background background = new Background();
		background.setKeyword("Background");
		background.setText("This is a background");
		
		String[] output = doWrite(background);
		Assert.assertEquals(2, output.length);
		Assert.assertEquals("", output[0]);
		Assert.assertEquals("  Background: This is a background", output[1]);
	}
	
	@Test
	public void testWriteScenario() {
		Scenario scenario = new Scenario();
		scenario.setKeyword("Scenario");
		scenario.setText("This is a scenario");
		
		String[] output = doWrite(scenario);
		Assert.assertEquals(2, output.length);
		Assert.assertEquals("", output[0]);
		Assert.assertEquals("  Scenario: This is a scenario", output[1]);
	}
	
	@Test
	public void testWriteScenarioOutline() {
		ScenarioOutline outline = new ScenarioOutline();
		outline.setKeyword("Scenario Outline");
		outline.setText("This is a scenario outline");
		
		String[] output = doWrite(outline);
		Assert.assertEquals(2, output.length);
		Assert.assertEquals("", output[0]);
		Assert.assertEquals("  Scenario Outline: This is a scenario outline", output[1]);
	}
	
	@Test
	public void testWriteScenarioWithComment() {
		Scenario scenario = new Scenario();
		scenario.setKeyword("Scenario");
		scenario.setText("This is a scenario");
		
		Comment comment = new Comment();
		comment.setText(" This is a comment");
		scenario.setComment(comment);
		
		String[] output = doWrite(scenario);
		Assert.assertEquals(2, output.length);
		Assert.assertEquals("", output[0]);
		Assert.assertEquals("  Scenario: This is a scenario  # This is a comment", output[1]);
	}
	
	///// steps
	
	@Test
	public void testWriteStepGiven() {
		String[] output = doWrite(stepScenario.getSteps().get(0));
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("    Given this is a Given statement", output[0]);
	}
	
	@Test
	public void testWriteStepWhen() {
		String[] output = doWrite(stepScenario.getSteps().get(2));
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("     When this is a When statement", output[0]);
	}
	
	@Test
	public void testWriteStepThen() {
		String[] output = doWrite(stepScenario.getSteps().get(4));
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("     Then this is a Then statement", output[0]);
	}
	
	@Test
	public void testWriteStepAnd() {
		String[] output = doWrite(stepScenario.getSteps().get(1));
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("      And this is a And statement", output[0]);
	}
	
	@Test
	public void testWriteStepBut() {
		String[] output = doWrite(stepScenario.getSteps().get(3));
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("      But this is a But statement", output[0]);
	}
	
	@Test
	public void testWriteStepWithComment() {
		Step step = stepScenario.getSteps().get(0);
		Comment comment = new Comment();
		comment.setText(" This is a comment");
		step.setComment(comment);
		
		String[] output = doWrite(step);
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("    Given this is a Given statement  # This is a comment", output[0]);
	}
	
	///// examples
	
	@Test
	public void testExamples() {
		Examples examples = new Examples();
		examples.setKeyword("Examples");
		
		String[] output = doWrite(examples);
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("    Examples:", output[0]);
	}
	
	@Test
	public void testExamplesWithComment() {
		Examples examples = new Examples();
		examples.setKeyword("Examples");
		
		Comment comment = new Comment();
		comment.setText(" This is a comment");
		examples.setComment(comment);
		
		writeExamples(examples);
		String[] output = getOutput();
		Assert.assertEquals(1, output.length);
		Assert.assertEquals("    Examples:  # This is a comment", output[0]);
	}
	
	///// data table
	
	@Test
	public void testDataTableUnderExamples() {
		Examples examples = new Examples();
		String[] output = doWriteDataTable(examples, new String[][] {
			new String[] {"a", "b", "c"},
			new String[] {"1", "2", "3"},
			new String[] {"2", "3", "5"},
			new String[] {"3", "5", "8"}
		});
		
		Assert.assertEquals(4, output.length);
		Assert.assertEquals("      | a | b | c |", output[0]);
		Assert.assertEquals("      | 1 | 2 | 3 |", output[1]);
		Assert.assertEquals("      | 2 | 3 | 5 |", output[2]);
		Assert.assertEquals("      | 3 | 5 | 8 |", output[3]);
	}
	
	@Test
	public void testDataTableUnderStep() {
		Step step = stepScenario.getSteps().get(1);
		String[] output = doWriteDataTable(step, new String[][] {
			new String[] {"a", "b", "c"},
			new String[] {"1", "2", "3"},
			new String[] {"2", "3", "5"},
			new String[] {"3", "5", "8"}
		});
		
		Assert.assertEquals(4, output.length);
		Assert.assertEquals("          | a | b | c |", output[0]);
		Assert.assertEquals("          | 1 | 2 | 3 |", output[1]);
		Assert.assertEquals("          | 2 | 3 | 5 |", output[2]);
		Assert.assertEquals("          | 3 | 5 | 8 |", output[3]);
	}
	
	@Test
	public void testDataTableConsistentColumnWidths() {
		Examples examples = new Examples();
		String[] output = doWriteDataTable(examples, new String[][] {
			new String[] {"Column A", "Column B",         "Column C"},
			new String[] {"abc",      "abcdefghijklmnop", "abc"},
			new String[] {"d",        "",                 "abcdefghijklmnopqrstuvwxyz"},
			new String[] {"defghij"}
		});
		
		Assert.assertEquals(4, output.length);
		Assert.assertEquals("      | Column A | Column B         | Column C                   |", output[0]);
		Assert.assertEquals("      | abc      | abcdefghijklmnop | abc                        |", output[1]);
		Assert.assertEquals("      | d        |                  | abcdefghijklmnopqrstuvwxyz |", output[2]);
		Assert.assertEquals("      | defghij  |                  |                            |", output[3]);
	}
	
	@Test
	public void testDataTableAlignment() {
		Examples examples = new Examples();
		String[] output = doWriteDataTable(examples, new String[][] {
			new String[] {"String", "Boolean", "Integer", "Floating Point"},
			new String[] {"abc",    "true",    "123",     "123.45"},
			new String[] {"def",    "false",   "345",     "345.67"},
			new String[] {"ghi",    "TRUE",    "567",     "567.89"},
		});
		
		Assert.assertEquals(4, output.length);
		Assert.assertEquals("      | String | Boolean | Integer | Floating Point |", output[0]);
		Assert.assertEquals("      | abc    | true    |     123 |         123.45 |", output[1]);
		Assert.assertEquals("      | def    | false   |     345 |         345.67 |", output[2]);
		Assert.assertEquals("      | ghi    | TRUE    |     567 |         567.89 |", output[3]);
	}
	
	@Test
	public void testDataTableComment() {
		Examples examples = new Examples();
		String[] output = doWriteDataTable(examples, new String[][] {
			new String[] {"a", "b", "c"},
			new String[] {"1", "2", "3"},
			new String[] {"2", "3", "5"},
			new String[] {"3", "5", "8"}
		}, new String[] {
				" heading comment",
				null,
				" row comment"
		});
		
		Assert.assertEquals(4, output.length);
		Assert.assertEquals("      | a | b | c |  # heading comment", output[0]);
		Assert.assertEquals("      | 1 | 2 | 3 |", output[1]);
		Assert.assertEquals("      | 2 | 3 | 5 |  # row comment", output[2]);
		Assert.assertEquals("      | 3 | 5 | 8 |", output[3]);
	}
	
	///// helper methods
	
	protected String[] doWrite(GherkinEntry entry) {
		List<GherkinEntry> entries = new ArrayList<GherkinEntry>();
		entries.add(entry);
		write(entries);
		
		return getOutput();
	}

	protected String[] doWriteDataTable(HasDataTable owner, String[][] values) {
		return doWriteDataTable(owner, values, null);
	}

	protected String[] doWriteDataTable(HasDataTable owner, String[][] values, String[] comments) {
		List<GherkinEntry> entries = new ArrayList<GherkinEntry>();
		
		DataTable dataTable = new DataTable();
		dataTable.setOwner(owner);
		owner.setDataTable(dataTable);
		
		List<DataTableRow> rows = new ArrayList<DataTableRow>();
		List<DataTableCell> cells;
		DataTableRow row;
		DataTableCell cell;
		Comment comment;
		int i = -1;
		for(String[] cellValues : values) {
			i++;
			cells = new ArrayList<DataTableCell>();
			row = new DataTableRow();
			row.setDataTable(dataTable);
			row.setCells(cells);
			rows.add(row);
			entries.add(row);
			
			if(comments != null && comments.length > i && comments[i] != null) {
				comment = new Comment();
				comment.setText(comments[i]);
				row.setComment(comment);
			}
			
			for(String cellValue : cellValues) {
				cell = new DataTableCell();
				cell.setRow(row);
				cell.setValue(cellValue);
				cells.add(cell);
			}
		}
		
		dataTable.setRows(rows);
		
		write(entries);
		return getOutput();
	}

	protected String[] getOutput() {
		out.close();
		
		String[] output = stringWriter.toString().replaceAll("\\r", "").split("\\n");
		log.debug("Wrote " + output.length + " lines:");
		for(int i = 0; i < output.length; i++)
			log.debug(i + ": " + output[i]);
		
		return output;
	}

}