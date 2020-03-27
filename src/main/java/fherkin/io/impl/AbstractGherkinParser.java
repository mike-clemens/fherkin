package fherkin.io.impl;

import fherkin.LogHelper;
import fherkin.io.GherkinParser;
import fherkin.io.GherkinSyntaxException;
import fherkin.lang.GherkinKeywordType;
import fherkin.lang.GherkinKeywords;
import fherkin.lang.GherkinKeywordsFactory;
import fherkin.model.GherkinEntry;
import fherkin.model.datatable.DataTable;
import fherkin.model.datatable.DataTableCell;
import fherkin.model.datatable.DataTableRow;
import fherkin.model.datatable.HasDataTable;
import fherkin.model.entry.Background;
import fherkin.model.entry.Comment;
import fherkin.model.entry.Examples;
import fherkin.model.entry.Feature;
import fherkin.model.entry.Scenario;
import fherkin.model.entry.ScenarioOutline;
import fherkin.model.entry.Step;
import fherkin.model.entry.Tag;
import fherkin.model.location.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for a gherkin parser.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public abstract class AbstractGherkinParser implements GherkinParser {
	
	protected Log log = LogFactory.getLog(getClass());
	
	protected GherkinKeywords keywords;
	protected List<GherkinEntry> entries = new ArrayList<GherkinEntry>();
	protected Stack<GherkinEntry> stack = new Stack<GherkinEntry>();
	protected List<Tag> tags;
	
	public AbstractGherkinParser() {
		keywords = GherkinKeywordsFactory.getInstance().getDefaultInstance();
	}
	
	protected void processLine(SortedMap<Location, String> tokens) {
		LogHelper.trace(log, AbstractGherkinParser.class, "processLine");
		
		Pair<Location, String> pair = getLineText(tokens);
		Location start = pair.getFirst();
		String text = pair.getSecond();
		Comment comment = processComment(tokens);
		
		if(text.length() < 1) {
			if(comment != null)
				entries.add(comment);
			
			if(stack.size() < 1 && comment.getText().trim().startsWith("language: ")) {
				String locale = comment.getText().trim().substring("language: ".length()).trim();
				log.debug("Changing locale: " + locale);
				keywords = GherkinKeywordsFactory.getInstance().getInstanceForLocale(locale);
			}
		}
		else
		if(isMatch(text, keywords.getFeature()))
			processFeature(tokens, getMatch(text, keywords.getFeature()), getTokensString(tokens), start, comment);
		else
		if(isMatch(text, keywords.getBackground()))
			processFeatureChild(tokens, getMatch(text, keywords.getBackground()), getTokensString(tokens), start, comment, GherkinKeywordType.BACKGROUND);
		else
		if(isMatch(text, keywords.getScenarioOutline()))
			processFeatureChild(tokens, getMatch(text, keywords.getScenarioOutline()), getTokensString(tokens), start, comment, GherkinKeywordType.SCENARIO_OUTLINE);
		else
		if(isMatch(text, keywords.getScenario()))
			processFeatureChild(tokens, getMatch(text, keywords.getScenario()), getTokensString(tokens), start, comment, GherkinKeywordType.SCENARIO);
		else
		if(isMatch(text, keywords.getRule()))
			processFeatureChild(tokens, getMatch(text, keywords.getRule()), getTokensString(tokens), start, comment, GherkinKeywordType.RULE);
		else
		if(isMatch(text, keywords.getGiven()))
			processStep(tokens, getMatch(text, keywords.getGiven()), getTokensString(tokens), start, comment, GherkinKeywordType.GIVEN);
		else
		if(isMatch(text, keywords.getWhen()))
			processStep(tokens, getMatch(text, keywords.getWhen()), getTokensString(tokens), start, comment, GherkinKeywordType.WHEN);
		else
		if(isMatch(text, keywords.getThen()))
			processStep(tokens, getMatch(text, keywords.getThen()), getTokensString(tokens), start, comment, GherkinKeywordType.THEN);
		else
		if(isMatch(text, keywords.getAnd()))
			processStep(tokens, getMatch(text, keywords.getAnd()), getTokensString(tokens), start, comment, GherkinKeywordType.AND);
		else
		if(isMatch(text, keywords.getBut()))
			processStep(tokens, getMatch(text, keywords.getBut()), getTokensString(tokens), start, comment, GherkinKeywordType.BUT);
		else
		if(isMatch(text, keywords.getExamples()))
			processExamples(tokens, getMatch(text, keywords.getExamples()), getTokensString(tokens), start, comment);
		else
		if(text.startsWith("|"))
			processDataTable(tokens, text, start, comment);
		else
		if(text.startsWith("@"))
			processTags(tokens, text, start, comment);
		else
		
		// if we are still processing the feature, then this could be additional text lines
		if(stack.size() > 0 && stack.peek() instanceof Feature) {
			Feature feature = (Feature) stack.peek();
			feature.addAdditionalText(text.trim());
		}
		else {
			for(char c : text.toCharArray())
				System.out.println("==> " + (int)c);
			throw new GherkinSyntaxException(start, "Unexpected token: " + text);
		}
		
		// TODO blow up here???
	}
	
	protected Pair<Location, String> getLineText(SortedMap<Location, String> tokens) {
		LogHelper.trace(log, AbstractGherkinParser.class, "getLineText", tokens);
		
		StringBuffer buffer = null;
		String value;
		Location start = null;
		for(Map.Entry<Location, String> entry : tokens.entrySet()) {
			value = entry.getValue();
			if(value.trim().length() < 1 && buffer == null)
				;
			else
			if(value.startsWith("#") || value.startsWith(":"))
				break;
			else {
				if(buffer == null) {
					buffer = new StringBuffer();
					start = entry.getKey();
				}
				
				buffer.append(value);
			}
		}
		
		return new Pair<Location, String>(start, buffer == null ? "" : buffer.toString().trim());
	}

	protected boolean isMatch(String text, Set<String> keywords) {
		LogHelper.trace(log, AbstractGherkinParser.class, "isMatch", keywords);
		return getMatch(text, keywords) != null;
	}
	
	protected String getMatch(String text, Set<String> keywords) {
		LogHelper.trace(log, AbstractGherkinParser.class, "getMatch", text, keywords);
		
		for(String keyword : keywords)
			if(text.equals(keyword)
			|| (text.length() > keyword.length() && text.startsWith(keyword) && Character.isWhitespace(text.charAt(keyword.length()))))
				return keyword;
		return null;
	}
	
	protected String getTokensString(SortedMap<Location, String> tokens) {
		LogHelper.trace(log, AbstractGherkinParser.class, "getTokensString", tokens);
		
		StringBuffer buffer = new StringBuffer();
		for(Map.Entry<Location, String> entry : tokens.entrySet())
			if(entry.getValue().trim().length() < 1)
				buffer.append(" ");
			else
			if(entry.getValue().startsWith("#"))
				break;
			else
				buffer.append(entry.getValue());
		
		return buffer.toString().trim();
	}
	
	protected Comment processComment(SortedMap<Location, String> tokens) {
		LogHelper.trace(log, AbstractGherkinParser.class, "processComment", tokens);
		
		Comment comment = null;
		StringBuffer buffer = null;
		for(Map.Entry<Location, String> entry : tokens.entrySet())
			if(entry.getValue().startsWith("#")) {
				if(comment == null) {
					comment = new Comment();
					comment.setLocation(entry.getKey());
					buffer = new StringBuffer();
				}
				
				buffer.append(entry.getValue().substring(1));
			}
			else
			if(buffer != null)
				buffer.append(entry.getValue());
		
		if(comment == null)
			return null;
		
		comment.setText(buffer.toString());
		return comment;
	}
	
	protected void processFeature(SortedMap<Location, String> tokens, String keyword, String text, Location start, Comment comment) {
		LogHelper.trace(log, AbstractGherkinParser.class, "processFeature", tokens, keyword, text, start, comment);
		
		if(!text.startsWith(keyword + ":"))
			throw new GherkinSyntaxException(start, "Expected colon after " + keyword);
		if(stack.size() > 0)
			throw new GherkinSyntaxException(start, keyword + " cannot follow " + stack.peek().getKeyword());
		
		Feature feature = new Feature();
		feature.setKeyword(keyword);
		feature.setText(text.substring(keyword.length() + 1).trim());
		feature.setLocation(start);
		feature.setComment(comment);
		
		if(tags != null && tags.size() > 0) {
			feature.setTags(tags);
			tags = null;
		}
		
		stack.push(feature);
		entries.add(feature);
	}
	
	protected void processFeatureChild(SortedMap<Location, String> tokens, String keyword, String text, Location start, Comment comment, GherkinKeywordType type) {
		LogHelper.trace(log, AbstractGherkinParser.class, "processFeatureChild", tokens, keyword, text, start, type);
		
		if(!text.startsWith(keyword + ":"))
			throw new GherkinSyntaxException(start, "Expected colon after " + keyword);
		if(stack.size() < 1)
			throw new GherkinSyntaxException(start, keyword + " must follow feature declaration");
		
		GherkinEntry parent = stack.peek();
		while(!(parent instanceof Feature) && stack.size() > 0) {
			stack.pop();
			if(stack.size() > 0)
				parent = stack.peek();
		}
		
		if(!(parent instanceof Feature))
			throw new GherkinSyntaxException(start, keyword + " must follow feature declaration; found " + parent.getKeyword() + " instead");
		Feature feature = (Feature) parent;
		
		if(type == GherkinKeywordType.BACKGROUND) {
			if(feature.getBackground() != null)
				throw new GherkinSyntaxException(start, "Only one " + keyword + " entry is permitted");
			if(feature.getScenarios().size() > 0)
				throw new GherkinSyntaxException(start, keyword + " must precede scenarios");
			
			if(tags != null && tags.size() > 0)
				throw new GherkinSyntaxException(tags.get(0).getLocation(), keyword + " cannot have tags associated with it");
			
			Background background = new Background();
			background.setKeyword(keyword);
			background.setText(text.substring(keyword.length() + 1).trim());
			background.setFeature(feature);
			background.setLocation(start);
			background.setComment(comment);
			
			feature.setBackground(background);
			stack.push(background);
			entries.add(background);
		}
		else
		if(type == GherkinKeywordType.SCENARIO_OUTLINE) {
			ScenarioOutline outline = new ScenarioOutline();
			outline.setKeyword(keyword);
			outline.setText(text.substring(keyword.length() + 1).trim());
			outline.setFeature(feature);
			outline.setLocation(start);
			outline.setComment(comment);

			if(tags != null && tags.size() > 0) {
				outline.setTags(tags);
				tags = null;
			}
			
			feature.addScenario(outline);
			stack.push(outline);
			entries.add(outline);
		}
		else {
			Scenario scenario = new Scenario();
			scenario.setKeyword(keyword);
			scenario.setText(text.substring(keyword.length() + 1).trim());
			scenario.setFeature(feature);
			scenario.setLocation(start);
			scenario.setComment(comment);

			if(tags != null && tags.size() > 0) {
				scenario.setTags(tags);
				tags = null;
			}
			
			feature.addScenario(scenario);
			stack.push(scenario);
			entries.add(scenario);
		}
		
		// TODO rule
	}
	
	protected void processStep(SortedMap<Location, String> tokens, String keyword, String text, Location start, Comment comment, GherkinKeywordType type) {
		LogHelper.trace(log, AbstractGherkinParser.class, "processStep", tokens, keyword, text, start, comment, type);
		
		if(text.startsWith(keyword + ":"))
			throw new GherkinSyntaxException(start, "Unexpected colon after " + keyword);
		if(stack.size() < 1)
			throw new GherkinSyntaxException(start, keyword + " must follow background or scenario declaration");
		
		GherkinEntry parent = stack.peek();
		while(!(parent instanceof Scenario) && stack.size() > 0) {
			stack.pop();
			if(stack.size() > 0)
				parent = stack.peek();
		}
		
		if(!(parent instanceof Scenario))
			throw new GherkinSyntaxException(start, keyword + " must follow background or scenario declaration; found " + parent.getKeyword());
		Scenario scenario = (Scenario) parent;
		
		if(tags != null && tags.size() > 0)
			throw new GherkinSyntaxException(tags.get(0).getLocation(), keyword + " cannot have tags associated with it");
		
		Step step = new Step();
		step.setType(type);
		step.setKeyword(keyword);
		step.setText(text.substring(keyword.length() + 1).trim());
		step.setScenario(scenario);
		step.setLocation(start);
		step.setComment(comment);
		
		scenario.addStep(step);
		stack.push(step);
		entries.add(step);
	}
	
	protected void processExamples(SortedMap<Location, String> tokens, String keyword, String text, Location start, Comment comment) {
		LogHelper.trace(log, AbstractGherkinParser.class, "processExamples", tokens, keyword, text, start, comment);
		
		if(!text.startsWith(keyword + ":"))
			throw new GherkinSyntaxException(start, "Expected colon after " + keyword);
		if(stack.size() < 1)
			throw new GherkinSyntaxException(start, keyword + " must follow scenario outline steps");
		if(!text.equals(keyword + ":"))
			throw new GherkinSyntaxException(start, "Additional text is not permitted on " + keyword);
		
		GherkinEntry parent = stack.peek();
		while(!(parent instanceof ScenarioOutline) && stack.size() > 0) {
			stack.pop();
			if(stack.size() > 0)
				parent = stack.peek();
		}
		
		if(!(parent instanceof ScenarioOutline))
			throw new GherkinSyntaxException(start, keyword + " must follow scenario outline steps; found " + parent.getKeyword() + " instead");
		ScenarioOutline outline = (ScenarioOutline) parent;
		
		if(outline.getExamples() != null)
			throw new GherkinSyntaxException(start, "Only one " + keyword + " entry is permitted on scenario outline");
		
		if(tags != null && tags.size() > 0)
			throw new GherkinSyntaxException(tags.get(0).getLocation(), keyword + " cannot have tags associated with it");
		
		Examples examples = new Examples();
		examples.setKeyword(keyword);
		examples.setScenarioOutline(outline);
		examples.setLocation(start);
		examples.setComment(comment);
		outline.setExamples(examples);
		
		stack.push(examples);
		entries.add(examples);
	}
	
	protected void processDataTable(SortedMap<Location, String> tokens, String text, Location start, Comment comment) {
		LogHelper.trace(log, AbstractGherkinParser.class, "processDataTable", tokens, text, start, comment);
		
		if(stack.size() < 1)
			throw new GherkinSyntaxException(start, "Data table must follow examples or step");
		
		GherkinEntry parent = stack.peek();
		while(!(parent instanceof DataTable) && !(parent instanceof HasDataTable) && stack.size() > 0) {
			stack.pop();
			if(stack.size() > 0)
				parent = stack.peek();
		}
		
		if(!(parent instanceof DataTable) && !(parent instanceof HasDataTable))
			throw new GherkinSyntaxException(start, "Data table must follow examples or step; found " + parent.getKeyword() + " instead");
		
		if(tags != null && tags.size() > 0)
			throw new GherkinSyntaxException(tags.get(0).getLocation(), "Data table cannot have tags associated with it");
		
		DataTable dataTable;
		if(parent instanceof DataTable)
			dataTable = (DataTable) parent;
		else {
			HasDataTable owner = (HasDataTable) parent;
			dataTable = new DataTable();
			dataTable.setLocation(start);
			dataTable.setOwner(owner);
			owner.setDataTable(dataTable);
			
			stack.push(dataTable);
		}
		
		DataTableRow row = new DataTableRow();
		row.setLocation(start);
		row.setDataTable(dataTable);
		row.setRowNumber(dataTable.getHeight());
		row.setComment(comment);
		dataTable.addRow(row);
		entries.add(row);
		
		DataTableCell cell = null;
		StringBuffer buffer = null;
		Location textStart = null;
		for(Map.Entry<Location, String> entry : tokens.entrySet()) {
			if(entry.getValue().equals("|")) {
				if(cell != null) {
					cell.setValue(buffer.toString().trim());
					row.addCell(cell);
				}
				
				cell = new DataTableCell();
				cell.setLocation(entry.getKey());
				cell.setRow(row);
				
				buffer = new StringBuffer();
				textStart = entry.getKey();
			}
			else
			if(buffer != null)
				buffer.append(entry.getValue());
		}
		
		if(buffer != null) {
			String tail = buffer.toString().trim();
			int index = tail.indexOf('#');
			if(index > -1)
				tail = tail.substring(0, index).trim();
			if(tail.length() > 0)
				throw new GherkinSyntaxException(textStart == null ? start : textStart, "Unexpected trailing characters on data table row");
		}
	}
	
	protected void processTags(SortedMap<Location, String> tokens, String text, Location start, Comment comment) {
		LogHelper.trace(log, AbstractGherkinParser.class, "processTags", tokens, text, start, comment);
		
		if(stack.size() > 0) {
			GherkinEntry parent = stack.peek();
			while(!(parent instanceof Feature) && stack.size() > 0) {
				stack.pop();
				if(stack.size() > 0)
					parent = stack.peek();
			}
			
			if(!(parent instanceof Feature))
				throw new GherkinSyntaxException(start, "Unexpected location for tag; expected to precede feature or scenario declarations");
		}
		
		if(tags == null)
			tags = new ArrayList<Tag>();
		
		Tag tag = null;
		StringBuffer buffer = null;
		for(Map.Entry<Location, String> entry : tokens.entrySet())
			if(entry.getValue().trim().length() < 1) {
				if(tag != null) {
					tag.setTag(buffer.toString());
					validateTagName(tag);
					tags.add(tag);
				}
				tag = null;
			}
			else
			if(entry.getValue().equals("@")) {
				if(tag != null)
					throw new GherkinSyntaxException(entry.getKey(), "Expected tag name; found @");
				tag = new Tag();
				tag.setLocation(entry.getKey());
				buffer = new StringBuffer();
			}
			else
			if(entry.getValue().trim().startsWith("@")) {
				buffer = null;
				for(char c : entry.getValue().toCharArray()) {
					if(c == '@') {
						tag = new Tag();
						tag.setLocation(entry.getKey());
						buffer = new StringBuffer();
					}
					else
					if(Character.isWhitespace(c)) {
						if(tag != null) {
							tag.setTag(buffer.toString());
							validateTagName(tag);
							tags.add(tag);
						}
						tag = null;
						buffer = null;
					}
					else
					if(tag != null)
						buffer.append(c);
					else
						throw new GherkinSyntaxException(entry.getKey(), "Expected tag; found " + entry.getValue().trim());
				}
				
				if(tag != null) {
					tag.setTag(buffer.toString());
					validateTagName(tag);
					tags.add(tag);
				}
				tag = null;
				buffer = null;
			}
			else
			if(tag != null)
				buffer.append(entry.getValue());
			else
				throw new GherkinSyntaxException(entry.getKey(), "Expected tag; found " + entry.getValue());
		
		if(tag != null) {
			tag.setTag(buffer.toString());
			validateTagName(tag);
			tags.add(tag);
		}
	}
	
	protected void validateTagName(Tag tag) {
		LogHelper.trace(log, AbstractGherkinParser.class, "validateTagName", tag);
		
		for(char c : tag.getTag().toCharArray())
			if(!Character.isLetter(c)
			&& !Character.isDigit(c)
			&& c != '_')
				throw new GherkinSyntaxException(tag.getLocation(), "Invalid character in tag name: " + c);
	}
	
}