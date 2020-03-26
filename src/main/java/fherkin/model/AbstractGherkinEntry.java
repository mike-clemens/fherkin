package fherkin.model;

import fherkin.model.entry.Comment;
import fherkin.model.location.Location;

/**
 * Base class for a gherkin entry.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public abstract class AbstractGherkinEntry implements GherkinEntry {
	
	private Location location;
	private Comment comment;
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

}