package fherkin.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  FileScanner
 * 
 * @author Mike Clemens
 */
public class FileScannerTest extends FileScanner implements FileScannerCallback {
	
	private Map<String, File> scanned = new HashMap<String, File>();
	
	@Before
	public void before() {
		scanned.clear();
	}
	
	///// scan
	
	@Test
	public void testScanSingleDir() throws IOException {
		File dir = newDir("root", new File("a.feature"), newFile("b.feature"), newFile("c.feature"), newFile("d.xls"));
		scan(dir, "*.feature", this);
		
		Assert.assertEquals(3, scanned.size());
		Assert.assertTrue(scanned.containsKey("a.feature"));
		Assert.assertTrue(scanned.containsKey("b.feature"));
		Assert.assertTrue(scanned.containsKey("c.feature"));
	}
	
	@Test
	public void testScanNextDir() throws IOException {
		File dir = newDir("root",
				newDir("abc", new File("a.feature"), newFile("b.feature"), newFile("c.feature"), newFile("d.xls")),
				newDir("def", new File("d.feature"), newFile("e.feature"), newFile("f.feature"), newFile("g.xls")));
		scan(dir, "*abc/*.feature", this);
		
		Assert.assertEquals(3, scanned.size());
		Assert.assertTrue(scanned.containsKey("abc/a.feature"));
		Assert.assertTrue(scanned.containsKey("abc/b.feature"));
		Assert.assertTrue(scanned.containsKey("abc/c.feature"));
	}
	
	@Test
	public void testScanNextDirAsAsterisk() throws IOException {
		File dir = newDir("root",
				newDir("abc", new File("a.feature"), newFile("b.feature"), newFile("c.feature"), newFile("d.xls")),
				newDir("def", new File("d.feature"), newFile("e.feature"), newFile("f.feature"), newFile("g.xls")));
		scan(dir, "*/*.feature", this);
		
		Assert.assertEquals(6, scanned.size());
		Assert.assertTrue(scanned.containsKey("abc/a.feature"));
		Assert.assertTrue(scanned.containsKey("abc/b.feature"));
		Assert.assertTrue(scanned.containsKey("abc/c.feature"));
		Assert.assertTrue(scanned.containsKey("def/d.feature"));
		Assert.assertTrue(scanned.containsKey("def/e.feature"));
		Assert.assertTrue(scanned.containsKey("def/f.feature"));
	}
	
	@Test
	public void testScanNextDirPartial() throws IOException {
		File dir = newDir("root",
				newDir("abc", new File("a.feature"), newFile("b.feature"), newFile("c.feature"), newFile("d.xls")),
				newDir("def", new File("d.feature"), newFile("e.feature"), newFile("f.feature"), newFile("g.xls")));
		scan(dir, "*a*/a*.feature", this);
		
		Assert.assertEquals(1, scanned.size());
		Assert.assertTrue(scanned.containsKey("abc/a.feature"));
	}
	
	@Test
	public void testScanRecursiveDirs() throws IOException {
System.out.println("================ testScanRecursiveDirs ========================");
		File dir = newDir("root", newDir("a", newDir("b", newDir("c",newFile("a.feature"), newFile("b.feature"), newFile("c.feature")))));
		scan(dir, "**/*.feature", this);
		
		Assert.assertEquals(3, scanned.size());
		Assert.assertTrue(scanned.containsKey("a/b/c/a.feature"));
		Assert.assertTrue(scanned.containsKey("a/b/c/b.feature"));
		Assert.assertTrue(scanned.containsKey("a/b/c/c.feature"));
	}
	
	///// isMatch
	
	@Test
	public void testIsMatch() {
		Assert.assertTrue(isMatch("*", new File("abc.feature")));
		Assert.assertTrue(isMatch("**", new File("abc.feature")));

		Assert.assertTrue(isMatch("abc.feature", new File("abc.feature")));

		Assert.assertTrue(isMatch("*.feature", new File("abc.feature")));
		Assert.assertTrue(isMatch("$*.feature", new File("$abc.feature")));
		Assert.assertTrue(isMatch("^*.feature", new File("^abc.feature")));
		Assert.assertTrue(isMatch("+*.feature", new File("+abc.feature")));
		
		Assert.assertTrue(isMatch("abc.*", new File("abc.feature")));
		Assert.assertTrue(isMatch("abc$.*", new File("abc$.feature")));
		Assert.assertTrue(isMatch("abc^.*", new File("abc^.feature")));
		Assert.assertTrue(isMatch("abc+.*", new File("abc+.feature")));

		Assert.assertFalse(isMatch("*.xls", new File("abc.feature")));
	}
	
	///// isPathValid
	
	@Test
	public void testIsPathValid() {
		Assert.assertTrue(isPathValid("*"));
		Assert.assertTrue(isPathValid("**/*"));
		Assert.assertTrue(isPathValid("a/b/c/**/*"));
		
		Assert.assertFalse(isPathValid("**"));
		Assert.assertFalse(isPathValid("abc**/*"));
		Assert.assertFalse(isPathValid("**abc/*"));
		Assert.assertFalse(isPathValid("**/abc/def"));
	}
	
	///// helper methods
	
	@Override
	public void process(File file, String relativePath) throws IOException {
		if(scanned.containsKey(relativePath))
			throw new IOException(relativePath + " found more than once");
		scanned.put(relativePath, file);
	}
	
	@SuppressWarnings("serial")
	private static class MockFile extends File {
		private File[] files;
		private boolean directory;
		
		public MockFile(String name) {
			super(name);
		}
		
		@Override
		public File[] listFiles() {
			return files;
		}
		
		@Override
		public boolean isDirectory() {
			return directory;
		}
	}
	
	private MockFile newFile(String name) {
		return new MockFile(name);
	}
	
	private MockFile newDir(String name, File... children) {
		MockFile file = new MockFile(name);
		file.files = children;
		file.directory = true;
		return file;
	}

}