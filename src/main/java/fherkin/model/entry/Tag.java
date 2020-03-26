package fherkin.model.entry;

import fherkin.lang.GherkinKeywordType;
import fherkin.model.AbstractGherkinEntry;

/**
 * Class representing a tag on a gherkin file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Tag extends AbstractGherkinEntry {
	
	private String tag;
	
	public Tag() {
	}
	
	public Tag(String tag) {
		this.tag = tag;
	}

	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.TAG;
	}

	@Override
	public String getKeyword() {
		return "@";
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}