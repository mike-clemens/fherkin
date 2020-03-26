package fherkin.io;

import fherkin.model.location.Location;

/**
 * Exception thrown to indicate an error with gherkin syntax.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class GherkinSyntaxException extends RuntimeException {
	
	private static final long serialVersionUID = -6394504797678270228L;

	public GherkinSyntaxException(Location location, String message) {
		super(message + ": " + location.getLocation());
	}

}