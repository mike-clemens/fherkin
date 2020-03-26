package fherkin.tests;

import fherkin.io.impl.csv.CSVGherkinParser;
import fherkin.io.impl.csv.CSVGherkinWriter;
import fherkin.model.GherkinEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for comparing parsed gherkin CSV's to written files.
 * 
 * @author Mike Clemens
 */
public class CSVToCSVTest {

	@Test
	public void testScenario() throws Exception {
		doTest("scenario");
	}
	
	@Test
	public void testScenarioOutline() throws Exception {
		doTest("scenarioOutline");
	}
	
	@Test
	public void testStepTable() throws Exception {
		doTest("stepTable");
	}
	
	@Test
	public void testMultiLineFeature() throws Exception {
		doTest("multiLineFeature");
	}
	
	@Test
	public void testTags() throws Exception {
		doTest("tags");
	}
	
	@Test
	public void testNonEnglish() throws Exception {
		doTest("nonEnglish");
	}
	
	///// helper methods
	
	protected void doTest(String name) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
			classLoader = getClass().getClassLoader();
		
		InputStream inputStream;
		OutputStream outputStream;
		
		// read the source file
		String expected;
		inputStream = classLoader.getResourceAsStream(getClass().getPackage().getName().replaceAll("\\.", "/") + "/csv/" + name + ".csv");
		try {
			outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read;
			do {
				read = inputStream.read(buffer);
				if(read > 0)
					outputStream.write(buffer, 0, read);
			}
			while(read > 0);
			inputStream.close();
			outputStream.close();
			
			expected = outputStream.toString().trim();
		}
		finally {
			inputStream.close();
		}
		
		// convert the file from text to text
		List<GherkinEntry> entries = new CSVGherkinParser(name + ".feature", expected, CSVFormat.DEFAULT).parse();
		
		outputStream = new ByteArrayOutputStream();
		new CSVGherkinWriter(outputStream, CSVFormat.DEFAULT).write(entries);
		outputStream.close();
		
		String actual = outputStream.toString().trim();
		System.out.println(actual);
		
		// compare the text
		Assert.assertArrayEquals(expected.split("\n"), actual.split("\n")); 
	}

}