package fherkin.io.impl.xlsx;

import fherkin.io.impl.AbstractSpreadsheetGherkinParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Gherkin parser that reads from an XLSX workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class XLSXGherkinParser extends AbstractSpreadsheetGherkinParser {
	
	public XLSXGherkinParser(File file) throws IOException {
		super(file);
	}
	
	public XLSXGherkinParser(String filename, InputStream inputStream) throws IOException {
		super(filename, inputStream);
	}
	
	protected XLSXGherkinParser() {
		// for unit testing only
	}
	
	@Override
	protected Workbook parseWorkbook() throws IOException {
		try {
			return new XSSFWorkbook(inputStream);
		}
		finally {
			inputStream.close();
		}
	}
	
}