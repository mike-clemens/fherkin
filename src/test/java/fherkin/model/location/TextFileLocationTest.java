package fherkin.model.location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  TextFileLocation
 * 
 * @author Mike Clemens
 */
public class TextFileLocationTest extends TextFileLocation {

	@Before
	public void before() {
		setFilename("TestFile.feature");
		setLine(1);
		setColumn(1);
	}
	
	@Test
	public void testGetLocation() {
		Assert.assertEquals("TestFile.feature: Line 1, column 1", getLocation());
	}
	
	@Test
	public void testEqual() {
		TextFileLocation other = new TextFileLocation();
		other.setFilename(getFilename());
		other.setLine(getLine());
		other.setColumn(getColumn());
		
		Assert.assertEquals(this, other);
		Assert.assertEquals(hashCode(), other.hashCode());
		Assert.assertEquals(0, compareTo(other));
	}
	
	@Test
	public void testNotEqualFilenameIsBefore() {
		TextFileLocation other = new TextFileLocation();
		other.setFilename("AAA.feature");
		other.setLine(getLine());
		other.setColumn(getColumn());
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) > 0);
	}
	
	@Test
	public void testNotEqualFilenameIsAfter() {
		TextFileLocation other = new TextFileLocation();
		other.setFilename("zzz.feature");
		other.setLine(getLine());
		other.setColumn(getColumn());
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) < 0);
	}
	
	@Test
	public void testNotEqualLineIsBefore() {
		TextFileLocation other = new TextFileLocation();
		other.setFilename(getFilename());
		other.setLine(getLine());
		other.setColumn(getColumn());
		setLine(100);
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) > 0);
	}
	
	@Test
	public void testNotEqualLineIsAfter() {
		TextFileLocation other = new TextFileLocation();
		other.setFilename(getFilename());
		other.setLine(100);
		other.setColumn(getColumn());
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) < 0);
	}
	
	@Test
	public void testNotEqualColumnIsBefore() {
		TextFileLocation other = new TextFileLocation();
		other.setFilename(getFilename());
		other.setLine(getLine());
		other.setColumn(getColumn());
		setColumn(100);
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) > 0);
	}
	
	@Test
	public void testNotEqualColumnIsAfter() {
		TextFileLocation other = new TextFileLocation();
		other.setFilename(getFilename());
		other.setLine(getLine());
		other.setColumn(100);
		
		Assert.assertNotEquals(this, other);
		Assert.assertTrue(compareTo(other) < 0);
	}
	
}