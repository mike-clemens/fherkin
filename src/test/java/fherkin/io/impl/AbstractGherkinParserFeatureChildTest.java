package fherkin.io.impl;

import fherkin.io.GherkinSyntaxException;
import fherkin.lang.GherkinKeywordType;
import fherkin.model.entry.Background;
import fherkin.model.entry.Feature;
import fherkin.model.entry.Scenario;
import fherkin.model.entry.ScenarioOutline;
import fherkin.model.entry.Step;
import fherkin.model.entry.Tag;
import fherkin.model.location.Location;
import java.util.ArrayList;
import java.util.SortedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  AbstractGherkinParser feature children (ie.  background,
 * scenario, scenario outline)
 * 
 * @author Mike Clemens
 */
public class AbstractGherkinParserFeatureChildTest extends BaseAbstractGherkinParserTestCase {

	private Log log = LogFactory.getLog(getClass());
	
	@Before
	public void before() {
		stack.clear();
		entries.clear();
	}
	
	@Test
	public void testFeatureChildNoParent() {
		try {
			processFeatureChild("Scenario", GherkinKeywordType.SCENARIO, "    ", "Scenario", ":", " ", "Desc");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testFeatureChildNoFeatureParent() {
		stack.push(new Scenario());
		stack.push(new Step());
		try {
			processFeatureChild("Scenario", GherkinKeywordType.SCENARIO, "    ", "Scenario", ":", " ", "Desc");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testFeatureChildNoColon() {
		stack.push(new Feature());
		try {
			processFeatureChild("Scenario", GherkinKeywordType.SCENARIO, "    ", "Scenario", " ", "Desc");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testBackground() {
		Feature feature = new Feature();
		stack.push(feature);
		processFeatureChild("Background", GherkinKeywordType.BACKGROUND, "    ", "Background", ":", " ", "Test", " ", "background");
		
		Background background = (Background) stack.peek();
		Assert.assertEquals("Background", background.getKeyword());
		Assert.assertEquals("Test background", background.getText());
		Assert.assertSame(feature, background.getFeature());
		Assert.assertSame(background, feature.getBackground());
	}
	
	@Test
	public void testMultipleBackgrounds() {
		Feature feature = new Feature();
		stack.push(feature);
		processFeatureChild("Background", GherkinKeywordType.BACKGROUND, "    ", "Background", ":", " ", "Test", " ", "background");
		
		try {
			processFeatureChild("Background", GherkinKeywordType.BACKGROUND, "    ", "Background", ":", " ", "Second", " ", "background");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testScenario() {
		Feature feature = new Feature();
		stack.push(feature);
		processFeatureChild("Scenario", GherkinKeywordType.SCENARIO, "    ", "Scenario", ":", " ", "Test", " ", "scenario");
		
		Scenario scenario = (Scenario) stack.peek();
		Assert.assertEquals("Scenario", scenario.getKeyword());
		Assert.assertEquals("Test scenario", scenario.getText());
		Assert.assertSame(feature, scenario.getFeature());
		
		Assert.assertEquals(1, feature.getScenarios().size());
		Assert.assertSame(scenario, feature.getScenarios().get(0));
	}
	
	@Test
	public void testScenarioOutline() {
		Feature feature = new Feature();
		stack.push(feature);
		processFeatureChild("Scenario Outline", GherkinKeywordType.SCENARIO_OUTLINE, "    ", "Scenario", " ", "Outline", ":", " ", "Test", " ", "scenario", " ", "outline");
		
		ScenarioOutline outline = (ScenarioOutline) stack.peek();
		Assert.assertEquals("Scenario Outline", outline.getKeyword());
		Assert.assertEquals("Test scenario outline", outline.getText());
		Assert.assertSame(feature, outline.getFeature());
		
		Assert.assertEquals(1, feature.getScenarios().size());
		Assert.assertSame(outline, feature.getScenarios().get(0));
	}
	
	@Test
	public void testRule() {
		// TODO
	}
	
	@Test
	public void testMultipleChildren() {
		Feature feature = new Feature();
		stack.push(feature);
		
		processFeatureChild("Background", GherkinKeywordType.BACKGROUND, "    ", "Background", ":", " ", "Test", " ", "background");
		processFeatureChild("Scenario", GherkinKeywordType.SCENARIO, "    ", "Scenario", ":", " ", "Test", " ", "scenario");
		processFeatureChild("Scenario Outline", GherkinKeywordType.SCENARIO_OUTLINE, "    ", "Scenario", " ", "Outline", ":", " ", "Test", " ", "scenario", " ", "outline");
		processFeatureChild("Scenario", GherkinKeywordType.SCENARIO, "    ", "Scenario", ":", " ", "Other", " ", "scenario");
		processFeatureChild("Scenario Outline", GherkinKeywordType.SCENARIO_OUTLINE, "    ", "Scenario", " ", "Outline", ":", " ", "Other", " ", "scenario", " ", "outline");
		
		Assert.assertNotNull(feature.getBackground());
		Assert.assertEquals(4, feature.getScenarios().size());
	}
	
	@Test
	public void testBackgroundAfterScenarios() {
		Feature feature = new Feature();
		stack.push(feature);
		processFeatureChild("Scenario", GherkinKeywordType.SCENARIO, "    ", "Scenario", ":", " ", "Test", " ", "scenario");
		
		try {
			processFeatureChild("Background", GherkinKeywordType.BACKGROUND, "    ", "Background", ":", " ", "Second", " ", "background");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testScenarioTags() {
		tags = new ArrayList<Tag>();
		tags.add(new Tag("abc"));
		tags.add(new Tag("def"));
		tags.add(new Tag("ghi"));
		
		Feature feature = new Feature();
		stack.push(feature);
		processFeatureChild("Scenario", GherkinKeywordType.SCENARIO, "    ", "Scenario", ":", " ", "Tagged", " ", "scenario");
		
		Scenario scenario = (Scenario) stack.peek();
		Assert.assertEquals("Scenario", scenario.getKeyword());
		Assert.assertEquals("Tagged scenario", scenario.getText());
		
		Assert.assertEquals(0, feature.getTags().size());
		Assert.assertEquals(3, scenario.getTags().size());
		Assert.assertEquals("abc", scenario.getTags().get(0).getTag());
		Assert.assertEquals("def", scenario.getTags().get(1).getTag());
		Assert.assertEquals("ghi", scenario.getTags().get(2).getTag());
		Assert.assertNull(tags);
	}
	
	@Test
	public void testScenarioOutlineTags() {
		tags = new ArrayList<Tag>();
		tags.add(new Tag("abc"));
		tags.add(new Tag("def"));
		tags.add(new Tag("ghi"));
		
		Feature feature = new Feature();
		stack.push(feature);
		processFeatureChild("Scenario Outline", GherkinKeywordType.SCENARIO_OUTLINE, "    ", "Scenario", " ", "Outline", ":", " ", "Tagged", " ", "scenario", " ", "outline");
		
		ScenarioOutline outline = (ScenarioOutline) stack.peek();
		Assert.assertEquals("Scenario Outline", outline.getKeyword());
		Assert.assertEquals("Tagged scenario outline", outline.getText());
		
		Assert.assertEquals(0, feature.getTags().size());
		Assert.assertEquals(3, outline.getTags().size());
		Assert.assertEquals("abc", outline.getTags().get(0).getTag());
		Assert.assertEquals("def", outline.getTags().get(1).getTag());
		Assert.assertEquals("ghi", outline.getTags().get(2).getTag());
		Assert.assertNull(tags);
	}
	
	@Test
	public void testBackgroundTags() {
		Tag tag = new Tag("oops");
		tag.setLocation(new Location() {
			@Override
			public String getLocation() {
				return "?: Line 41, column 1";
			}
		});
		
		tags = new ArrayList<Tag>();
		tags.add(tag);
		
		Feature feature = new Feature();
		stack.push(feature);
		
		try {
			processFeatureChild("Background", GherkinKeywordType.BACKGROUND, "    ", "Background", ":", " ", "Tagged", " ", "background");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	protected void processFeatureChild(String keyword, GherkinKeywordType type, String... tokens) {
		SortedMap<Location, String> map = createTextTokens(tokens);
		
		StringBuffer buffer = new StringBuffer();
		for(String token : tokens)
			buffer.append(token);
		
		processFeatureChild(map, keyword, buffer.toString().trim(), new Location() {
			@Override
			public String getLocation() {
				return "FakeFile.feature: Line 42, column 42";
			}
		}, null, type);
	}

}