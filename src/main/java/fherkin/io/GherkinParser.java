package fherkin.io;

import fherkin.model.GherkinEntry;
import java.io.IOException;
import java.util.List;

/**
 * Interface for a gherkin parser.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public interface GherkinParser {
	
	List<GherkinEntry> parse() throws IOException;

}