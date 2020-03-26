package fherkin.io.impl;

import fherkin.model.location.Location;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedMap;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  AbstractGherkinParser
 * 
 * @author Mike Clemens
 */
public class AbstractGherkinParserTest extends BaseAbstractGherkinParserTestCase {
	
	@Test
	public void testGetLineTextSingleWordWithColon() {
		SortedMap<Location, String> map = createTextTokens("    ", "Feature", ":", "This", " ", "is", " ", "a", " ", "feature");
		
		Pair<Location, String> pair = getLineText(map);
		Assert.assertEquals("Feature", pair.getSecond());
	}

	@Test
	public void testGetLineTextMultipleWordsWithColon() {
		SortedMap<Location, String> map = createTextTokens("    ", "Scenario", " ", "Outline", ":", " ", "This", " ", "is", " ", "a", " ", "feature");
		
		Pair<Location, String> pair = getLineText(map);
		Assert.assertEquals("Scenario Outline", pair.getSecond());
	}

	@Test
	public void testGetLineTextWithoutColon() {
		SortedMap<Location, String> map = createTextTokens("    ", "Given", " ", "I", " ", "have", " ", "5", " ", "cucumbers");
		
		Pair<Location, String> pair = getLineText(map);
		Assert.assertEquals("Given I have 5 cucumbers", pair.getSecond());
	}

	@Test
	public void testGetLineTextWithComment() {
		SortedMap<Location, String> map = createTextTokens("    ", "Given", " ", "I", " ", "have", " ", "5", " ", "cucumbers", "   ", "#", " ", "comment");
		
		Pair<Location, String> pair = getLineText(map);
		Assert.assertEquals("Given I have 5 cucumbers", pair.getSecond());
	}

	@Test
	public void testGetLineTextDataTable() {
		SortedMap<Location, String> map = createTextTokens("    ", "|", " ", "2", " ", "|", " ", "3", " ", "|", " ", "5", " ", "|");
		
		Pair<Location, String> pair = getLineText(map);
		Assert.assertEquals("| 2 | 3 | 5 |", pair.getSecond());
	}
	
	@Test
	public void isMatchEquals() {
		Set<String> set = new LinkedHashSet<String>();
		set.add("abc");
		set.add("Given");
		set.add("xyz");
		Assert.assertTrue(isMatch("Given", set));
	}

	@Test
	public void isMatchStartsWith() {
		Set<String> set = new LinkedHashSet<String>();
		set.add("abc");
		set.add("Given");
		set.add("xyz");
		Assert.assertTrue(isMatch("Given I have two cucumbers", set));
	}

	@Test
	public void isMatchStartsWithButNoWhitespace() {
		Set<String> set = new LinkedHashSet<String>();
		set.add("abc");
		set.add("Given");
		set.add("xyz");
		Assert.assertFalse(isMatch("Givenz I have two cucumbers", set));
	}

	@Test
	public void isMatchShorterThanKeywords() {
		Set<String> set = new LinkedHashSet<String>();
		set.add("abc");
		set.add("Given");
		set.add("xyz");
		Assert.assertFalse(isMatch("If I have two cucumbers", set));
	}

}