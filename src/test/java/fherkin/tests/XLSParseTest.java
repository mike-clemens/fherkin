package fherkin.tests;

import fherkin.io.impl.xls.XLSGherkinParser;
import fherkin.model.GherkinEntry;
import java.io.InputStream;
import java.util.List;

/**
 * Test class for:  XLSGherkinParser
 * 
 * @author Mike Clemens
 */
public class XLSParseTest extends AbstractParseTest {

	@Override
	protected List<GherkinEntry> doParse(String name) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
			classLoader = getClass().getClassLoader();
		
		InputStream inputStream = classLoader.getResourceAsStream(getClass().getPackage().getName().replaceAll("\\.", "/") + "/xls/" + name + ".xls");
		return new XLSGherkinParser(name + ".xls", inputStream).parse();
	}

}