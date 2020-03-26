package fherkin.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Scanner for finding files based on expressions.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class FileScanner {
	
	private Map<String, String> regexes = new HashMap<String, String>();
	
	public void scan(File dir, String path, FileScannerCallback callback) throws IOException {
		// validate the path string
		path = path.replaceAll("\\\\", "/");
		if(!isPathValid(path))
			throw new IOException("Invalid path: " + path);
		
		doScan(null, dir, path, callback);
	}
	
	protected void doScan(String relativePath, File dir, String path, FileScannerCallback callback) throws IOException {
		int index = path.indexOf('/');
		String expression = index < 0 ? path : path.substring(0, index);
		String remainder = index < 0 ? null : path.substring(index + 1);
		
		if(expression.equals("**"))
			doRecursiveScan(relativePath, dir, remainder, callback);
		else
		if(remainder == null)
			doFileScan(relativePath, dir, path, callback);
		else
			for(File file : dir.listFiles())
				if(file.isDirectory() && isMatch(expression, file))
					doScan(relativePath == null ? file.getName() : relativePath + "/" + file.getName(), file, remainder, callback);
	}
	
	protected void doRecursiveScan(String relativePath, File dir, String path, FileScannerCallback callback) throws IOException {
		doFileScan(relativePath, dir, path, callback);
		for(File file : dir.listFiles())
			if(file.isDirectory())
				doRecursiveScan(relativePath == null ? file.getName() : relativePath + "/" + file.getName(), file, path, callback);
	}
	
	protected void doFileScan(String relativePath, File dir, String path, FileScannerCallback callback) throws IOException {
		for(File file : dir.listFiles())
			if(!file.isDirectory() && (path == null || isMatch(path, file)))
				callback.process(file, relativePath == null ? file.getName() : relativePath + "/" + file.getName());
	}
	
	protected boolean isMatch(String expression, File file) {
		if(expression.equals("**") || expression.equals("*"))
			return true;
		
		String regex = regexes.get(expression);
		if(regex == null) {
			StringBuffer buffer = new StringBuffer("^");
			for(char c : expression.toCharArray())
				switch(c) {
					case '.':
						buffer.append("\\.");
						break;
					case '^':
						buffer.append("\\^");
						break;
					case '$':
						buffer.append("\\$");
						break;
					case '+':
						buffer.append("\\+");
						break;
					case '*':
						buffer.append("(.*)");
						break;
					default:
						buffer.append(c);
				}
			
			regex = buffer.toString();
			regexes.put(expression, regex);
		}
		
		return file.getName().matches(regex);
	}
	
	protected boolean isPathValid(String path) {
		int index = path.indexOf("**");
		if(index > -1) {
			if(index > 0 && path.charAt(index - 1) != '/')
				return false;
			
			String remainder = path.substring(index + 2);
			if(remainder.length() < 1 || !remainder.startsWith("/"))
				return false;
			
			if(remainder.substring(1).indexOf('/') > -1)
				return false;
		}
		
		return true;
	}

}