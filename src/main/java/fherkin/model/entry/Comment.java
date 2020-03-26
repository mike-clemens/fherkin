package fherkin.model.entry;

import fherkin.lang.GherkinKeywordType;
import fherkin.model.AbstractGherkinEntry;

/**
 * Class representing a comment in a gherkin file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Comment extends AbstractGherkinEntry {
	
	private String text;
	
	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.COMMENT;
	}

	@Override
	public String getKeyword() {
		return "#";
	}

	@Override
	public Comment getComment() {
		return this;
	}

	@Override
	public void setComment(Comment comment) {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}