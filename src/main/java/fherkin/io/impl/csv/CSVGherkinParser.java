package fherkin.io.impl.csv;

import fherkin.io.impl.AbstractSpreadsheetGherkinParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Gherkin parser that reads from a CSV workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class CSVGherkinParser extends AbstractSpreadsheetGherkinParser {
	
	protected CSVFormat format = CSVFormat.DEFAULT;
	protected Reader reader;
	
	public CSVGherkinParser(File file) throws IOException {
		filename = file.getName();
		reader = new FileReader(file);
	}
	
	public CSVGherkinParser(File file, CSVFormat format) throws IOException {
		filename = file.getName();
		reader = new FileReader(file);
		this.format = format;
	}
	
	public CSVGherkinParser(File file, Charset charset) throws IOException {
		filename = file.getName();
		reader = new InputStreamReader(new FileInputStream(file), charset);
	}
	
	public CSVGherkinParser(File file, Charset charset, CSVFormat format) throws IOException {
		filename = file.getName();
		reader = new InputStreamReader(new FileInputStream(file), charset);
		this.format = format;
	}
	
	public CSVGherkinParser(String filename, InputStream inputStream) throws IOException {
		this.filename = filename;
		reader = new InputStreamReader(inputStream);
	}
	
	public CSVGherkinParser(String filename, InputStream inputStream, CSVFormat format) throws IOException {
		this.filename = filename;
		reader = new InputStreamReader(inputStream);
		this.format = format;
	}
	
	public CSVGherkinParser(String filename, InputStream inputStream, Charset charset) throws IOException {
		this.filename = filename;
		reader = new InputStreamReader(inputStream, charset);
	}
	
	public CSVGherkinParser(String filename, InputStream inputStream, Charset charset, CSVFormat format) throws IOException {
		this.filename = filename;
		reader = new InputStreamReader(inputStream, charset);
		this.format = format;
	}
	
	public CSVGherkinParser(String filename, Reader reader) throws IOException {
		this.filename = filename;
		this.reader = reader;
	}
	
	public CSVGherkinParser(String filename, Reader reader, CSVFormat format) throws IOException {
		this.filename = filename;
		this.reader = reader;
		this.format = format;
	}
	
	public CSVGherkinParser(String filename, String contents) throws IOException {
		this.filename = filename;
		reader = new StringReader(contents);
	}
	
	public CSVGherkinParser(String filename, String contents, CSVFormat format) throws IOException {
		this.filename = filename;
		reader = new StringReader(contents);
		this.format = format;
	}
	
	protected CSVGherkinParser() {
		// for unit testing only
	}
	
	@Override
	protected Workbook parseWorkbook() throws IOException {
		Workbook workbook = new CSVWorkbook();
		Sheet sheet = workbook.createSheet();
		
		Iterable<CSVRecord> records = format.parse(reader);
		Row row;
		Cell cell;
		int rowNum = 0;
		int cellNum;
		for(CSVRecord record : records) {
			row = sheet.createRow(rowNum++);
			cellNum = 0;
			for(String s : record) {
				cell = row.createCell(cellNum++);
				cell.setCellValue(s);
			}
		}
		
		return workbook;
	}
	
}