package fherkin.style;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Factory for creating spreadsheet style configuration instances.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class SpreadsheetStyleConfigFactory {
	
	private static final SpreadsheetStyleConfigFactory INSTANCE = new SpreadsheetStyleConfigFactory();
	
	private Log log = LogFactory.getLog(getClass());
	private SpreadsheetStyleConfig styleConfig;
	
	private SpreadsheetStyleConfigFactory() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
			classLoader = getClass().getClassLoader();
		
		try {
			styleConfig = createConfig(classLoader.getResourceAsStream(getClass().getPackage().getName().replaceAll("\\.", "/") + "/defaultStyles.json"));
		}
		catch(IOException e) {
			log.error(e.getClass().getSimpleName() + " caught:", e);
			throw new SpreadsheetStyleConfigException(e);
		}
	}
	
	public static SpreadsheetStyleConfigFactory getInstance() {
		return INSTANCE;
	}
	
	public SpreadsheetStyleConfig getStyleConfig() {
		return styleConfig;
	}
	
	public void setConfigFile(File configFile) throws IOException {
		createConfig(new FileInputStream(configFile));
	}
	
	protected SpreadsheetStyleConfig createConfig(InputStream inputStream) throws IOException {
		try {
			JSONObject root = parse(inputStream);
			return new SpreadsheetStyleConfigImpl(root);
		}
		finally {
			inputStream.close();
		}
	}
	
	protected JSONObject parse(InputStream inputStream) {
		try {
			return (JSONObject) new JSONParser().parse(new InputStreamReader(inputStream));
		}
		catch(IOException e) {
			log.error(e.getClass().getSimpleName() + " caught:", e);
			throw new SpreadsheetStyleConfigException(e);
		}
		catch(ParseException e) {
			log.error(e.getClass().getSimpleName() + " caught:", e);
			throw new SpreadsheetStyleConfigException(e);
		}
	}

}