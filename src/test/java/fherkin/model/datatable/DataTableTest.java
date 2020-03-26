package fherkin.model.datatable;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  DataTable
 * 
 * @author Mike Clemens
 */
public class DataTableTest extends DataTable {
	
	///// getColumnLength
	
	@Test
	public void testGetColumnLengthOutOfBounds() {
		initDataTable(new String[][] {
			new String[] {"a", "b", "c"},
			new String[] {"1", "2", "3"},
			new String[] {"2", "3", "5"},
			new String[] {"3", "5", "8"}
		});
		
		try {
			getColumnLength(3);
			Assert.fail("Expected IndexOutOfBoundsException");
		}
		catch(IndexOutOfBoundsException e) {
			// do nothing here; the test case passed
		}
	}
	
	@Test
	public void testGetColumnLength() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {"123"},
			new String[] {"123456789012345"},
			new String[] {"12345"},
			new String[] {"               1               "}
		});
		
		Assert.assertEquals(15, getColumnLength(0));
	}
	
	@Test
	public void testGetColumnLengthImproperlySizedTable() {
		initDataTable(new String[][] {
			new String[] {"a", "b", "c", "d", "e"},
			new String[] {"1"},
			new String[] {"2", "3", "5"},
			new String[] {"3", "5", "8", "13", "21"}
		});
		
		Assert.assertEquals(1, getColumnLength(0));
		Assert.assertEquals(1, getColumnLength(1));
		Assert.assertEquals(1, getColumnLength(2));
		Assert.assertEquals(2, getColumnLength(3));
		Assert.assertEquals(2, getColumnLength(4));
	}
	
	///// getColumnType
	
	@Test
	public void testGetColumnTypeOutOfBounds() {
		initDataTable(new String[][] {
			new String[] {"a", "b", "c"},
			new String[] {"1", "2", "3"},
			new String[] {"2", "3", "5"},
			new String[] {"3", "5", "8"}
		});
		
		try {
			getColumnType(3);
			Assert.fail("Expected IndexOutOfBoundsException");
		}
		catch(IndexOutOfBoundsException e) {
			// do nothing here; the test case passed
		}
	}
	
	@Test
	public void testGetColumnImproperlySizedTable() {
		initDataTable(new String[][] {
			new String[] {"a", "b", "c", "d", "e"},
			new String[] {"1"},
			new String[] {"2", "3", "5"},
			new String[] {"3", "5", "8", "13", "21"}
		});
		
		Assert.assertEquals(CellType.INTEGER, getColumnType(0));
		Assert.assertEquals(CellType.INTEGER, getColumnType(1));
		Assert.assertEquals(CellType.INTEGER, getColumnType(2));
		Assert.assertEquals(CellType.INTEGER, getColumnType(3));
		Assert.assertEquals(CellType.INTEGER, getColumnType(4));
	}
	
	@Test
	public void testGetColumnTypeBlank() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {""},
			new String[] {""},
			new String[] {""}
		});
		
		Assert.assertEquals(CellType.STRING, getColumnType(0));
	}
	
	@Test
	public void testGetColumnTypeStrings() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {"abc"},
			new String[] {"def"},
			new String[] {"ghi"}
		});
		
		Assert.assertEquals(CellType.STRING, getColumnType(0));
	}
	
	@Test
	public void testGetColumnTypeBooleans() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {"true"},
			new String[] {"false"},
			new String[] {"TRUE"},
			new String[] {"FALSE"}
		});
		
		Assert.assertEquals(CellType.BOOLEAN, getColumnType(0));
	}
	
	@Test
	public void testGetColumnTypeIntegers() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {"123"},
			new String[] {"456"},
			new String[] {"12345678901234567890"}
		});
		
		Assert.assertEquals(CellType.INTEGER, getColumnType(0));
	}
	
	@Test
	public void testGetColumnTypeFloats() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {"123.45"},
			new String[] {"456.78"},
			new String[] {"1234567890.1234567890"}
		});
		
		Assert.assertEquals(CellType.FLOAT, getColumnType(0));
	}
	
	@Test
	public void testGetColumnTypeSomeIntegersSomeFloats() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {"123"},
			new String[] {"123.45"},
			new String[] {"456"},
			new String[] {"456.78"},
			new String[] {"1234567890.1234567890"},
			new String[] {"1234567890.1234567890"}
		});
		
		Assert.assertEquals(CellType.FLOAT, getColumnType(0));
	}
	
	@Test
	public void testGetColumnTypeSomeNumbersSomeStrings() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {"123"},
			new String[] {"123.45"},
			new String[] {"abc"},
			new String[] {"456"},
			new String[] {"456.78"},
			new String[] {"def"},
			new String[] {"1234567890.1234567890"},
			new String[] {"1234567890.1234567890"},
			new String[] {"ghi"},
		});
		
		Assert.assertEquals(CellType.STRING, getColumnType(0));
	}
	
	@Test
	public void testGetColumnTypeSomeStringsSomeBooleans() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {"true"},
			new String[] {"false"},
			new String[] {"abc"},
			new String[] {"TRUE"},
			new String[] {"FALSE"},
			new String[] {"def"}
		});
		
		Assert.assertEquals(CellType.STRING, getColumnType(0));
	}
	
	@Test
	public void testGetColumnTypeSomeNumbersSomeBooleans() {
		initDataTable(new String[][] {
			new String[] {"Heading"},
			new String[] {"123"},
			new String[] {"123.45"},
			new String[] {"true"},
			new String[] {"456"},
			new String[] {"456.78"},
			new String[] {"false"},
			new String[] {"1234567890.1234567890"},
			new String[] {"1234567890.1234567890"},
			new String[] {"TrUe"},
		});
		
		Assert.assertEquals(CellType.STRING, getColumnType(0));
	}
	
	///// helper methods
	
	protected void initDataTable(String[][] values) {
		List<DataTableRow> rows = new ArrayList<DataTableRow>();
		List<DataTableCell> cells;
		DataTableRow row;
		DataTableCell cell;
		for(String[] cellValues : values) {
			cells = new ArrayList<DataTableCell>();
			row = new DataTableRow();
			row.setDataTable(this);
			row.setCells(cells);
			rows.add(row);
			
			for(String cellValue : cellValues) {
				cell = new DataTableCell();
				cell.setRow(row);
				cell.setValue(cellValue);
				cells.add(cell);
			}
		}
		
		setRows(rows);
	}

}