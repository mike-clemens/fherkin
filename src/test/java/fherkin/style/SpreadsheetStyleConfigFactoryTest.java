package fherkin.style;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  SpreadsheetStyleConfigFactory
 * 
 * @author Mike Clemens
 */
public class SpreadsheetStyleConfigFactoryTest {
	
	@Test
	public void testDefaultInstance() {
		SpreadsheetStyleConfig config = SpreadsheetStyleConfigFactory.getInstance().getStyleConfig();
		
		CellStyleConfig cellStyle = config.getFeatureCellStyle();
		Assert.assertNotNull(cellStyle);
		Assert.assertEquals(HorizontalAlignment.RIGHT, cellStyle.getAlignment());
		Assert.assertEquals(new Short((short) 1), cellStyle.getIndention());
		
		FontConfig font = config.getFeatureFont();
		Assert.assertNotNull(font);
		Assert.assertTrue(font.getBold());
		Assert.assertEquals(new Short(HSSFColorPredefined.DARK_BLUE.getIndex()), font.getColor());
		Assert.assertEquals(new Short((short) 14), font.getFontHeightInPoints());
	}

}