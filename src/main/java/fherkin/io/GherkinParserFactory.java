package fherkin.io;

import fherkin.FileFormat;
import fherkin.io.impl.csv.CSVGherkinParser;
import fherkin.io.impl.text.TextGherkinParser;
import fherkin.io.impl.xls.XLSGherkinParser;
import fherkin.io.impl.xlsx.XLSXGherkinParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.commons.csv.CSVFormat;

/**
 * Factory for gherkin parsers.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class GherkinParserFactory {
	
	private FileFormat fileFormat;
	private File file;
	private String filename;
	private InputStream inputStream;
	private Charset encoding;
	private CSVFormat csvFormat;
	
	public GherkinParser newParser() throws IOException {
		switch(fileFormat) {
			case FEATURE:
				if(file != null)
					return encoding == null ? new TextGherkinParser(file) : new TextGherkinParser(file, encoding);
				else
					return encoding == null ? new TextGherkinParser(filename, inputStream) : new TextGherkinParser(filename, inputStream, encoding);
			case CSV:
				if(csvFormat == null) {
					if(file != null)
						return encoding == null ? new CSVGherkinParser(file) : new CSVGherkinParser(file, encoding);
					else
						return encoding == null ? new CSVGherkinParser(filename, inputStream) : new CSVGherkinParser(filename, inputStream, encoding);
				}
				else {
					if(file != null)
						return encoding == null ? new CSVGherkinParser(file, csvFormat) : new CSVGherkinParser(file, encoding, csvFormat);
					else
						return encoding == null ? new CSVGherkinParser(filename, inputStream, csvFormat) : new CSVGherkinParser(filename, inputStream, encoding, csvFormat);
				}
			case XLS:
				return file != null ? new XLSGherkinParser(file) : new XLSGherkinParser(filename, inputStream);
			case XLSX:
				return file != null ? new XLSXGherkinParser(file) : new XLSXGherkinParser(filename, inputStream);
			default:
				throw new IllegalArgumentException(); // should never happen
		}
	}
	
	///// setters

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setEncoding(Charset encoding) {
		this.encoding = encoding;
	}

	public void setCsvFormat(CSVFormat csvFormat) {
		this.csvFormat = csvFormat;
	}

}