package fherkin;

import fherkin.io.FileScanner;
import fherkin.io.FileScannerCallback;
import fherkin.io.GherkinParser;
import fherkin.io.GherkinParserFactory;
import fherkin.io.GherkinWriter;
import fherkin.io.GherkinWriterFactory;
import fherkin.model.GherkinEntry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Driver class for converting gherkin files from one format to another.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Converter {
	
	private Log log = LogFactory.getLog(getClass());
	
	private FileFormat sourceFormat;
	private Charset sourceEncoding;
	private CSVFormat sourceCSVFormat = CSVFormat.DEFAULT;
	private File sourceDir;
	private String sourceFiles;

	private FileFormat targetFormat = FileFormat.FEATURE;
	private Charset targetEncoding;
	private CSVFormat targetCSVFormat = CSVFormat.DEFAULT;
	private File targetDir;
	private boolean createTargetDirectories;
	
	private int result;
	
	public int convert() throws IOException {
		LogHelper.trace(log, Converter.class, "convert");
		result = 0;
		
		if(!sourceDir.exists())
			throw new FileNotFoundException("Source directory does not exist: " + sourceDir.getAbsolutePath());
		if(!sourceDir.isDirectory())
			throw new IOException("Source directory is not a directory: " + sourceDir.getAbsolutePath());
		
		if(!targetDir.exists()) {
			if(!createTargetDirectories)
				throw new FileNotFoundException("Target directory does not exist: " + targetDir.getAbsolutePath());
			if(!targetDir.mkdirs())
				throw new IOException("Unable to create target directory: " + targetDir.getAbsolutePath());
		}
		if(!targetDir.isDirectory())
			throw new IOException("Target directory is not a directory: " + targetDir.getAbsolutePath());
		
		new FileScanner().scan(sourceDir, sourceFiles, new FileScannerCallback() {
			@Override
			public void process(File sourceFile, String relativePath) throws IOException {
				LogHelper.trace(log, getClass(), "process", sourceFile, relativePath);
				
				File targetFile = new File(createTargetDir(relativePath), getTargetFilename(relativePath));
				try {
					convertFile(
							getSourceFileFormat(sourceFile),
							sourceFile,
							getTargetFileFormat(),
							targetFile);
					
					log.info("Converted " + sourceFile.getAbsolutePath() + " to " + targetFile.getAbsolutePath());
				}
				catch(Exception e) {
					log.error(e.getClass().getSimpleName() + " caught:", e);
					log.error("Error converting file: " + sourceFile.getAbsolutePath());
					result = 2;
				}
			}
		});
		
		return result;
	}
	
	protected File createTargetDir(String relativePath) throws IOException {
		LogHelper.trace(log, Converter.class, "createTargetDir", relativePath);
		
		int index = relativePath.lastIndexOf('/');
		if(index < 0)
			return targetDir;
		
		File dir = new File(targetDir, relativePath.substring(0, index));
		if(!dir.exists()) {
			if(!createTargetDirectories)
				throw new FileNotFoundException("Target directory does not exist: " + dir.getAbsolutePath());
			
			log.debug("Creating target directory: " + dir.getAbsolutePath());
			if(!dir.mkdirs())
				throw new IOException("Unable to create target directory: " + dir.getAbsolutePath());
		}
		
		if(!dir.isDirectory())
			throw new IOException("Target directory is not a directory: " + dir.getAbsolutePath());
		
		return dir;
	}
	
	protected String getTargetFilename(String relativePath) {
		LogHelper.trace(log, Converter.class, "getTargetFilename", relativePath);
		
		int index = relativePath.lastIndexOf('/');
		String filename = index < 0 ? relativePath : relativePath.substring(index + 1);
		
		index = filename.lastIndexOf('.');
		if(index > -1)
			filename = filename.substring(0, index);
		filename += "." + targetFormat.getExtension();
		
		return filename;
	}
	
	protected FileFormat getSourceFileFormat(File file) throws IOException {
		LogHelper.trace(log, Converter.class, "getSourceFileFormat", file);
		
		if(sourceFormat != null)
			return sourceFormat;
		
		int index = file.getName().lastIndexOf('.');
		if(index < 0)
			throw new IOException("Unable to infer source format of file with no extension: " + file.getAbsolutePath());
		
		String extension = file.getName().substring(index + 1);
		for(FileFormat instance : FileFormat.values())
			if(instance.getExtension().equalsIgnoreCase(extension))
				return instance;
		
		throw new IOException("Unable to infer source format of file with unknown extension: " + file.getAbsolutePath());
	}
	
	protected FileFormat getTargetFileFormat() {
		return targetFormat == null ? FileFormat.FEATURE : targetFormat;
	}
	
	protected void convertFile(FileFormat sourceFileFormat, File sourceFile, FileFormat targetFileFormat, File targetFile) throws IOException {
		LogHelper.trace(log, Converter.class, "convert");
		
		// parse
		GherkinParserFactory parserFactory = new GherkinParserFactory();
		parserFactory.setFile(sourceFile);
		parserFactory.setFileFormat(sourceFileFormat);
		parserFactory.setEncoding(sourceEncoding);
		parserFactory.setCsvFormat(sourceCSVFormat);
		
		GherkinParser parser = parserFactory.newParser();
		List<GherkinEntry> entries = parser.parse();
		
		// write
		GherkinWriterFactory writerFactory = new GherkinWriterFactory();
		writerFactory.setFile(targetFile);
		writerFactory.setFileFormat(targetFileFormat);
		writerFactory.setEncoding(targetEncoding);
		writerFactory.setCsvFormat(targetCSVFormat);
		
		GherkinWriter writer = writerFactory.newWriter();
		writer.write(entries);
	}
	
	///// getters/setters
	
	public FileFormat getSourceFormat() {
		return sourceFormat;
	}

	public void setSourceFormat(FileFormat sourceFormat) {
		this.sourceFormat = sourceFormat;
	}

	public Charset getSourceEncoding() {
		return sourceEncoding;
	}

	public void setSourceEncoding(Charset sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}

	public CSVFormat getSourceCSVFormat() {
		return sourceCSVFormat;
	}

	public void setSourceCSVFormat(CSVFormat sourceCSVFormat) {
		this.sourceCSVFormat = sourceCSVFormat;
	}

	public File getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(File sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getSourceFiles() {
		return sourceFiles;
	}

	public void setSourceFiles(String sourceFiles) {
		this.sourceFiles = sourceFiles;
	}

	public FileFormat getTargetFormat() {
		return targetFormat;
	}

	public void setTargetFormat(FileFormat targetFormat) {
		this.targetFormat = targetFormat;
	}

	public Charset getTargetEncoding() {
		return targetEncoding;
	}

	public void setTargetEncoding(Charset targetEncoding) {
		this.targetEncoding = targetEncoding;
	}

	public CSVFormat getTargetCSVFormat() {
		return targetCSVFormat;
	}

	public void setTargetCSVFormat(CSVFormat targetCSVFormat) {
		this.targetCSVFormat = targetCSVFormat;
	}

	public File getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(File targetDir) {
		this.targetDir = targetDir;
	}

	public boolean isCreateTargetDirectories() {
		return createTargetDirectories;
	}

	public void setCreateTargetDirectories(boolean createTargetDirectories) {
		this.createTargetDirectories = createTargetDirectories;
	}

}