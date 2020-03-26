package fherkin.style;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 * Implementation class for the configuration of spreadsheet styles.
 *  
 * @author Mike Clemens
 * @since 1.0.0
 */
public class SpreadsheetStyleConfigImpl implements SpreadsheetStyleConfig {

	private Map<String, CellStyleConfig> cellStyles;
	private Map<String, FontConfig> fonts;
	
	public SpreadsheetStyleConfigImpl(JSONObject root) {
		init(root);
	}
	
	protected SpreadsheetStyleConfigImpl() {
		// for unit testing
	}
	
	@Override
	public CellStyleConfig getFeatureCellStyle() {
		return cellStyles.get("feature");
	}
	
	@Override
	public CellStyleConfig getFeatureTextCellStyle() {
		return cellStyles.get("featureText");
	}
	
	@Override
	public CellStyleConfig getBackgroundCellStyle() {
		return cellStyles.get("background");
	}
	
	@Override
	public CellStyleConfig getScenarioCellStyle() {
		return cellStyles.get("scenario");
	}
	
	@Override
	public CellStyleConfig getScenarioOutlineCellStyle() {
		return cellStyles.get("scenarioOutline");
	}
	
	@Override
	public CellStyleConfig getBackgroundTextCellStyle() {
		return cellStyles.get("backgroundText");
	}
	
	@Override
	public CellStyleConfig getScenarioTextCellStyle() {
		return cellStyles.get("scenarioText");
	}
	
	@Override
	public CellStyleConfig getScenarioOutlineTextCellStyle() {
		return cellStyles.get("scenarioOutlineText");
	}
	
	@Override
	public CellStyleConfig getGivenCellStyle() {
		return cellStyles.get("given");
	}
	
	@Override
	public CellStyleConfig getWhenCellStyle() {
		return cellStyles.get("when");
	}
	
	@Override
	public CellStyleConfig getThenCellStyle() {
		return cellStyles.get("then");
	}
	
	@Override
	public CellStyleConfig getAndCellStyle() {
		return cellStyles.get("and");
	}
	
	@Override
	public CellStyleConfig getButCellStyle() {
		return cellStyles.get("but");
	}
	
	@Override
	public CellStyleConfig getGivenTextCellStyle() {
		return cellStyles.get("givenText");
	}
	
	@Override
	public CellStyleConfig getWhenTextCellStyle() {
		return cellStyles.get("whenText");
	}
	
	@Override
	public CellStyleConfig getThenTextCellStyle() {
		return cellStyles.get("thenText");
	}
	
	@Override
	public CellStyleConfig getAndTextCellStyle() {
		return cellStyles.get("andText");
	}
	
	@Override
	public CellStyleConfig getButTextCellStyle() {
		return cellStyles.get("butText");
	}
	
	@Override
	public CellStyleConfig getExamplesCellStyle() {
		return cellStyles.get("examples");
	}
	
	@Override
	public CellStyleConfig getTagCellStyle() {
		return cellStyles.get("tag");
	}
	
	@Override
	public CellStyleConfig getDataTableHeadingCellStyleAlignLeft() {
		return cellStyles.get("dataTableHeadingAlignLeft");
	}
	
	@Override
	public CellStyleConfig getDataTableHeadingCellStyleAlignRight() {
		return cellStyles.get("dataTableHeadingAlignRight");
	}
	
	@Override
	public CellStyleConfig getDataTableRowCellStyleAlignLeft() {
		return cellStyles.get("dataTableCellAlignLeft");
	}
	
	@Override
	public CellStyleConfig getDataTableRowCellStyleAlignRight() {
		return cellStyles.get("dataTableCellAlignRight");
	}
	
	@Override
	public CellStyleConfig getCommentCellStyle() {
		return cellStyles.get("comment");
	}
	
	///// fonts
	
	@Override
	public FontConfig getFeatureFont() {
		return fonts.get("feature");
	}
	
	@Override
	public FontConfig getFeatureTextFont() {
		return fonts.get("featureText");
	}
	
	@Override
	public FontConfig getBackgroundFont() {
		return fonts.get("background");
	}
	
	@Override
	public FontConfig getScenarioFont() {
		return fonts.get("scenario");
	}
	
	@Override
	public FontConfig getScenarioOutlineFont() {
		return fonts.get("scenarioOutline");
	}
	
	@Override
	public FontConfig getBackgroundTextFont() {
		return fonts.get("backgroundText");
	}
	
	@Override
	public FontConfig getScenarioTextFont() {
		return fonts.get("scenarioText");
	}
	
	@Override
	public FontConfig getScenarioOutlineTextFont() {
		return fonts.get("scenarioOutlineText");
	}
	
	@Override
	public FontConfig getGivenFont() {
		return fonts.get("given");
	}
	
	@Override
	public FontConfig getWhenFont() {
		return fonts.get("when");
	}
	
	@Override
	public FontConfig getThenFont() {
		return fonts.get("then");
	}
	
	@Override
	public FontConfig getAndFont() {
		return fonts.get("and");
	}
	
	@Override
	public FontConfig getButFont() {
		return fonts.get("but");
	}
	
	@Override
	public FontConfig getGivenTextFont() {
		return fonts.get("givenText");
	}
	
	@Override
	public FontConfig getWhenTextFont() {
		return fonts.get("whenText");
	}
	
	@Override
	public FontConfig getThenTextFont() {
		return fonts.get("thenText");
	}
	
