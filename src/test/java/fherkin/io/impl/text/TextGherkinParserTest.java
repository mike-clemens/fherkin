package fherkin.io.impl.text;

import fherkin.model.location.Location;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  TextGherkinParser
 * 
 * @author Mike Clemens
 */
public class TextGherkinParserTest extends TextGherkinParser {
	
	public TextGherkinParserTest() {
		filename = "abc.feature";
	}
	
	@Test
	public void testTokenize() {
		Map<Location, String> tokens = tokenize(1, " \t\n\r abc \t\n\r 123 \t\n\r !!!@#$ 123.45.67");
		String[] expected = new String[] {
				" \t\n\r ",
				"abc",
				" \t\n\r ",
				"123",
				" \t\n\r ",
				"!!!",
				"@",
				"#",
				"$",
				" ",
				"123.45.67"
		};
		
		int column = 1;
		int i = 0;
		
		Assert.assertEquals(expected.length, tokens.size());
		for(Map.Entry<Location, String> entry : tokens.entrySet()) {
			Assert.assertEquals(expected[i], entry.getValue());
			Assert.assertEquals("abc.feature: Line 1, column " + column, entry.getKey().getLocation());
			
			i++;
			column += entry.getValue().length();
		}
	}
	
	@Test
	public void testTokenizeBlank() {
		Map<Location, String> tokens = tokenize(1, "");
		Assert.assertEquals(0, tokens.size());
	}

}