package fherkin.io;

import java.io.File;
import java.io.IOException;

/**
 * Callback interface for the FileScanner component.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public interface FileScannerCallback {
	
	void process(File file, String relativePath) throws IOException;

}