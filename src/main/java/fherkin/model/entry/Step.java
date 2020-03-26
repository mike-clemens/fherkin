package fherkin.model.entry;

import fherkin.lang.GherkinKeywordType;
import fherkin.model.AbstractGherkinEntry;
import fherkin.model.datatable.DataTable;
import fherkin.model.datatable.HasDataTable;

/**
 * Class representing a step in a gherkin file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Step extends AbstractGherkinEntry implements HasDataTable {
	
	private Scenario scenario;
	private GherkinKeywordType type;
	private String keyword;
	private String text;
	private DataTable dataTable;
	
	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	@Override
	public GherkinKeywordType getType() {
		return type;
	}

	public void setType(GherkinKeywordType type) {
		this.type = type;
	}

	@Override
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public DataTable getDataTable() {
		return dataTable;
	}
	
	@Override
	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

}