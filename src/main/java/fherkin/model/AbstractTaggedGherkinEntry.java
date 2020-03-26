package fherkin.model;

import fherkin.model.entry.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for a gherkin entry that can have tags associated with it.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public abstract class AbstractTaggedGherkinEntry extends AbstractGherkinEntry {
	
	private List<Tag> tags = new ArrayList<Tag>();

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public void addTag(Tag tag) {
		tags.add(tag);
	}

}