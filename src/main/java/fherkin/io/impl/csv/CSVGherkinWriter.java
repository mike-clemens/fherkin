package fherkin.io.impl.csv;

import fherkin.io.impl.AbstractSpreadsheetGherkinWriter;
import fherkin.style.CellStyleConfig;
import fherkin.style.FontConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Gherkin writer that writes to a CSV workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class CSVGherkinWriter extends AbstractSpreadsheetGherkinWriter {
	
	protected CSVFormat format = CSVFormat.DEFAULT;
	protected PrintWriter out;
	
	public CSVGherkinWriter(File file) throws IOException {
		out = new PrintWriter(file);
	}
	
	public CSVGherkinWriter(File file, Charset charset) throws IOException {
		out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
	}

	public CSVGherkinWriter(File file, CSVFormat format) throws IOException {
		out = new PrintWriter(file);
		this.format = format;
	}
	
	public CSVGherkinWriter(File file, Charset charset, CSVFormat format) throws IOException {
		out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
		this.format = format;
	}

	public CSVGherkinWriter(OutputStream outputStream) throws IOException {
		out = new PrintWriter(new OutputStreamWriter(outputStream));
	}
	
	public CSVGherkinWriter(OutputStream outputStream, Charset charset) throws IOException {
		out = new PrintWriter(new OutputStreamWriter(outputStream, charset));
	}
	
	public CSVGherkinWriter(OutputStream outputStream, CSVFormat format) throws IOException {
		out = new PrintWriter(new OutputStreamWriter(outputStream));
		this.format = format;
	}
	
	public CSVGherkinWriter(OutputStream outputStream, Charset charset, CSVFormat format) throws IOException {
		out = new PrintWriter(new OutputStreamWriter(outputStream, charset));
		this.format = format;
	}
	
	protected CSVGherkinWriter() {
		// for unit testing only
	}
	
	@Override
	protected void doWrite(Workbook workbook) throws IOException {
		CSVPrinter printer = format.print(out);
		Sheet sheet = workbook.sheetIterator().next();
		
		Object[] values;
		int i;
		int length;
		Cell cell;
		for(Row row : sheet) {
			length = row.getPhysicalNumberOfCells();
			values = new String[length];
			for(i = 0; i < length; i++) {
				cell = row.getCell(i);
				values[i] = cell == null ? null : cell.getStringCellValue();
			}
			
			printer.printRecord(values);
		}
		
		out.close();
	}
	
	@Override
	protected Workbook newWorkbook() {
		return new CSVWorkbook();
	}

	@Override
	protected Sheet newSheet(Workbook workbook, String name) {
		return workbook.createSheet();
	}

	@Override
	protected Row newRow(Sheet sheet) {
		return sheet.createRow(sheet.getPhysicalNumberOfRows());
	}

	@Override
	protected Cell newCell(Row row, int column) {
		return row.createCell(column);
	}
	
	@Override
	protected void setCellStyleAndFont(Cell cell, CellStyleConfig styleConfig, FontConfig fontConfig) {
	}
	
	@Override
	protected void setCellStyle(Cell cell, CellStyleConfig styleConfig) {
	}
	
	@Override
	protected CellStyle getCellStyleAndFont(CellStyleConfig styleConfig, FontConfig fontConfig) {
		return null;
	}

	@Override
	protected CellStyle getCellStyle(CellStyleConfig styleConfig) {
		return null;
	}

	@Override
	protected void setRichText(Cell cell, String[] texts, FontConfig[] fonts) {
		StringBuffer buffer = new StringBuffer();
		for(String text : texts)
			buffer.append(text);
		cell.setCellValue(buffer.toString());
	}

}