package fherkin.tests;

import fherkin.io.impl.text.TextGherkinParser;
import fherkin.model.GherkinEntry;
import java.io.InputStream;
import java.util.List;

/**
 * Test class for:  TextGherkinParser
 * 
 * @author Mike Clemens
 */
public class TextParseTest extends AbstractParseTest {

	@Override
	protected List<GherkinEntry> doParse(String name) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
			classLoader = getClass().getClassLoader();
		
		InputStream inputStream = classLoader.getResourceAsStream(getClass().getPackage().getName().replaceAll("\\.", "/") + "/text/" + name + ".feature");
		return new TextGherkinParser(name + ".feature", inputStream).parse();
	}

}