	@Override
	public FontConfig getAndTextFont() {
		return fonts.get("andText");
	}
	
	@Override
	public FontConfig getButTextFont() {
		return fonts.get("butText");
	}
	
	@Override
	public FontConfig getGivenTextVariableFont() {
		return fonts.get("givenTextVariable");
	}
	
	@Override
	public FontConfig getWhenTextVariableFont() {
		return fonts.get("whenTextVariable");
	}
	
	@Override
	public FontConfig getThenTextVariableFont() {
		return fonts.get("thenTextVariable");
	}
	
	@Override
	public FontConfig getAndTextVariableFont() {
		return fonts.get("andTextVariable");
	}
	
	@Override
	public FontConfig getButTextVariableFont() {
		return fonts.get("butTextVariable");
	}
	
	@Override
	public FontConfig getExamplesFont() {
		return fonts.get("examples");
	}
	
	@Override
	public FontConfig getTagFont() {
		return fonts.get("tag");
	}
	
	@Override
	public FontConfig getDataTableHeadingFontAlignLeft() {
		return fonts.get("dataTableHeadingAlignLeft");
	}
	
	@Override
	public FontConfig getDataTableHeadingFontAlignRight() {
		return fonts.get("dataTableHeadingAlignRight");
	}
	
	@Override
	public FontConfig getDataTableRowFontAlignLeft() {
		return fonts.get("dataTableRowAlignLeft");
	}
	
	@Override
	public FontConfig getDataTableRowFontAlignRight() {
		return fonts.get("dataTableRowAlignRight");
	}
	
	@Override
	public FontConfig getCommentFont() {
		return fonts.get("comment");
	}
	
	///// initialize
	
	protected void init(JSONObject root) {
		JSONObject cellStylesParent = (JSONObject) root.get("cellStyles");
		if(cellStylesParent != null)
			cellStyles = processCellStyles(cellStylesParent);
		
		JSONObject fontsParent = (JSONObject) root.get("fonts");
		if(fontsParent != null)
			fonts = processFonts(fontsParent);
	}
	
	protected Map<String, CellStyleConfig> processCellStyles(JSONObject object) {
		Map<String, CellStyleConfig> map = new HashMap<String, CellStyleConfig>();
		Map<String, JSONObject> objects = getObjectMap("cellStyles", object);
		if(objects != null) {
			Map<String, JSONObject> next;
			String extendsString;
			CellStyleConfig config;
			while(objects.size() > 0) {
				next = new HashMap<String, JSONObject>();
				for(Map.Entry<String, JSONObject> entry : objects.entrySet()) {
					extendsString = entry.getValue().containsKey("extends") ? entry.getValue().get("extends").toString() : null;
					
					if(extendsString == null || map.containsKey(extendsString)) {
						config = extendsString == null ? new CellStyleConfig() : map.get(extendsString).clone();
						config.populate(entry.getKey(), entry.getValue());
						map.put(entry.getKey(), config);
					}
					else
						next.put(entry.getKey(), entry.getValue());
				}
				
				if(next.size() >= objects.size())
					throw new SpreadsheetStyleConfigException("Recursive extends found in cellStyles config");
				objects = next;
			}
		}
		
		return map;
	}
	
	protected Map<String, FontConfig> processFonts(JSONObject object) {
		Map<String, FontConfig> map = new HashMap<String, FontConfig>();
		Map<String, JSONObject> objects = getObjectMap("fonts", object);
		if(objects != null) {
			Map<String, JSONObject> next;
			String extendsString;
			FontConfig config;
			while(objects.size() > 0) {
				next = new HashMap<String, JSONObject>();
				for(Map.Entry<String, JSONObject> entry : objects.entrySet()) {
					extendsString = entry.getValue().containsKey("extends") ? entry.getValue().get("extends").toString() : null;
					
					if(extendsString == null || map.containsKey(extendsString)) {
						config = extendsString == null ? new FontConfig() : map.get(extendsString).clone();
						config.populate(entry.getKey(), entry.getValue());
						map.put(entry.getKey(), config);
					}
					else
						next.put(entry.getKey(), entry.getValue());
				}
				
				if(next.size() >= objects.size())
					throw new SpreadsheetStyleConfigException("Recursive extends found in fonts config");
				objects = next;
			}
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, JSONObject> getObjectMap(String parentName, JSONObject object) {
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		Map.Entry<String, Object> entry;
		JSONObject child;
		String extendsString;
		for(Object entryObject : object.entrySet()) {
			entry = (Map.Entry<String, Object>) entryObject;
			if(!(entry.getValue() instanceof JSONObject))
				throw new SpreadsheetStyleConfigException("Invalid " + parentName + " entry: " + entry.getKey() + "; expected JSONObject, found " + entry.getValue().getClass().getSimpleName());
			
			child = (JSONObject) entry.getValue();
			if(child.containsKey("extends")) {
				extendsString = child.get("extends").toString();
				if(!object.containsKey(extendsString))
					throw new SpreadsheetStyleConfigException("Invalid " + parentName + " entry: " + entry.getKey() + "; undefined extends value: " + extendsString);
			}
			
			if(map.containsKey(entry.getKey()))
				throw new SpreadsheetStyleConfigException("Invalid " + parentName + " entry: " + entry.getKey() + "; non-unique key");
			
			map.put(entry.getKey(), child);
		}
		
		return map;
	}		
		
}