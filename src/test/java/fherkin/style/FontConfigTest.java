package fherkin.style;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  FontConfig
 * 
 * @author Mike Clemens
 */
public class FontConfigTest extends FontConfig {
	
	@Test
	public void testJSONConstructor() throws ParseException {
		FontConfig config = new FontConfig();
		config.populate("Feature", (JSONObject) new JSONParser().parse(
				"{" + 
					"\"bold\": \"yes\"," +
					"\"italic\": \"no\"," +
					"\"underline\": \"10\"," +
					"\"strikeout\": \"yes\"," +
					"\"color\": \"aqua\"," +
					"\"fontHeight\": \"10\"," +
					"\"fontHeightInPoints\": \"20\"," +
					"\"fontName\": \"Aerial\"," +
					"\"typeOffset\": \"15\"," +
					"\"charSet\": \"222\"," +
				"}"));
		
		Assert.assertTrue(config.getBold());
		Assert.assertFalse(config.getItalic());
		Assert.assertEquals(Byte.valueOf("10"), config.getUnderline());
		Assert.assertTrue(config.getStrikeout());
		Assert.assertEquals(Short.valueOf(HSSFColorPredefined.AQUA.getIndex()), config.getColor());
		Assert.assertEquals(Short.valueOf("10"), config.getFontHeight());
		Assert.assertEquals(Short.valueOf("20"), config.getFontHeightInPoints());
		Assert.assertEquals("Aerial", config.getFontName());
		Assert.assertEquals(Short.valueOf("15"), config.getTypeOffset());
		Assert.assertEquals(Integer.valueOf("222"), config.getCharSet());
	}

	@Test
	public void testJSONConstructorEmpty() throws ParseException {
		FontConfig config = new FontConfig();
		config.populate("Feature", (JSONObject) new JSONParser().parse("{}"));
		
		Assert.assertNull(config.getBold());
		Assert.assertNull(config.getItalic());
		Assert.assertNull(config.getUnderline());
		Assert.assertNull(config.getStrikeout());
		Assert.assertNull(config.getColor());
		Assert.assertNull(config.getFontHeight());
		Assert.assertNull(config.getFontHeightInPoints());
		Assert.assertNull(config.getFontName());
		Assert.assertNull(config.getTypeOffset());
		Assert.assertNull(config.getCharSet());
	}

}