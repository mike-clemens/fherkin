package fherkin.io.impl.xls;

import fherkin.LogHelper;
import fherkin.io.impl.AbstractSpreadsheetGherkinParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Gherkin parser that reads from an XLS workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class XLSGherkinParser extends AbstractSpreadsheetGherkinParser {
	
	public XLSGherkinParser(File file) throws IOException {
		super(file);
	}
	
	public XLSGherkinParser(String filename, InputStream inputStream) throws IOException {
		super(filename, inputStream);
	}
	
	protected XLSGherkinParser() {
		// for unit testing only
	}
	
	@Override
	protected Workbook parseWorkbook() throws IOException {
		LogHelper.trace(log, XLSGherkinParser.class, "parseWorkbook");
		
		try {
			return new HSSFWorkbook(inputStream);
		}
		finally {
			inputStream.close();
		}
	}
	
}