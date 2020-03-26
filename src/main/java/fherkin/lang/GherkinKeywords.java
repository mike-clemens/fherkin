package fherkin.lang;

import java.util.Set;

/**
 * Interface defining internationalized gherkin keywords.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public interface GherkinKeywords {
	
	Set<String> getKeywords();
	
	Set<String> getFeature();
	Set<String> getBackground();
	Set<String> getScenario();
	Set<String> getScenarioOutline();
	Set<String> getRule();
	Set<String> getGiven();
	Set<String> getWhen();
	Set<String> getThen();
	Set<String> getAnd();
	Set<String> getBut();
	Set<String> getExamples();
	
}