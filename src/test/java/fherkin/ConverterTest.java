package fherkin;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  Converter
 * 
 * @author Mike Clemens
 */
public class ConverterTest extends Converter {
	
	///// getSourceFileFormat
	
	@Test
	public void testGetSourceFileFormatProvided() throws IOException {
		setSourceFormat(FileFormat.XLSX);
		Assert.assertEquals(FileFormat.XLSX, getSourceFileFormat(new File("xyz.feature")));
	}
	
	@Test
	public void testGetSourceFileFormatInferred() throws IOException {
		setSourceFormat(null);
		Assert.assertEquals(FileFormat.FEATURE, getSourceFileFormat(new File("xyz.FeAtUrE")));
		Assert.assertEquals(FileFormat.CSV, getSourceFileFormat(new File("xyz.CsV")));
		Assert.assertEquals(FileFormat.XLS, getSourceFileFormat(new File("xyz.XlS")));
		Assert.assertEquals(FileFormat.XLSX, getSourceFileFormat(new File("xyz.XlSx")));
	}
	
	@Test
	public void testGetSourceFileFormatNoExtension() {
		setSourceFormat(null);
		try {
			getSourceFileFormat(new File("xyz"));
			Assert.fail("Expected IOException");
		}
		catch(IOException e) {
			// do nothing here; the test case passed
		}
	}

	@Test
	public void testGetSourceFileFormatUnknownExtension() {
		setSourceFormat(null);
		try {
			getSourceFileFormat(new File("xyz.qwerty"));
			Assert.fail("Expected IOException");
		}
		catch(IOException e) {
			// do nothing here; the test case passed
		}
	}

}