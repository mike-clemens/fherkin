package fherkin.io.impl;

import fherkin.model.GherkinEntry;
import fherkin.model.location.Location;
import fherkin.model.location.TextFileLocation;
import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Base class for test classes for AbstractGherkinParser.
 * 
 * @author Mike Clemens
 */
public class BaseAbstractGherkinParserTestCase extends AbstractGherkinParser {

	@Override
	public List<GherkinEntry> parse() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected SortedMap<Location, String> createTextTokens(String... strings) {
		SortedMap<Location, String> map = new TreeMap<Location, String>();
		int i = 1;

		TextFileLocation location = new TextFileLocation();
		location.setFilename("?");
		location.setLine(1);
		location.setColumn(i);

		for(String s : strings) {
			map.put(location, s);
			location = location.clone();
			i += s.length();
			location.setColumn(i);
		}
		
		return map;
	}

}