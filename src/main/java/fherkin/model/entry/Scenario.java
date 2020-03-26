package fherkin.model.entry;

import fherkin.lang.GherkinKeywordType;
import fherkin.model.AbstractTaggedGherkinEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a scenario in a gherkin file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Scenario extends AbstractTaggedGherkinEntry {
	
	private Feature feature;
	private String keyword;
	private String text;
	private List<Step> steps = new ArrayList<Step>();
	
	public int getMaxKeywordLength() {
		int length = 0;
		for(Step step : steps)
			if(step.getKeyword().length() > length)
				length = step.getKeyword().length();
		return length;
	}
	
	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.SCENARIO;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
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
	
	public List<Step> getSteps() {
		return steps;
	}
	
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
	
	public void addStep(Step step) {
		steps.add(step);
	}

}