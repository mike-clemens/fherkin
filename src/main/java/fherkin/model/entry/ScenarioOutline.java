package fherkin.model.entry;

import fherkin.lang.GherkinKeywordType;

/**
 * Class representing a scenario outline in a gherkin file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class ScenarioOutline extends Scenario {
	
	private Examples examples;

	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.SCENARIO_OUTLINE;
	}

	public Examples getExamples() {
		return examples;
	}

	public void setExamples(Examples examples) {
		this.examples = examples;
	}
	
}