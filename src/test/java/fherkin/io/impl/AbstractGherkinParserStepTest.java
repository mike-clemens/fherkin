package fherkin.io.impl;

import fherkin.io.GherkinSyntaxException;
import fherkin.model.entry.Feature;
import fherkin.model.entry.Scenario;
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
 * Test class for:  AbstractGherkinParser steps (ie.  given, when, then)
 * 
 * @author Mike Clemens
 */
public class AbstractGherkinParserStepTest extends BaseAbstractGherkinParserTestCase {

	private Log log = LogFactory.getLog(getClass());
	
	@Before
	public void before() {
		stack.clear();
		entries.clear();
	}
	
	@Test
	public void testStepNoParent() {
		try {
			processStep("Given", "    ", "Given", " ", "I", " ", "have", " ", "42", " ", "cucumbers");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testStepNoScenarioParent() {
		stack.push(new Feature());
		try {
			processStep("Given", "    ", "Given", " ", "I", " ", "have", " ", "42", " ", "cucumbers");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}	
	}
	
	@Test
	public void testStepWithColon() {
		stack.push(new Feature());
		stack.push(new Scenario());
		try {
			processStep("Given", "    ", "Given", ":", " ", "I", " ", "have", " ", "42", " ", "cucumbers");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testStep() {
		Scenario scenario = new Scenario();
		stack.push(new Feature());
		stack.push(scenario);
		processStep("Given", "    ", "Given", " ", "I", " ", "have", " ", "42", " ", "cucumbers");
		
		Step step = (Step) stack.peek();
		Assert.assertEquals("Given", step.getKeyword());
		Assert.assertEquals("I have 42 cucumbers", step.getText());
		Assert.assertSame(scenario, step.getScenario());
		
		Assert.assertEquals(1, scenario.getSteps().size());
		Assert.assertSame(step, scenario.getSteps().get(0));
	}
	
	@Test
	public void testMultipleSteps() {
		Scenario scenario = new Scenario();
		stack.push(new Feature());
		stack.push(scenario);
		processStep("Given", "    ", "Given", " ", "I", " ", "have", " ", "42", " ", "cucumbers");
		processStep("When", "    ", "When", " ", "I", " ", "eat", " ", "all", " ", "of", " ", "them");
		processStep("Then", "    ", "Then", " ", "I", " ", "will", " ", "have", " ", "none", " ", "left");
		processStep("And", "    ", "And", " ", "I", " ", "will", " ", "be", " ", "sad");
		
		Assert.assertEquals(4, scenario.getSteps().size());
		
		Step given = scenario.getSteps().get(0);
		Assert.assertEquals("Given", given.getKeyword());
		Assert.assertEquals("I have 42 cucumbers", given.getText());
		Assert.assertSame(scenario, given.getScenario());
		
		Step when = scenario.getSteps().get(1);
		Assert.assertEquals("When", when.getKeyword());
		Assert.assertEquals("I eat all of them", when.getText());
		Assert.assertSame(scenario, when.getScenario());

		Step then = scenario.getSteps().get(2);
		Assert.assertEquals("Then", then.getKeyword());
		Assert.assertEquals("I will have none left", then.getText());
		Assert.assertSame(scenario, then.getScenario());

		Step and = scenario.getSteps().get(3);
		Assert.assertEquals("And", and.getKeyword());
		Assert.assertEquals("I will be sad", and.getText());
		Assert.assertSame(scenario, and.getScenario());
	}
	
	@Test
	public void testStepTags() {
		Tag tag = new Tag("oops");
		tag.setLocation(new Location() {
			@Override
			public String getLocation() {
				return "?: Line 41, column 1";
			}
		});
		
		tags = new ArrayList<Tag>();
		tags.add(tag);
		
		Scenario scenario = new Scenario();
		stack.push(new Feature());
		stack.push(scenario);
		
		try {
			processStep("Given", "    ", "Given", " ", "I", " ", "have", " ", "42", " ", "cucumbers");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	protected void processStep(String keyword, String... tokens) {
		SortedMap<Location, String> map = createTextTokens(tokens);
		
		StringBuffer buffer = new StringBuffer();
		for(String token : tokens)
			buffer.append(token);
		
		processStep(map, keyword, buffer.toString().trim(), new Location() {
			@Override
			public String getLocation() {
				return "FakeFile.feature: Line 42, column 42";
			}
		}, null, null);
	}

}