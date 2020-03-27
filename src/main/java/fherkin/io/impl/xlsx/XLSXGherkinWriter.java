package fherkin.io.impl.xlsx;

import fherkin.LogHelper;
import fherkin.io.impl.AbstractSpreadsheetGherkinWriter;
import fherkin.style.FontConfig;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Gherkin writer that writes to an XLSX workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class XLSXGherkinWriter extends AbstractSpreadsheetGherkinWriter {
	
	public XLSXGherkinWriter(File file) throws IOException {
		super(file);
	}
	
	public XLSXGherkinWriter(String filename, OutputStream outputStream) throws IOException {
		super(filename, outputStream);
	}
	
	protected XLSXGherkinWriter() {
		// for unit testing only
	}

	///// overridden methods
	
	@Override
	protected Workbook newWorkbook() {
		LogHelper.trace(log, XLSXGherkinWriter.class, "newWorkbook");
		
		workbook = new XSSFWorkbook();
		return workbook;
	}
	
	@Override
	protected void setRichText(Cell cell, String[] texts, FontConfig[] fonts) {
		LogHelper.trace(log, XLSXGherkinWriter.class, "setRichText", cell, texts, fonts);
		
		if(texts.length != fonts.length)
			throw new IllegalArgumentException();
		
		StringBuffer buffer = new StringBuffer();
		for(String text : texts)
			buffer.append(text);

		XSSFRichTextString string = new XSSFRichTextString(buffer.toString());
		int position = 0;
		for(int i = 0; i < texts.length; i++) {
			string.applyFont(position, position + texts[i].length(), getFont(fonts[i]));
			position += texts[i].length();
		}
		
		cell.setCellValue(string);
	}
	
}