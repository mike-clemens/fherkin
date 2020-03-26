package fherkin.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Factory to create internationalized GherkinKeywords instances.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class GherkinKeywordsFactory {
	
	private static final GherkinKeywordsFactory INSTANCE = new GherkinKeywordsFactory();
	
	private Log log = LogFactory.getLog(getClass());
	private Map<String, GherkinKeywords> instances = Collections.synchronizedMap(new HashMap<String, GherkinKeywords>());
	private JSONObject root;
	
	protected GherkinKeywordsFactory() {
		try {
			root = parseGherkinLanguagesJSON();
		}
		catch(RuntimeException e) {
			log.error(e.getClass().getSimpleName() + " caught:", e);
			throw e;
		}
		catch(Exception e) {
			log.error(e.getClass().getSimpleName() + " caught:", e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static GherkinKeywordsFactory getInstance() {
		return INSTANCE;
	}
	
	public GherkinKeywords getDefaultInstance() {
		return getInstanceForLocale("en");
	}
	
	public GherkinKeywords getInstanceForLocale(String locale) {
		if(locale == null)
			throw new IllegalArgumentException("locale cannot be null");
		if(locale.length() < 1)
			throw new IllegalArgumentException("locale cannot be blank");
		
		return getOrCreateInstance(locale);
	}

	protected JSONObject parseGherkinLanguagesJSON() throws IOException, ParseException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
			classLoader = getClass().getClassLoader();
		
		InputStream inputStream = classLoader.getResourceAsStream(getClass().getPackage().getName().replaceAll("\\.", "/") + "/gherkin-languages.json");
		try {
			return (JSONObject) new JSONParser().parse(new InputStreamReader(inputStream, "utf8"));
		}
		finally {
			inputStream.close();
		}
	}
	
	protected synchronized GherkinKeywords getOrCreateInstance(String locale) {
		GherkinKeywords instance = instances.get(locale);
		if(instance != null)
			return instance;
		
		JSONObject o = null;
		String key = locale;
		int index;
		while(o == null) {
			o = (JSONObject) root.get(key);
			if(o == null) {
				index = key.lastIndexOf('-');
				if(index < 0)
					throw new IllegalArgumentException("No locale available for " + locale);
				key = key.substring(0, index);
			}
		}
		
		instance = new GherkinKeywordsImpl(o);
		instances.put(locale, instance);
		return instance;
	}
	
	protected JSONObject findByLocale(JSONObject root, String locale) {
		JSONObject o = (JSONObject) root.get(locale.replaceAll("\\_", "-"));
		if(o != null)
			return o;
		
		if(locale.length() < 1)
			return (JSONObject) root.get("en");
		
		int index = locale.lastIndexOf('_');
		return findByLocale(root, index < 0 ? "" : locale.substring(0, index));
	}

}