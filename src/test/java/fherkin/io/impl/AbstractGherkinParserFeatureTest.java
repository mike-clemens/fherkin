package fherkin.io.impl;

import fherkin.io.GherkinSyntaxException;
import fherkin.model.entry.Feature;
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
 * Test class for:  AbstractGherkinParser features
 * 
 * @author Mike Clemens
 */
public class AbstractGherkinParserFeatureTest extends BaseAbstractGherkinParserTestCase {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Before
	public void before() {
		stack.clear();
		entries.clear();
		tags = null;
	}
	
	@Test
	public void testFeatureNotTop() {
		stack.push(new Feature());
		try {
			processFeature("Feature", "    ", "Feature", ":", " ", "Desc");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testFeatureNoColon() {
		try {
			processFeature("Feature", "    ", "Feature", " ", "Desc");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testFeatureSingleWord() {
		processFeature("Feature", "    ", "Feature", ":", " ", "Test", " ", "feature");
		Feature feature = (Feature) stack.peek();
		Assert.assertEquals("Feature", feature.getKeyword());
		Assert.assertEquals("Test feature", feature.getText());
	}
	
	@Test
	public void testFeatureMultipleWords() {
		processFeature("Business Need", "    ", "Business", " ", "Need", ":", " ", "Test", " ", "feature");
		Feature feature = (Feature) stack.peek();
		Assert.assertEquals("Business Need", feature.getKeyword());
		Assert.assertEquals("Test feature", feature.getText());
	}
	
	@Test
	public void testFeatureTags() {
		tags = new ArrayList<Tag>();
		tags.add(new Tag("abc"));
		tags.add(new Tag("def"));
		tags.add(new Tag("ghi"));
		
		processFeature("Feature", "    ", "Feature", ":", " ", "Tagged", " ", "feature");
		Feature feature = (Feature) stack.peek();
		Assert.assertEquals("Feature", feature.getKeyword());
		Assert.assertEquals("Tagged feature", feature.getText());
		
		Assert.assertEquals(3, feature.getTags().size());
		Assert.assertEquals("abc", feature.getTags().get(0).getTag());
		Assert.assertEquals("def", feature.getTags().get(1).getTag());
		Assert.assertEquals("ghi", feature.getTags().get(2).getTag());
		Assert.assertNull(tags);
	}
	
	protected void processFeature(String keyword, String... tokens) {
		SortedMap<Location, String> map = createTextTokens(tokens);
		
		StringBuffer buffer = new StringBuffer();
		for(String token : tokens)
			buffer.append(token);
		
		processFeature(map, keyword, buffer.toString().trim(), new Location() {
			@Override
			public String getLocation() {
				return "FakeFile.feature: Line 42, column 42";
			}
		}, null);
	}

}