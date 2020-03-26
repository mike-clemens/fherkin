package fherkin.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  ObjectHelper
 * 
 * @author Mike Clemens
 */
public class ObjectHelperTest extends ObjectHelper {
	
	@Test
	public void testEqualsBothNull() {
		Assert.assertTrue(equals(null, null));
	}

	@Test
	public void testEqualsFirstNull() {
		Assert.assertFalse(equals(null, "abc"));
	}

	@Test
	public void testEqualsSecondNull() {
		Assert.assertFalse(equals("abc", null));
	}

	@Test
	public void testEqualsNeitherNull() {
		Assert.assertTrue(equals("abc", "abc"));
		Assert.assertFalse(equals("abc", "def"));
	}

}