package fherkin.style;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  SpreadsheetStyleConfigImpl
 * 
 * @author Mike Clemens
 */
public class SpreadsheetStyleConfigImplTest extends SpreadsheetStyleConfigImpl {
	
	private Log log = LogFactory.getLog(getClass());
	
	///// processCellStyles
	
	@Test
	public void testProcessCellStyles() throws Exception {
		Map<String, CellStyleConfig> map = processCellStyles((JSONObject) new JSONParser().parse(
				"{" +
					"\"abc\": {" +
						"\"alignment\": \"center\"," +
						"\"verticalAlignment\": \"top\"" +
					"}" +
				"}"));
		
		Assert.assertEquals(1, map.size());
		Assert.assertTrue(map.containsKey("abc"));
		
		CellStyleConfig abc = map.get("abc");
		Assert.assertEquals(HorizontalAlignment.CENTER, abc.getAlignment());
		Assert.assertEquals(VerticalAlignment.TOP, abc.getVerticalAlignment());
	}
	
	@Test
	public void testProcessCellStylesExtends() throws Exception {
		Map<String, CellStyleConfig> map = processCellStyles((JSONObject) new JSONParser().parse(
				"{" +
					"\"abc\": {" +
						"\"alignment\": \"center\"," +
						"\"verticalAlignment\": \"top\"" +
					"}," +
					"\"def\": {" +
						"\"extends\": \"abc\"," +
						"\"verticalAlignment\": \"bottom\"" +
					"}" +
				"}"));
		
		Assert.assertEquals(2, map.size());
		Assert.assertTrue(map.containsKey("abc"));
		Assert.assertTrue(map.containsKey("def"));
		
		CellStyleConfig abc = map.get("abc");
		Assert.assertEquals(HorizontalAlignment.CENTER, abc.getAlignment());
		Assert.assertEquals(VerticalAlignment.TOP, abc.getVerticalAlignment());
		
		CellStyleConfig def = map.get("def");
		Assert.assertEquals(HorizontalAlignment.CENTER, def.getAlignment());
		Assert.assertEquals(VerticalAlignment.BOTTOM, def.getVerticalAlignment());
	}
	
	@Test
	public void testProcessCellStylesCircularDependency() throws Exception {
		try {
			processCellStyles((JSONObject) new JSONParser().parse(
					"{" +
						"\"abc\": {" +
							"\"extends\": \"def\"" +
						"}," +
						"\"def\": {" +
							"\"extends\": \"abc\"" +
						"}," +
						"\"xyz\": {" +
							"\"alignment\": \"center\"" +
						"}," +
					"}"));
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}
	
	///// processFonts
	
	@Test
	public void testProcessFonts() throws Exception {
		Map<String, FontConfig> map = processFonts((JSONObject) new JSONParser().parse(
				"{" +
					"\"abc\": {" +
						"\"bold\": \"true\"," +
						"\"italic\": \"false\"" +
					"}" +
				"}"));
		
		Assert.assertEquals(1, map.size());
		Assert.assertTrue(map.containsKey("abc"));
		
		FontConfig abc = map.get("abc");
		Assert.assertTrue(abc.getBold());
		Assert.assertFalse(abc.getItalic());
	}
	
	@Test
	public void testProcessFontsExtends() throws Exception {
		Map<String, FontConfig> map = processFonts((JSONObject) new JSONParser().parse(
				"{" +
					"\"abc\": {" +
						"\"bold\": \"true\"," +
						"\"italic\": \"false\"" +
					"}," +
					"\"def\": {" +
						"\"extends\": \"abc\"," +
						"\"italic\": \"true\"" +
					"}" +
				"}"));
		
		Assert.assertEquals(2, map.size());
		Assert.assertTrue(map.containsKey("abc"));
		Assert.assertTrue(map.containsKey("def"));
		
		FontConfig abc = map.get("abc");
		Assert.assertTrue(abc.getBold());
		Assert.assertFalse(abc.getItalic());
		
		FontConfig def = map.get("def");
		Assert.assertTrue(def.getBold());
		Assert.assertTrue(def.getItalic());
	}
	
	@Test
	public void testProcessFontsCircularDependency() throws Exception {
		try {
			processFonts((JSONObject) new JSONParser().parse(
					"{" +
						"\"abc\": {" +
							"\"extends\": \"def\"" +
						"}," +
						"\"def\": {" +
							"\"extends\": \"abc\"" +
						"}," +
						"\"xyz\": {" +
							"\"bold\": \"true\"" +
						"}," +
					"}"));
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}
	
	///// getObjectMap
	
	@Test
	public void testGetObjectMap() throws Exception {
		Map<String, JSONObject> map = getObjectMap("cellStyles", (JSONObject) new JSONParser().parse(
					"{" +
						"\"abc\": {}," +
						"\"def\": {" +
							"\"extends\": \"abc\"" +
						"}," +
						"\"ghi\": {}" +
					"}"));
		
		Assert.assertEquals(3, map.size());
		Assert.assertTrue(map.containsKey("abc"));
		Assert.assertTrue(map.containsKey("def"));
		Assert.assertTrue(map.containsKey("ghi"));
	}
	
	@Test
	public void testGetObjectMapNotJSONObject() throws Exception {
		try {
			getObjectMap("cellStyles", (JSONObject) new JSONParser().parse("{\"xyz\": []}"));
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}

	@Test
	public void testGetObjectMapExtendsNotFound() throws Exception {
		try {
			getObjectMap("cellStyles", (JSONObject) new JSONParser().parse(
					"{" +
						"\"abc\": {}," +
						"\"xyz\": {" +
							"\"extends\": \"qrs\"" +
						"}" +
					"}"));
			Assert.fail("Expected SpreadsheetStyleConfigException");
		}
		catch(SpreadsheetStyleConfigException e) {
			// do nothing here; the test case passed
			log.debug(e.getMessage());
		}
	}

}