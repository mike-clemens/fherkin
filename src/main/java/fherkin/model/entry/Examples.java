package fherkin.model.entry;

import fherkin.lang.GherkinKeywordType;
import fherkin.model.AbstractGherkinEntry;
import fherkin.model.datatable.DataTable;
import fherkin.model.datatable.HasDataTable;

/**
 * Class representing an examples entry in a gherkin file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Examples extends AbstractGherkinEntry implements HasDataTable {
	
	private ScenarioOutline scenarioOutline;
	private String keyword;
	private DataTable dataTable;

	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.EXAMPLES;
	}

	public ScenarioOutline getScenarioOutline() {
		return scenarioOutline;
	}

	public void setScenarioOutline(ScenarioOutline scenarioOutline) {
		this.scenarioOutline = scenarioOutline;
	}

	@Override
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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