package fherkin.io.impl.text;

import fherkin.io.impl.AbstractGherkinParser;
import fherkin.model.GherkinEntry;
import fherkin.model.location.Location;
import fherkin.model.location.TextFileLocation;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Gherkin parser that reads from a text feature file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class TextGherkinParser extends AbstractGherkinParser {
	
	protected String filename;
	protected Reader reader;
	
	public TextGherkinParser(File file) throws IOException {
		filename = file.getName();
		reader = new FileReader(file);
	}
	
	public TextGherkinParser(File file, Charset charset) throws IOException {
		filename = file.getName();
		reader = new InputStreamReader(new FileInputStream(file), charset);
	}
	
	public TextGherkinParser(String filename, InputStream inputStream) throws IOException {
		this.filename = filename;
		reader = new InputStreamReader(inputStream);
	}
	
	public TextGherkinParser(String filename, InputStream inputStream, Charset charset) throws IOException {
		this.filename = filename;
		reader = new InputStreamReader(inputStream, charset);
	}
	
	public TextGherkinParser(String filename, Reader reader) throws IOException {
		this.filename = filename;
		this.reader = reader;
	}
	
	public TextGherkinParser(String filename, String contents) throws IOException {
		this.filename = filename;
		reader = new StringReader(contents);
	}
	
	protected TextGherkinParser() {
		// for unit testing only
	}
	
	@Override
	public List<GherkinEntry> parse() throws IOException {
		BufferedReader in = new BufferedReader(reader);
		try {
			String line;
			int lineNumber = 0;
			SortedMap<Location, String> tokens;
			do {
				line = in.readLine();
				if(line != null) {
					tokens = tokenize(++lineNumber, line);
					processLine(tokens);
				}
			}
			while(line != null);
			return entries;
		}
		finally {
			in.close();
		}
	}
	
	protected SortedMap<Location, String> tokenize(int lineNumber, String line) {
		SortedMap<Location, String> map = new TreeMap<Location, String>();

		TextFileLocation location = new TextFileLocation();
		location.setFilename(filename);
		location.setLine(lineNumber);
		location.setColumn(1);

		StringBuffer buffer = null;
		char[] chars = line.toCharArray();
		char current = ' ';
		char type;
		char c;
		for(int i = 0; i < chars.length; i++) {
			c = chars[i];
			
			// determine the token type:  whitespace, letter, digit, or symbol
			if(Character.isWhitespace(c))
				type = ' ';
			else
			if(Character.isLetter(c))
				type = 'X';
			else
			if(Character.isDigit(c))
				type = '9';
			else
				type = c;
			
			// if we are processing digits and the type is a decimal place, then we will make it act like a digit
			if(type == '.' && current == '9')
				type = '9';
			
			if(buffer == null) {
				buffer = new StringBuffer();
				buffer.append(c);
				current = type;
			}
			else
			if(current == type)
				buffer.append(c);
			else {
				map.put(location, buffer.toString());
				
				location = location.clone();
				location.setColumn(i + 1);
				
				buffer = new StringBuffer();
				buffer.append(c);
				current = type;
			}
		}
		
		if(buffer != null)
			map.put(location, buffer.toString());
		
		return map;
	}
	
}