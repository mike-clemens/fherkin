package fherkin.tests;

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
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * Base class for test classes for implementations of GherkinParser.
 * 
 * @author Mike Clemens
 */
public abstract class AbstractParseTest {
	
	@Test
	public void testParseScenario() throws Exception {
		List<GherkinEntry> entries = doParse("scenario");
		Iterator<GherkinEntry> iterator = entries.iterator();
		Step step;
		Comment comment;
		
		// contents
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals(" Feature file containing a simple scenario", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		Feature feature = (Feature) iterator.next();
		Assert.assertEquals("Feature", feature.getKeyword());
		Assert.assertEquals("Scenario feature file", feature.getText());
		
		Background background = (Background) iterator.next();
		Assert.assertEquals("Background", background.getKeyword());
		Assert.assertEquals("Buy some cucumbers", background.getText());
		Assert.assertSame(feature, background.getFeature());
		
		step = (Step) iterator.next();
		Assert.assertEquals("*", step.getKeyword());
		Assert.assertEquals("Go to the store", step.getText());
		Assert.assertSame(background, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("*", step.getKeyword());
		Assert.assertEquals("Buy some cucumbers", step.getText());
		Assert.assertSame(background, step.getScenario());
		
		Scenario scenario = (Scenario) iterator.next();
		Assert.assertEquals("Scenario", scenario.getKeyword());
		Assert.assertEquals("Simple scenario", scenario.getText());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Given", step.getKeyword());
		Assert.assertEquals("I have 5 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("But", step.getKeyword());
		Assert.assertEquals("I somehow acquire another 5 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("When", step.getKeyword());
		Assert.assertEquals("I eat 3 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("And", step.getKeyword());
		Assert.assertEquals("I throw away another 3 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Then", step.getKeyword());
		Assert.assertEquals("I will have 4 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		Assert.assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testParseScenarioOutline() throws Exception {
		List<GherkinEntry> entries = doParse("scenarioOutline");
		Iterator<GherkinEntry> iterator = entries.iterator();
		Step step;
		Comment comment;
		
		// contents
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals(" Feature file containing a simple scenario outline", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		Feature feature = (Feature) iterator.next();
		Assert.assertEquals("Feature", feature.getKeyword());
		Assert.assertEquals("Scenario outline feature file", feature.getText());
		
		Background background = (Background) iterator.next();
		Assert.assertEquals("Background", background.getKeyword());
		Assert.assertEquals("Buy some cucumbers", background.getText());
		Assert.assertSame(feature, background.getFeature());
		
		step = (Step) iterator.next();
		Assert.assertEquals("*", step.getKeyword());
		Assert.assertEquals("Go to the store", step.getText());
		Assert.assertSame(background, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("*", step.getKeyword());
		Assert.assertEquals("Buy some cucumbers", step.getText());
		Assert.assertSame(background, step.getScenario());
		
		ScenarioOutline outline = (ScenarioOutline) iterator.next();
		Assert.assertEquals("Scenario Outline", outline.getKeyword());
		Assert.assertEquals("Simple scenario outline", outline.getText());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Given", step.getKeyword());
		Assert.assertEquals("I have <start> cucumbers", step.getText());
		Assert.assertSame(outline, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("When", step.getKeyword());
		Assert.assertEquals("I eat <eat> cucumbers", step.getText());
		Assert.assertSame(outline, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Then", step.getKeyword());
		Assert.assertEquals("I will have <remaining> cucumbers", step.getText());
		Assert.assertSame(outline, step.getScenario());
		
		String[][] expected = new String[][] {
				new String[] {"start", "eat", "remaining"}, 
				new String[] {"5",     "3",   "2"        }, 
				new String[] {"3",     "2",   "1"        }, 
				new String[] {"2",     "1",   "1"        } 
		};
		
		Examples examples = (Examples) iterator.next();
		Assert.assertSame(outline, examples.getScenarioOutline());
		
		DataTable dataTable;
		DataTableRow row;
		DataTableCell cell;
		for(int i = 0; i < 4; i++) {
			row = (DataTableRow) iterator.next();
			dataTable = row.getDataTable();
			Assert.assertSame(examples, dataTable.getOwner());
			Assert.assertEquals(3, row.getCells().size());
			
			for(int j = 0; j < row.getCells().size(); j++) {
				cell = row.getCells().get(j);
				Assert.assertSame(row, cell.getRow());
				Assert.assertEquals(expected[i][j], cell.getValue());
			}
		}
		
		Assert.assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testParseStepTable() throws Exception {
		List<GherkinEntry> entries = doParse("stepTable");
		Iterator<GherkinEntry> iterator = entries.iterator();
		Step step;
		Comment comment;
		DataTable dataTable;
		DataTableRow row;
		DataTableCell cell;
		String[][] expected;
		
		// contents
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals(" Feature file containing scenarios with data tables under steps", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals(" Note: This is not a complete gherkin recipe and will likely be terrible if you attempt", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		Feature feature = (Feature) iterator.next();
		Assert.assertEquals("Feature", feature.getKeyword());
		Assert.assertEquals("Step table feature file", feature.getText());
		
		Scenario scenario = (Scenario) iterator.next();
		Assert.assertEquals("Scenario", scenario.getKeyword());
		Assert.assertEquals("Steps with data tables", scenario.getText());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Given", step.getKeyword());
		Assert.assertEquals("I have the following ingredients:", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		expected = new String[][] {
				new String[] {"Item",     "Count", "Unit"  },
				new String[] {"Cucumber", "7",     "Pieces"},
				new String[] {"Salt",     "0.5",   "Cups"  },
				new String[] {"Sugar",    "8",     "Cups"  },
				new String[] {"Vinegar",  "6",     "Cups"  }
		};
		
		for(int i = 0; i < expected.length; i++) {
			row = (DataTableRow) iterator.next();
			dataTable = row.getDataTable();
			Assert.assertSame(step, dataTable.getOwner());
			
			for(int j = 0; j < expected[i].length; j++) {
				cell = row.getCells().get(j);
				Assert.assertEquals(expected[i][j], cell.getValue());
			}
		}

		step = (Step) iterator.next();
		Assert.assertEquals("When", step.getKeyword());
		Assert.assertEquals("I boil these ingredients together:", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		expected = new String[][] {
				new String[] {"Item"    },
				new String[] {"Salt"    },
				new String[] {"Sugar"   },
				new String[] {"Vinegar" }
		};
		
		for(int i = 0; i < expected.length; i++) {
			row = (DataTableRow) iterator.next();
			dataTable = row.getDataTable();
			Assert.assertSame(step, dataTable.getOwner());
			
			for(int j = 0; j < expected[i].length; j++) {
				cell = row.getCells().get(j);
				Assert.assertEquals(expected[i][j], cell.getValue());
			}
		}
		
		step = (Step) iterator.next();
		Assert.assertEquals("And", step.getKeyword());
		Assert.assertEquals("I pour the result over the cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		Assert.assertNull(step.getDataTable());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Then", step.getKeyword());
		Assert.assertEquals("I may have something resembling a gherkin", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		Assert.assertNull(step.getDataTable());
		
		Assert.assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testParseMultiLineFeature() throws Exception {
		List<GherkinEntry> entries = doParse("multiLineFeature");
		Iterator<GherkinEntry> iterator = entries.iterator();
		Step step;
		Comment comment;
		
		// contents
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals(" Feature file containing a feature description split across multiple lines", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		Feature feature = (Feature) iterator.next();
		Assert.assertEquals("Feature", feature.getKeyword());
		Assert.assertEquals("Long feature description", feature.getText());
		Assert.assertArrayEquals(new String[] {
				"The feature section can be multiple lines, and all lines prior to one",
				"of the gherkin keywords are considered to be part of the feature",
				"description."
		}, feature.getAdditionalText().toArray());
		
		Scenario scenario = (Scenario) iterator.next();
		Assert.assertEquals("Scenario", scenario.getKeyword());
		Assert.assertEquals("Eat some cucumbers", scenario.getText());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Given", step.getKeyword());
		Assert.assertEquals("I have 5 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("When", step.getKeyword());
		Assert.assertEquals("I eat 3 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Then", step.getKeyword());
		Assert.assertEquals("I will have 2 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		Assert.assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testParseTags() throws Exception {
		List<GherkinEntry> entries = doParse("tags");
		Iterator<GherkinEntry> iterator = entries.iterator();
		Step step;
		Comment comment;
		
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals(" Feature file containing tags", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		Feature feature = (Feature) iterator.next();
		Assert.assertEquals("Feature", feature.getKeyword());
		Assert.assertEquals("Tagged feature", feature.getText());
		
		Assert.assertEquals(3, feature.getTags().size());
		Assert.assertEquals("featureTag1", feature.getTags().get(0).getTag());
		Assert.assertEquals("featureTag2", feature.getTags().get(1).getTag());
		Assert.assertEquals("featureTag3", feature.getTags().get(2).getTag());
		
		Scenario scenario = (Scenario) iterator.next();
		Assert.assertEquals("Scenario", scenario.getKeyword());
		Assert.assertEquals("Tagged scenario", scenario.getText());

		Assert.assertEquals(3, scenario.getTags().size());
		Assert.assertEquals("scenarioTag1", scenario.getTags().get(0).getTag());
		Assert.assertEquals("scenarioTag2", scenario.getTags().get(1).getTag());
		Assert.assertEquals("scenarioTag3", scenario.getTags().get(2).getTag());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Given", step.getKeyword());
		Assert.assertEquals("I have 5 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("When", step.getKeyword());
		Assert.assertEquals("I eat 3 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Then", step.getKeyword());
		Assert.assertEquals("I will have 2 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		ScenarioOutline outline = (ScenarioOutline) iterator.next();
		Assert.assertEquals("Scenario Outline", outline.getKeyword());
		Assert.assertEquals("Tagged scenario outline", outline.getText());

		Assert.assertEquals(3, outline.getTags().size());
		Assert.assertEquals("scenarioOutlineTag1", outline.getTags().get(0).getTag());
		Assert.assertEquals("scenarioOutlineTag2", outline.getTags().get(1).getTag());
		Assert.assertEquals("scenarioOutlineTag3", outline.getTags().get(2).getTag());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Given", step.getKeyword());
		Assert.assertEquals("I have <start> cucumbers", step.getText());
		Assert.assertSame(outline, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("When", step.getKeyword());
		Assert.assertEquals("I eat <eat> cucumbers", step.getText());
		Assert.assertSame(outline, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Then", step.getKeyword());
		Assert.assertEquals("I will have <remaining> cucumbers", step.getText());
		Assert.assertSame(outline, step.getScenario());
		
		String[][] expected = new String[][] {
				new String[] {"start", "eat", "remaining"}, 
				new String[] {"5",     "3",   "2"        }, 
				new String[] {"3",     "2",   "1"        }, 
				new String[] {"2",     "1",   "1"        } 
		};
		
		Examples examples = (Examples) iterator.next();
		Assert.assertSame(outline, examples.getScenarioOutline());
		
		DataTable dataTable;
		DataTableRow row;
		DataTableCell cell;
		for(int i = 0; i < 4; i++) {
			row = (DataTableRow) iterator.next();
			dataTable = row.getDataTable();
			Assert.assertSame(examples, dataTable.getOwner());
			Assert.assertEquals(3, row.getCells().size());
			
			for(int j = 0; j < row.getCells().size(); j++) {
				cell = row.getCells().get(j);
				Assert.assertSame(row, cell.getRow());
				Assert.assertEquals(expected[i][j], cell.getValue());
			}
		}
		
		Assert.assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testParseNonEnglish() throws Exception {
		List<GherkinEntry> entries = doParse("nonEnglish");
		Iterator<GherkinEntry> iterator = entries.iterator();
		Scenario scenario;
		Step step;
		Comment comment;
		
		// contents
		comment = (Comment) iterator.next();
		Assert.assertEquals(" language: fr", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals(" Feature file in a non-English language", comment.getText());
		
		comment = (Comment) iterator.next();
		Assert.assertEquals("", comment.getText());
		
		Feature feature = (Feature) iterator.next();
		Assert.assertEquals("Fonctionnalité", feature.getKeyword());
		Assert.assertEquals("Fichier de fonctionnalités en français", feature.getText());
		
		scenario = (Scenario) iterator.next();
		Assert.assertEquals("Scénario", scenario.getKeyword());
		Assert.assertEquals("Scénario en français", scenario.getText());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Étant donné", step.getKeyword());
		Assert.assertEquals("que j'ai 5 concombres", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Quand", step.getKeyword());
		Assert.assertEquals("je mange 3 concombres", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		step = (Step) iterator.next();
		Assert.assertEquals("Alors", step.getKeyword());
		Assert.assertEquals("j'aurai 2 concombres", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		Assert.assertFalse(iterator.hasNext());
	}
	
	protected abstract List<GherkinEntry> doParse(String name) throws Exception;

}