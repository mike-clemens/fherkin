package fherkin.tests;

import fherkin.io.impl.xlsx.XLSXGherkinParser;
import fherkin.model.GherkinEntry;
import java.io.InputStream;
import java.util.List;

/**
 * Test class for:  XLSXGherkinParser
 * 
 * @author Mike Clemens
 */
public class XLSXParseTest extends AbstractParseTest {

	@Override
	protected List<GherkinEntry> doParse(String name) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
			classLoader = getClass().getClassLoader();
		
		InputStream inputStream = classLoader.getResourceAsStream(getClass().getPackage().getName().replaceAll("\\.", "/") + "/xlsx/" + name + ".xlsx");
		return new XLSXGherkinParser(name + ".xlsx", inputStream).parse();
	}

}