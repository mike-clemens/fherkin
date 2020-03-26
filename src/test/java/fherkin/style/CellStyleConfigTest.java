package fherkin.style;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  CellStyleConfig
 * 
 * @author Mike Clemens
 */
public class CellStyleConfigTest extends CellStyleConfig {

	@Test
	public void testPopulate() throws ParseException {
		CellStyleConfig config = new CellStyleConfig();
		config.populate("Feature", (JSONObject) new JSONParser().parse(
				"{" + 
					"\"alignment\": \"Center\"," +
					"\"verticalAlignment\": \"Top\"," +
					"\"indention\": \"10\"," +
					"\"borderTop\": \"Thin\"," +
					"\"borderLeft\": \"Medium\"," +
					"\"borderRight\": \"Dashed\"," +
					"\"borderBottom\": \"Dotted\"," +
					"\"topBorderColor\": \"Black\"," +
					"\"leftBorderColor\": \"Brown\"," +
					"\"rightBorderColor\": \"Olive Green\"," +
					"\"bottomBorderColor\": \"Dark Green\"," +
					"\"fillPattern\": \"Squares\"," +
					"\"fillBackgroundColor\": \"plum\"," +
					"\"fillForegroundColor\": \"pink\"" +
				"}"));
		
		Assert.assertEquals(HorizontalAlignment.CENTER, config.getAlignment());
		Assert.assertEquals(VerticalAlignment.TOP, config.getVerticalAlignment());
		Assert.assertEquals(Short.valueOf("10"), config.getIndention());
		Assert.assertEquals(BorderStyle.THIN, config.getBorderTop());
		Assert.assertEquals(BorderStyle.MEDIUM, config.getBorderLeft());
		Assert.assertEquals(BorderStyle.DASHED, config.getBorderRight());
		Assert.assertEquals(BorderStyle.DOTTED, config.getBorderBottom());
		Assert.assertEquals(Short.valueOf(HSSFColorPredefined.BLACK.getIndex()), config.getTopBorderColor());
		Assert.assertEquals(Short.valueOf(HSSFColorPredefined.BROWN.getIndex()), config.getLeftBorderColor());
		Assert.assertEquals(Short.valueOf(HSSFColorPredefined.OLIVE_GREEN.getIndex()), config.getRightBorderColor());
		Assert.assertEquals(Short.valueOf(HSSFColorPredefined.DARK_GREEN.getIndex()), config.getBottomBorderColor());
		Assert.assertEquals(FillPatternType.SQUARES, config.getFillPattern());
		Assert.assertEquals(Short.valueOf(HSSFColorPredefined.PLUM.getIndex()), config.getFillBackgroundColor());
		Assert.assertEquals(Short.valueOf(HSSFColorPredefined.PINK.getIndex()), config.getFillForegroundColor());
	}

	@Test
	public void testJSONConstructorEmpty() throws ParseException {
		CellStyleConfig config = new CellStyleConfig();
		config.populate("Feature", (JSONObject) new JSONParser().parse("{}"));
		
		Assert.assertNull(config.getAlignment());
		Assert.assertNull(config.getVerticalAlignment());
		Assert.assertNull(config.getIndention());
		Assert.assertNull(config.getBorderTop());
		Assert.assertNull(config.getBorderLeft());
		Assert.assertNull(config.getBorderRight());
		Assert.assertNull(config.getBorderBottom());
		Assert.assertNull(config.getTopBorderColor());
		Assert.assertNull(config.getLeftBorderColor());
		Assert.assertNull(config.getRightBorderColor());
		Assert.assertNull(config.getBottomBorderColor());
		Assert.assertNull(config.getFillPattern());
		Assert.assertNull(config.getFillBackgroundColor());
		Assert.assertNull(config.getFillForegroundColor());
	}

}