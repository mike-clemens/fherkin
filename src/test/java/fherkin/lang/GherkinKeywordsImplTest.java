package fherkin.lang;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  GherkinKeywords and GherkinKeywordsFactory
 * 
 * @author Mike Clemens
 */
public class GherkinKeywordsImplTest {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Test
	public void testDefault() throws Exception {
		GherkinKeywords instance = GherkinKeywordsFactory.getInstance().getDefaultInstance();
		Assert.assertArrayEquals(new String[] {"Ability", "Business Need", "Feature"}, instance.getFeature().toArray());
		Assert.assertArrayEquals(new String[] {"Background"}, instance.getBackground().toArray());
		Assert.assertArrayEquals(new String[] {"Example", "Scenario"}, instance.getScenario().toArray());
		Assert.assertArrayEquals(new String[] {"Scenario Outline", "Scenario Template"}, instance.getScenarioOutline().toArray());
		Assert.assertArrayEquals(new String[] {"Rule"}, instance.getRule().toArray());
		Assert.assertArrayEquals(new String[] {"*", "Given"}, instance.getGiven().toArray());
		Assert.assertArrayEquals(new String[] {"*", "When"}, instance.getWhen().toArray());
		Assert.assertArrayEquals(new String[] {"*", "Then"}, instance.getThen().toArray());
		Assert.assertArrayEquals(new String[] {"*", "And"}, instance.getAnd().toArray());
		Assert.assertArrayEquals(new String[] {"*", "But"}, instance.getBut().toArray());
		Assert.assertArrayEquals(new String[] {"Examples", "Scenarios"}, instance.getExamples().toArray());
	}
	
	@Test
	public void testSpecificLocaleExactMatch() throws Exception {
		GherkinKeywords instance = GherkinKeywordsFactory.getInstance().getInstanceForLocale("de");
		Assert.assertArrayEquals(new String[] {"Funktion", "Funktionalität"}, instance.getFeature().toArray());
		Assert.assertArrayEquals(new String[] {"Grundlage", "Hintergrund", "Voraussetzungen", "Vorbedingungen"}, instance.getBackground().toArray());
		Assert.assertArrayEquals(new String[] {"Beispiel", "Szenario"}, instance.getScenario().toArray());
		Assert.assertArrayEquals(new String[] {"Szenarien", "Szenariogrundriss"}, instance.getScenarioOutline().toArray());
		Assert.assertArrayEquals(new String[] {"Regel", "Rule"}, instance.getRule().toArray());
		Assert.assertArrayEquals(new String[] {"*", "Angenommen", "Gegeben sei", "Gegeben seien"}, instance.getGiven().toArray());
		Assert.assertArrayEquals(new String[] {"*", "Wenn"}, instance.getWhen().toArray());
		Assert.assertArrayEquals(new String[] {"*", "Dann"}, instance.getThen().toArray());
		Assert.assertArrayEquals(new String[] {"*", "Und"}, instance.getAnd().toArray());
		Assert.assertArrayEquals(new String[] {"*", "Aber"}, instance.getBut().toArray());
		Assert.assertArrayEquals(new String[] {"Beispiele"}, instance.getExamples().toArray());
	}
	
	@Test
	public void testSpecificLocalePartialMatch() throws Exception {
		GherkinKeywords instance = GherkinKeywordsFactory.getInstance().getInstanceForLocale("en-CA-Ontario-Midland");
		Assert.assertArrayEquals(new String[] {"Ability", "Business Need", "Feature"}, instance.getFeature().toArray());
		Assert.assertArrayEquals(new String[] {"Background"}, instance.getBackground().toArray());
		Assert.assertArrayEquals(new String[] {"Example", "Scenario"}, instance.getScenario().toArray());
		Assert.assertArrayEquals(new String[] {"Scenario Outline", "Scenario Template"}, instance.getScenarioOutline().toArray());
		Assert.assertArrayEquals(new String[] {"Rule"}, instance.getRule().toArray());
		Assert.assertArrayEquals(new String[] {"*", "Given"}, instance.getGiven().toArray());
		Assert.assertArrayEquals(new String[] {"*", "When"}, instance.getWhen().toArray());
		Assert.assertArrayEquals(new String[] {"*", "Then"}, instance.getThen().toArray());
		Assert.assertArrayEquals(new String[] {"*", "And"}, instance.getAnd().toArray());
		Assert.assertArrayEquals(new String[] {"*", "But"}, instance.getBut().toArray());
		Assert.assertArrayEquals(new String[] {"Examples", "Scenarios"}, instance.getExamples().toArray());
	}
	
	@Test
	public void testSpecificLocaleNoMatch() throws Exception {
		try {
			GherkinKeywordsFactory.getInstance().getInstanceForLocale("abc-def-xyz");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(IllegalArgumentException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testSpecificLocaleNullLocale() throws Exception {
		try {
			GherkinKeywordsFactory.getInstance().getInstanceForLocale(null);
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(IllegalArgumentException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testSpecificLocaleBlankLocale() throws Exception {
		try {
			GherkinKeywordsFactory.getInstance().getInstanceForLocale("");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(IllegalArgumentException e) {
			log.debug(e.getMessage());
		}
	}
	
}