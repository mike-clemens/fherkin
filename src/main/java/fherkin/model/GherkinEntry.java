package fherkin.model;

import fherkin.lang.GherkinKeywordType;

/**
 * Interface defining a gherkin entry.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public interface GherkinEntry {
	
	String getKeyword();
	GherkinKeywordType getType();

}