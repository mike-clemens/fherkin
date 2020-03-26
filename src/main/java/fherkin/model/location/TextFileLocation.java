package fherkin.model.location;

/**
 * Location implementation representing a line a column in a text file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class TextFileLocation implements Location, Comparable<TextFileLocation>, Cloneable {
	
	private String filename;
	private int line;
	private int column;
	
	@Override
	public String getLocation() {
		return filename + ": Line " + line + ", column " + column;
	}

	@Override
	public int compareTo(TextFileLocation o) {
		int compare = filename.compareTo(o.filename);
		if(compare != 0)
			return compare;
		
		if(line < o.line)
			return -1;
		if(line > o.line)
			return 1;
		
		if(column < o.column)
			return -1;
		if(column > o.column)
			return 1;
		return 0;
	}
	
	@Override
	public int hashCode() {
		return getLocation().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null || !(o instanceof TextFileLocation))
			return false;
		
		TextFileLocation other = (TextFileLocation) o;
		return filename.equals(other.filename)
			&& line == other.line
			&& column == other.column;
	}
	
	public TextFileLocation clone() {
		TextFileLocation clone = new TextFileLocation();
		clone.setFilename(filename);
		clone.setLine(line);
		clone.setColumn(column);
		return clone;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

}