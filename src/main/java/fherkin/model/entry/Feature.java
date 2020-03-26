package fherkin.model.entry;

import fherkin.lang.GherkinKeywordType;
import fherkin.model.AbstractTaggedGherkinEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a feature in a gherkin file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Feature extends AbstractTaggedGherkinEntry {
	
	private String keyword;
	private String text;
	private List<String> additionalText = new ArrayList<String>();
	private Background background;
	private List<Scenario> scenarios = new ArrayList<Scenario>();
	
	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.FEATURE;
	}

	@Override
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public List<String> getAdditionalText() {
		return additionalText;
	}

	public void setAdditionalText(List<String> additionalText) {
		this.additionalText = additionalText;
	}
	
	public void addAdditionalText(String additionalText) {
		this.additionalText.add(additionalText);
	}

	public Background getBackground() {
		return background;
	}

	public void setBackground(Background background) {
		this.background = background;
	}

	public List<Scenario> getScenarios() {
		return scenarios;
	}
	
	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	
	public void addScenario(Scenario scenario) {
		scenarios.add(scenario);
	}

}