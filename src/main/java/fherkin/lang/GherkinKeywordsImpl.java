package fherkin.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Implementation of the internationalized gherkin keywords interface.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class GherkinKeywordsImpl implements GherkinKeywords {
	
	private Map<String, Set<String>> values;
	private Set<String> keywords;
	
	public GherkinKeywordsImpl(JSONObject language) {
		String key;
		JSONArray array;
		Set<String> valueSet;
		Map<String, Set<String>> values = new HashMap<String, Set<String>>();
		Set<String> keywords = new HashSet<String>();
		for(Object o : language.keySet()) {
			key = (String) o;
			if(language.get(key) instanceof JSONArray) {
				array = (JSONArray) language.get(key);
				
				valueSet = new TreeSet<String>();
				for(Object c : array) {
					valueSet.add(c.toString().trim());
					keywords.add(c.toString().trim());
				}
				
				values.put(key, Collections.synchronizedSet(Collections.unmodifiableSet(valueSet)));
			}
		}
		
		this.values = Collections.synchronizedMap(Collections.unmodifiableMap(values));
		this.keywords = Collections.synchronizedSet(Collections.unmodifiableSet(keywords));
	}
	
	@Override
	public Set<String> getKeywords() {
		return keywords;
	}
	
	@Override
	public Set<String> getFeature() {
		return values.get("feature");
	}

	@Override
	public Set<String> getBackground() {
		return values.get("background");
	}

	@Override
	public Set<String> getScenario() {
		return values.get("scenario");
	}

	@Override
	public Set<String> getScenarioOutline() {
		return values.get("scenarioOutline");
	}

	@Override
	public Set<String> getRule() {
		return values.get("rule");
	}

	@Override
	public Set<String> getGiven() {
		return values.get("given");
	}

	@Override
	public Set<String> getWhen() {
		return values.get("when");
	}

	@Override
	public Set<String> getThen() {
		return values.get("then");
	}

	@Override
	public Set<String> getAnd() {
		return values.get("and");
	}

	@Override
	public Set<String> getBut() {
		return values.get("but");
	}

	@Override
	public Set<String> getExamples() {
		return values.get("examples");
	}

}