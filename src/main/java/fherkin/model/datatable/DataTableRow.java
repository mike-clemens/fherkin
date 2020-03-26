package fherkin.model.datatable;

import fherkin.lang.GherkinKeywordType;
import fherkin.model.AbstractGherkinEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a row in a data table.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class DataTableRow extends AbstractGherkinEntry {
	
	private DataTable dataTable;
	private List<DataTableCell> cells = new ArrayList<DataTableCell>();
	private int rowNumber;
	
	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.DATA_TABLE;
	}

	@Override
	public String getKeyword() {
		return "data table row";
	}

	public DataTable getDataTable() {
		return dataTable;
	}
	
	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}
	
	public List<DataTableCell> getCells() {
		return cells;
	}
	
	public void setCells(List<DataTableCell> cells) {
		this.cells = cells;
	}
	
	public void addCell(DataTableCell cell) {
		cells.add(cell);
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

}