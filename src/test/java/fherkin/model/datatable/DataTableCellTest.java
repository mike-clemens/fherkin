package fherkin.model.datatable;

import fherkin.model.datatable.DataTable.CellType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  DataTableCell
 * 
 * @author Mike Clemens
 */
public class DataTableCellTest extends DataTableCell {
	
	@Test
	public void testGetCellLengthNull() {
		setValue(null);
		Assert.assertEquals(0, getCellLength());
	}

	@Test
	public void testGetCellLength() {
		setValue(" abc ");
		Assert.assertEquals(3, getCellLength());
	}
	
	@Test
	public void testGetCellTypeNull() {
		setValue(null);
		Assert.assertNull(getCellType());
	}

	@Test
	public void testGetCellTypeBlank() {
		setValue("   ");
		Assert.assertNull(getCellType());
	}

	@Test
	public void testGetCellTypeBooleanTrue() {
		setValue("True");
		Assert.assertEquals(CellType.BOOLEAN, getCellType());
	}

	@Test
	public void testGetCellTypeBooleanFalse() {
		setValue("False");
		Assert.assertEquals(CellType.BOOLEAN, getCellType());
	}

	@Test
	public void testGetCellTypeLong() {
		setValue("12345");
		Assert.assertEquals(CellType.INTEGER, getCellType());
	}

	@Test
	public void testGetCellTypeDouble() {
		setValue("12345.6789");
		Assert.assertEquals(CellType.FLOAT, getCellType());
	}

	@Test
	public void testGetCellTypeString() {
		setValue("abc");
		Assert.assertEquals(CellType.STRING, getCellType());
	}

}