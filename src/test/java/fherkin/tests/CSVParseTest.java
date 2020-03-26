package fherkin.tests;

import fherkin.io.impl.csv.CSVGherkinParser;
import fherkin.model.GherkinEntry;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Test class for:  CSVGherkinParser
 * 
 * @author Mike Clemens
 */
public class CSVParseTest extends AbstractParseTest {

	@Override
	protected List<GherkinEntry> doParse(String name) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
			classLoader = getClass().getClassLoader();
		
		InputStream inputStream = classLoader.getResourceAsStream(getClass().getPackage().getName().replaceAll("\\.", "/") + "/csv/" + name + ".csv");
		return new CSVGherkinParser(name + ".csv", inputStream, Charset.forName("utf8")).parse();
	}

}