package fherkin.io.impl.xls;

import fherkin.io.impl.AbstractSpreadsheetGherkinWriter;
import fherkin.style.FontConfig;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Gherkin writer that writes to an XLS workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class XLSGherkinWriter extends AbstractSpreadsheetGherkinWriter {
	
	public XLSGherkinWriter(File file) throws IOException {
		super(file);
	}
	
	public XLSGherkinWriter(String filename, OutputStream outputStream) throws IOException {
		super(filename, outputStream);
	}
	
	protected XLSGherkinWriter() {
		// for unit testing only
	}

	///// overridden methods
	
	@Override
	protected Workbook newWorkbook() {
		workbook = new HSSFWorkbook();
		return workbook;
	}
	
	@Override
	protected void setRichText(Cell cell, String[] texts, FontConfig[] fonts) {
		if(texts.length != fonts.length)
			throw new IllegalArgumentException();
		
		StringBuffer buffer = new StringBuffer();
		for(String text : texts)
			buffer.append(text);

		HSSFRichTextString string = new HSSFRichTextString(buffer.toString());
		int position = 0;
		for(int i = 0; i < texts.length; i++) {
			string.applyFont(position, position + texts[i].length(), getFont(fonts[i]));
			position += texts[i].length();
		}
		
		cell.setCellValue(string);
	}
	
}