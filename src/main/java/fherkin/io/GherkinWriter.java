package fherkin.io;

import fherkin.model.GherkinEntry;
import java.io.IOException;
import java.util.List;

/**
 * Interface for a gherkin writer.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public interface GherkinWriter {
	
	void write(List<GherkinEntry> entries) throws IOException;

}