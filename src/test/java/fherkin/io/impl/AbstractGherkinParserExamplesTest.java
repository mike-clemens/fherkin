package fherkin.io.impl;

import fherkin.io.GherkinSyntaxException;
import fherkin.model.entry.Examples;
import fherkin.model.entry.Feature;
import fherkin.model.entry.Scenario;
import fherkin.model.entry.ScenarioOutline;
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
 * Test class for:  AbstractGherkinParser examples
 * 
 * @author Mike Clemens
 */
public class AbstractGherkinParserExamplesTest extends BaseAbstractGherkinParserTestCase {

	private Log log = LogFactory.getLog(getClass());
	
	@Before
	public void before() {
		stack.clear();
		entries.clear();
	}
	
	@Test
	public void testExamplesNoParent() {
		try {
			processExamples("Examples", "    ", "Examples", ":");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testExamplesNoScenarioOutlineParent() {
		stack.push(new Feature());
		stack.push(new Scenario());
		try {
			processExamples("Examples", "    ", "Examples", ":");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}	
	}
	
	@Test
	public void testExamplesWithoutColon() {
		stack.push(new Feature());
		stack.push(new ScenarioOutline());
		try {
			processExamples("Examples", "    ", "Examples");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testExamplesWithTrailingText() {
		stack.push(new Feature());
		stack.push(new ScenarioOutline());
		try {
			processExamples("Examples", "    ", "Examples", ":", " ", "This", " ", "text", " ", "is", " ", "not", " ", "allowed");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testExamples() {
		ScenarioOutline outline = new ScenarioOutline();
		stack.push(new Feature());
		stack.push(outline);
		processExamples("Examples", "    ", "Examples", ":");
		
		Examples examples = (Examples) stack.peek();
		Assert.assertNotNull(examples);
		Assert.assertSame(examples, outline.getExamples());
		Assert.assertSame(outline, examples.getScenarioOutline());
	}
	
	@Test
	public void testMultipleExamples() {
		ScenarioOutline outline = new ScenarioOutline();
		outline.setExamples(new Examples());
		
		stack.push(new Feature());
		stack.push(outline);
		try {
			processExamples("Examples", "    ", "Examples", ":");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testExamplesTags() {
		Tag tag = new Tag("oops");
		tag.setLocation(new Location() {
			@Override
			public String getLocation() {
				return "?: Line 41, column 1";
			}
		});
		
		tags = new ArrayList<Tag>();
		tags.add(tag);
		
		ScenarioOutline outline = new ScenarioOutline();
		stack.push(new Feature());
		stack.push(outline);
		
		try {
			processExamples("Examples", "    ", "Examples", ":");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	protected void processExamples(String keyword, String... tokens) {
		SortedMap<Location, String> map = createTextTokens(tokens);
		
		StringBuffer buffer = new StringBuffer();
		for(String token : tokens)
			buffer.append(token);
		
		processExamples(map, keyword, buffer.toString().trim(), new Location() {
			@Override
			public String getLocation() {
				return "FakeFile.feature: Line 42, column 42";
			}
		}, null);
	}

}