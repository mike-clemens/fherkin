package fherkin.model.entry;

import fherkin.lang.GherkinKeywordType;

/**
 * Class representing a background in a gherkin file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Background extends Scenario {
	
	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.BACKGROUND;
	}

}