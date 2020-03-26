package fherkin.io.impl;

import fherkin.io.GherkinSyntaxException;
import fherkin.model.entry.Feature;
import fherkin.model.entry.Scenario;
import fherkin.model.entry.Tag;
import fherkin.model.location.Location;
import java.util.SortedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  AbstractGherkinParser tags
 * 
 * @author Mike Clemens
 */
public class AbstractGherkinParserTagTest extends BaseAbstractGherkinParserTestCase {

	private Log log = LogFactory.getLog(getClass());
	
	@Before
	public void before() {
		stack.clear();
		tags = null;
	}
	
	@Test
	public void testSingleTagTopLevel() {
		processTags("    ", "@", "tag", "1");
		
		Assert.assertEquals(1, tags.size());
		Tag tag = tags.get(0);
		Assert.assertEquals("tag1", tag.getTag());
	}
	
	@Test
	public void testSingleTagUnderFeature() {
		stack.push(new Feature());
		processTags("    ", "@", "tag", "1");
		
		Assert.assertEquals(1, tags.size());
		Tag tag = tags.get(0);
		Assert.assertEquals("tag1", tag.getTag());
	}
	
	@Test
	public void testSingleTagTrailingSpaces() {
		processTags("    ", "@", "tag", "1", " \t \t \t ");
		
		Assert.assertEquals(1, tags.size());
		Tag tag = tags.get(0);
		Assert.assertEquals("tag1", tag.getTag());
	}
	
	@Test
	public void testMultipleTags() {
		processTags("    ", "@", "tag", "1", " ", "@", "tag", "2", " ", "@", "tag", "3");
		
		Assert.assertEquals(3, tags.size());
		Tag tag;
		for(int i = 0; i < tags.size(); i++) {
			tag = tags.get(i);
			Assert.assertEquals("tag" + (i + 1), tag.getTag());
		}
	}
	
	@Test
	public void testExistingTags() {
		processTags("    ", "@", "tag", "1", " ", "@", "tag", "2");
		processTags("    ", "@", "tag", "3", " ", "@", "tag", "4");
		
		Assert.assertEquals(4, tags.size());
		Tag tag;
		for(int i = 0; i < tags.size(); i++) {
			tag = tags.get(i);
			Assert.assertEquals("tag" + (i + 1), tag.getTag());
		}
	}
	
	@Test
	public void testTagNotTopLevelOrUnderFeature() {
		stack.push(new Scenario());
		try {
			processTags("@", "tag", "1");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testTagInvalidName() {
		try {
			processTags("@", "tag", "*", "1");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testTagNotAboveFeatureOrScenario() {
		stack.push(new Scenario());
		try {
			processTags("@", "tag", "1");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testTagInCombinedToken() {
		processTags("@tag1");
		
		Assert.assertEquals(1, tags.size());
		Assert.assertEquals("tag1", tags.get(0).getTag());
	}
	
	@Test
	public void testTagsInCombinedToken() {
		processTags(" \t\t\t @tag1 \t\t\t @tag2 \t\t\t @tag3 \t\t\t ");
		
		Assert.assertEquals(3, tags.size());
		Tag tag;
		for(int i = 0; i < tags.size(); i++) {
			tag = tags.get(i);
			Assert.assertEquals("tag" + (i + 1), tag.getTag());
		}
	}
	
	protected void processTags(String... tokens) {
		SortedMap<Location, String> map = createTextTokens(tokens);
		
		StringBuffer buffer = new StringBuffer();
		for(String token : tokens)
			buffer.append(token);
		
		processTags(map, buffer.toString().trim(), new Location() {
			@Override
			public String getLocation() {
				return "FakeFile.feature: Line 42, column 42";
			}
		}, null);
	}

}