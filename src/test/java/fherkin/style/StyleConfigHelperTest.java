package fherkin.style;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  StyleConfigHelper
 * 
 * @author Mike Clemens
 */
public class StyleConfigHelperTest extends StyleConfigHelper {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Test
	public void testToBoolean() {
		Assert.assertNull(toBoolean("a", "b", null));
		Assert.assertNull(toBoolean("a", "b", ""));
		
		Assert.assertTrue(toBoolean("a", "b", "true"));
		Assert.assertTrue(toBoolean("a", "b", "yes"));
		Assert.assertTrue(toBoolean("a", "b", "on"));
		Assert.assertTrue(toBoolean("a", "b", "TRUE"));
		Assert.assertTrue(toBoolean("a", "b", "YES"));
		Assert.assertTrue(toBoolean("a", "b", "ON"));
		
		Assert.assertFalse(toBoolean("a", "b", "false"));
		Assert.assertFalse(toBoolean("a", "b", "no"));
		Assert.assertFalse(toBoolean("a", "b", "off"));
		Assert.assertFalse(toBoolean("a", "b", "FALSE"));
		Assert.assertFalse(toBoolean("a", "b", "NO"));
		Assert.assertFalse(toBoolean("a", "b", "OFF"));
		
		try {
			toBoolean("a", "b", "xyz");
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testToByte() {
		Assert.assertNull(toByte("a", "b", null));
		Assert.assertNull(toByte("a", "b", ""));
		Assert.assertEquals(Byte.valueOf("123"), toByte("a", "b", "123"));
		
		try {
			toByte("a", "b", "xyz");
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testToShort() {
		Assert.assertNull(toShort("a", "b", null));
		Assert.assertNull(toShort("a", "b", ""));
		Assert.assertEquals(Short.valueOf("123"), toShort("a", "b", "123"));
		
		try {
			toShort("a", "b", "xyz");
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}

	@Test
	public void testToInteger() {
		Assert.assertNull(toInteger("a", "b", null));
		Assert.assertNull(toInteger("a", "b", ""));
		Assert.assertEquals(Integer.valueOf("123"), toInteger("a", "b", "123"));
		
		try {
			toInteger("a", "b", "xyz");
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testToColor() {
		Assert.assertNull(toColor("a", "b", null));
		Assert.assertNull(toColor("a", "b", ""));
		Assert.assertEquals(Short.valueOf("123"), toColor("a", "b", "123"));
		Assert.assertEquals(HSSFColorPredefined.AQUA.getIndex(), toColor("a", "b", "aqua").shortValue());
		Assert.assertEquals(HSSFColorPredefined.CORNFLOWER_BLUE.getIndex(), toColor("a", "b", "CoRnFlOwEr BlUe").shortValue());
		
		try {
			toShort("a", "b", "that brown m&m's color");
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testToHorizontalAlignment() {
		Assert.assertNull(toHorizontalAlignment("a", "b", null));
		Assert.assertNull(toHorizontalAlignment("a", "b", ""));
		Assert.assertEquals(HorizontalAlignment.CENTER, toHorizontalAlignment("a", "b", "Center"));
		Assert.assertEquals(HorizontalAlignment.CENTER_SELECTION, toHorizontalAlignment("a", "b", "Center Selection"));
		
		try {
			toHorizontalAlignment("a", "b", "xyz");
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}

	@Test
	public void testToVerticalAlignment() {
		Assert.assertNull(toVerticalAlignment("a", "b", null));
		Assert.assertNull(toVerticalAlignment("a", "b", ""));
		Assert.assertEquals(VerticalAlignment.JUSTIFY, toVerticalAlignment("a", "b", "Justify"));
		
		try {
			toVerticalAlignment("a", "b", "xyz");
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}

	@Test
	public void testToBorderStyle() {
		Assert.assertNull(toBorderStyle("a", "b", null));
		Assert.assertNull(toBorderStyle("a", "b", ""));
		Assert.assertEquals(BorderStyle.DASHED, toBorderStyle("a", "b", "Dashed"));
		Assert.assertEquals(BorderStyle.DASH_DOT_DOT, toBorderStyle("a", "b", "Dash Dot Dot"));
		
		try {
			toBorderStyle("a", "b", "xyz");
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}

	@Test
	public void testToFillPatternType() {
		Assert.assertNull(toFillPatternType("a", "b", null));
		Assert.assertNull(toFillPatternType("a", "b", ""));
		Assert.assertEquals(FillPatternType.BRICKS, toFillPatternType("a", "b", "Bricks"));
		Assert.assertEquals(FillPatternType.BIG_SPOTS, toFillPatternType("a", "b", "Big Spots"));
		
		try {
			toFillPatternType("a", "b", "xyz");
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}

}