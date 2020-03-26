package fherkin.io;

import fherkin.FileFormat;
import fherkin.io.impl.csv.CSVGherkinWriter;
import fherkin.io.impl.text.TextGherkinWriter;
import fherkin.io.impl.xls.XLSGherkinWriter;
import fherkin.io.impl.xlsx.XLSXGherkinWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.commons.csv.CSVFormat;

/**
 * Factory for gherkin writers.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class GherkinWriterFactory {
	
	private FileFormat fileFormat;
	private File file;
	private String filename;
	private OutputStream outputStream;
	private Charset encoding;
	private CSVFormat csvFormat;
	
	public GherkinWriter newWriter() throws IOException {
		switch(fileFormat) {
			case FEATURE:
				if(file != null)
					return encoding == null ? new TextGherkinWriter(file) : new TextGherkinWriter(file, encoding);
				else
					return encoding == null ? new TextGherkinWriter(outputStream) : new TextGherkinWriter(outputStream, encoding);
			case CSV:
				if(csvFormat == null) {
					if(file != null)
						return encoding == null ? new CSVGherkinWriter(file) : new CSVGherkinWriter(file, encoding);
					else
						return encoding == null ? new CSVGherkinWriter(outputStream) : new CSVGherkinWriter(outputStream, encoding);
				}
				else {
					if(file != null)
						return encoding == null ? new CSVGherkinWriter(file, csvFormat) : new CSVGherkinWriter(file, encoding, csvFormat);
					else
						return encoding == null ? new CSVGherkinWriter(outputStream, csvFormat) : new CSVGherkinWriter(outputStream, encoding, csvFormat);
				}
			case XLS:
				return file != null ? new XLSGherkinWriter(file) : new XLSGherkinWriter(filename, outputStream);
			case XLSX:
				return file != null ? new XLSXGherkinWriter(file) : new XLSXGherkinWriter(filename, outputStream);
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

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void setEncoding(Charset encoding) {
		this.encoding = encoding;
	}

	public void setCsvFormat(CSVFormat csvFormat) {
		this.csvFormat = csvFormat;
	}
	
}