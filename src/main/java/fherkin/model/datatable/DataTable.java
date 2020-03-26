package fherkin.model.datatable;

import fherkin.lang.GherkinKeywordType;
import fherkin.model.AbstractGherkinEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class representing a data table.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class DataTable extends AbstractGherkinEntry {
	
	public enum CellType { STRING, BOOLEAN, INTEGER, FLOAT };
	
	private HasDataTable owner;
	private List<DataTableRow> rows = new ArrayList<DataTableRow>();
	
	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.DATA_TABLE;
	}

	@Override
	public String getKeyword() {
		return "data table";
	}
	
	public int getHeight() {
		return rows.size();
	}
	
	public int getWidth() {
		int width = 0;
		for(DataTableRow row : rows)
			if(row.getCells().size() > width)
				width = row.getCells().size();
		return width;
	}
	
	public int getColumnLength(int column) {
		if(column >= getWidth())
			throw new IndexOutOfBoundsException("width = " + getWidth() + ", column = " + column);
		
		int length = 0;
		DataTableCell cell;
		for(DataTableRow row : rows)
			if(column < row.getCells().size()) {
				cell = row.getCells().get(column);
				if(cell.getCellLength() > length)
					length = cell.getCellLength();
			}
		
		return length;
	}
	
	public CellType getColumnType(int column) {
		if(column >= getWidth())
			throw new IndexOutOfBoundsException("width = " + getWidth() + ", column = " + column);
		
		// retrieve all of the cell types in this column
		Set<CellType> set = new HashSet<CellType>();
		boolean first = true;
		DataTableCell cell;
		CellType cellType;
		for(DataTableRow row : rows)
			if(first)
				first = false;
			else
			if(column < row.getCells().size()) {
				cell = row.getCells().get(column);
				cellType = cell.getCellType();
				if(cellType != null)
					set.add(cellType);
			}
		
		// apply the "lowest common denominator" rules to determine the column type
		if(set.size() < 1 || set.contains(CellType.STRING))
			return CellType.STRING;
		if(set.size() == 1 && set.contains(CellType.BOOLEAN))
			return CellType.BOOLEAN;
		if(set.size() == 1 && set.contains(CellType.INTEGER))
			return CellType.INTEGER;
		if(set.contains(CellType.FLOAT) && !set.contains(CellType.BOOLEAN))
			return CellType.FLOAT;
		return CellType.STRING;
	}

	public HasDataTable getOwner() {
		return owner;
	}

	public void setOwner(HasDataTable owner) {
		this.owner = owner;
	}

	public List<DataTableRow> getRows() {
		return rows;
	}

	public void setRows(List<DataTableRow> rows) {
		this.rows = rows;
	}

	public void addRow(DataTableRow row) {
		rows.add(row);
	}

}