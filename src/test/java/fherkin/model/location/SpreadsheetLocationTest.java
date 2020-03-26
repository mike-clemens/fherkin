package fherkin.model.location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  SpreadsheetLocation
 * 
 * @author Mike Clemens
 */
public class SpreadsheetLocationTest extends SpreadsheetLocation {
	
	@Before
	public void before() {
		setFilename("TestFile.xlsx");
		setSheet("Fake Scenario");
		setRow(0);
		setColumn(0);
		setPosition(0);
	}
	
	@Test
	public void testGetLocation() {
		Assert.assertEquals("TestFile.xlsx: Fake Scenario!A1", getLocation());
	}
	
	@Test
	public void testGetLocationPositionGreaterThan0() {
		setPosition(5);
		Assert.assertEquals("TestFile.xlsx: Fake Scenario!A1:5", getLocation());
	}
	
	@Test
	public void testEqual() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename(getFilename());
		other.setSheet(getSheet());
		other.setRow(getRow());
		other.setColumn(getColumn());
		other.setPosition(getPosition());
		
		Assert.assertEquals(this, other);
		Assert.assertEquals(hashCode(), other.hashCode());
		Assert.assertEquals(0, compareTo(other));
	}
	
	@Test
	public void testNotEqualFilenameIsBefore() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename("AAA.xlsx");
		other.setSheet(getSheet());
		other.setRow(getRow());
		other.setColumn(getColumn());
		other.setPosition(getPosition());
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) > 0);
	}
	
	@Test
	public void testNotEqualFilenameIsAfter() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename("zzz.xlsx");
		other.setSheet(getSheet());
		other.setRow(getRow());
		other.setColumn(getColumn());
		other.setPosition(getPosition());
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) < 0);
	}
	
	@Test
	public void testNotEqualSheetIsBefore() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename(getFilename());
		other.setSheet("AAA");
		other.setRow(getRow());
		other.setColumn(getColumn());
		other.setPosition(getPosition());

		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) > 0);
	}
	
	@Test
	public void testNotEqualSheetIsAfter() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename(getFilename());
		other.setSheet("zzz");
		other.setRow(getRow());
		other.setColumn(getColumn());
		other.setPosition(getPosition());

		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) < 0);
	}
	
	@Test
	public void testNotEqualRowIsBefore() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename(getFilename());
		other.setSheet(getSheet());
		other.setRow(getRow());
		other.setColumn(getColumn());
		other.setPosition(getPosition());
		setRow(100);
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) > 0);
	}
	
	@Test
	public void testNotEqualRowIsAfter() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename(getFilename());
		other.setSheet(getSheet());
		other.setRow(100);
		other.setColumn(getColumn());
		other.setPosition(getPosition());

		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) < 0);
	}
	
	@Test
	public void testNotEqualColumnIsBefore() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename(getFilename());
		other.setSheet(getSheet());
		other.setRow(getRow());
		other.setColumn(getColumn());
		other.setPosition(getPosition());
		setColumn(100);
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) > 0);
	}
	
	@Test
	public void testNotEqualColumnIsAfter() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename(getFilename());
		other.setSheet(getSheet());
		other.setRow(getRow());
		other.setColumn(100);
		other.setPosition(getPosition());

		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) < 0);
	}
	
	@Test
	public void testNotEqualPositionIsBefore() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename(getFilename());
		other.setSheet(getSheet());
		other.setRow(getRow());
		other.setColumn(getColumn());
		other.setPosition(-1);
		setColumn(100);
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) > 0);
	}
	
	@Test
	public void testNotEqualPositionIsAfter() {
		SpreadsheetLocation other = new SpreadsheetLocation();
		other.setFilename(getFilename());
		other.setSheet(getSheet());
		other.setRow(getRow());
		other.setColumn(getColumn());
		other.setPosition(10);

		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) < 0);
	}
	
	@Test
	public void testCellA() {
		setRow(41);
		setColumn(0);
		Assert.assertTrue(getLocation().endsWith("!A42"));
	}
	
	@Test
	public void testCellZ() {
		setRow(41);
		setColumn(25);
		Assert.assertTrue(getLocation().endsWith("!Z42"));
	}
	
	@Test
	public void testCellAA() {
		setRow(41);
		setColumn(26);
		Assert.assertTrue(getLocation().endsWith("!AA42"));
	}
	
	@Test
	public void testCellAZ() {
		setRow(41);
		setColumn(51);
		Assert.assertTrue(getLocation().endsWith("!AZ42"));
	}
	
}