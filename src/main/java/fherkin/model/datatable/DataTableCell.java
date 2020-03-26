package fherkin.model.datatable;

import fherkin.lang.GherkinKeywordType;
import fherkin.model.AbstractGherkinEntry;
import fherkin.model.datatable.DataTable.CellType;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Class representing a cell in a data table.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class DataTableCell extends AbstractGherkinEntry {
	
	private DataTableRow row;
	private String value;
	
	@Override
	public GherkinKeywordType getType() {
		return GherkinKeywordType.DATA_TABLE;
	}

	@Override
	public String getKeyword() {
		return "data table cell";
	}
	
	public int getCellLength() {
		return value == null ? 0 : value.trim().length();
	}
	
	public CellType getCellType() {
		if(value == null)
			return null;
		value = value.trim();
		if(value.length() < 1)
			return null;
		
		if(value.equalsIgnoreCase(Boolean.TRUE.toString())
		|| value.equalsIgnoreCase(Boolean.FALSE.toString()))
			return CellType.BOOLEAN;
		
		try {
			new BigInteger(value);
			return CellType.INTEGER;
		}
		catch(NumberFormatException e) {
			// do nothing here
		}
		
		try {
			new BigDecimal(value);
			return CellType.FLOAT;
		}
		catch(NumberFormatException e) {
			// do nothing here
		}
		
		return CellType.STRING;
	}
	
	public DataTableRow getRow() {
		return row;
	}
	
	public void setRow(DataTableRow row) {
		this.row = row;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}