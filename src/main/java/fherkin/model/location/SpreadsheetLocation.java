package fherkin.model.location;

/**
 * Location implementation representing a cell in a spreadsheet.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class SpreadsheetLocation implements Location, Comparable<SpreadsheetLocation> {
	
	private String filename;
	private String sheet;
	private int row;
	private int column;
	private String columnName;
	private int position;
	
	@Override
	public String getLocation() {
		return getCellLocation() + (position > 0 && position < Integer.MAX_VALUE ? ":" + position : "");
	}
	
	@Override
	public int compareTo(SpreadsheetLocation o) {
		int compare = filename.compareTo(o.filename);
		if(compare != 0)
			return compare;
		
		compare = sheet.compareTo(o.sheet);
		if(compare != 0)
			return compare;
		
		if(row < o.row)
			return -1;
		if(row > o.row)
			return 1;
		
		if(column < o.column)
			return -1;
		if(column > o.column)
			return 1;
		
		if(position < o.position)
			return -1;
		if(position > o.position)
			return 1;
		return 0;
	}
	
	@Override
	public int hashCode() {
		return (getCellLocation() + ":" + position).hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null || !(o instanceof SpreadsheetLocation))
			return false;
		
		SpreadsheetLocation other = (SpreadsheetLocation) o;
		return filename.equals(other.filename)
			&& sheet.equals(other.sheet)
			&& row == other.row
			&& column == other.column
			&& position == other.position;
	}
	
	public SpreadsheetLocation clone() {
		SpreadsheetLocation clone = new SpreadsheetLocation();
		clone.filename = filename;
		clone.sheet = sheet;
		clone.row = row;
		clone.column = column;
		clone.columnName = columnName;
		clone.position = position;
		return clone;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSheet() {
		return sheet;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
		
		// convert the column number into a column name
		int i1 = column / 26;
		int i2 = column % 26;
		columnName = i1 > 0 ? Character.toString((char)('A' + i1 - 1)) + Character.toString((char)('A' + i2)) : Character.toString((char)('A' + i2));
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public void setPositionBefore() {
		position = -1;
	}
	
	public void setPositionAfter() {
		position = Integer.MAX_VALUE;
	}
	
	protected String getCellLocation() {
		return filename + ": " + sheet + "!" + columnName + (row + 1);
	}

}