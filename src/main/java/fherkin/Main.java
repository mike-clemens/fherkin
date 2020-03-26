package fherkin;

import java.io.File;
import java.nio.charset.Charset;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Main class for the fherkin converter.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Main {
	
	private Log log = LogFactory.getLog(getClass());
	
	private static final String HELP = "help";
	
	private static final String SOURCE_FORMAT     = "sourceFormat";
	private static final String SOURCE_ENCODING   = "sourceEncoding";
	private static final String SOURCE_CSV_FORMAT = "sourceCSVFormat";
	private static final String SOURCE_DIR        = "sourceDir";
	private static final String SOURCE_FILES      = "sourceFiles";
	
	private static final String TARGET_FORMAT             = "targetFormat";
	private static final String TARGET_ENCODING           = "targetEncoding";
	private static final String TARGET_CSV_FORMAT         = "targetCSVFormat";
	private static final String TARGET_DIR                = "targetDir";
	private static final String CREATE_TARGET_DIRECTORIES = "createTargetDirectories";
	
	public static void main(String[] args) {
		int result = new Main().doMain(args);
		System.exit(result);
	}
	
	protected int doMain(String[] args) {
		// parse the command line arguments
		CommandLineParser parser = new DefaultParser();
		Options options = createOptions();

		CommandLine commandLine;
		try {
			commandLine = parser.parse(options, args);
		}
		catch(ParseException e) {
			log.error(e.getClass().getSimpleName() + " caught:", e);
			return printHelp(e.getMessage(), options, 10);
		}
		
		// if the help option is provided, print the help message and abort
		if(commandLine.hasOption("help"))
			return printHelp(null, options, 0);
		
		Converter instance = newConverter();
		
		// source arguments
		if(commandLine.hasOption(SOURCE_FORMAT)) {
			String s = commandLine.getOptionValue(SOURCE_FORMAT);
			instance.setSourceFormat(getFileFormatOption(s));
			if(instance.getSourceFormat() == null)
				return printHelp("Invalid " + SOURCE_FORMAT + " value; expected one of [" + FileFormat.VALUES + "], received " + s, options, 10);
		}
		
		if(commandLine.hasOption(SOURCE_ENCODING)) {
			if(instance.getSourceFormat() != null && instance.getSourceFormat() != FileFormat.FEATURE && instance.getSourceFormat() != FileFormat.CSV)
				return printHelp("Invalid command line arguments; " + SOURCE_ENCODING + " is only valid for source formats of " + FileFormat.FEATURE.getExtension() + " and " + FileFormat.CSV.getExtension(), options, 10);
			
			String s = commandLine.getOptionValue(SOURCE_ENCODING);
			instance.setSourceEncoding(getEncodingOption(s));
			if(instance.getSourceEncoding() == null)
				return printHelp("Invalid " + SOURCE_ENCODING + " value; expected valid charset, received " + s, options, 10);
		}
		
		if(commandLine.hasOption(SOURCE_CSV_FORMAT)) {
			if(instance.getSourceFormat() != null && instance.getSourceFormat() != FileFormat.CSV)
				return printHelp("Invalid command line arguments; " + SOURCE_CSV_FORMAT + " is only valid for source format of " + FileFormat.CSV.getExtension(), options, 10);
			
			String s = commandLine.getOptionValue(SOURCE_CSV_FORMAT);
			instance.setSourceCSVFormat(getCSVFormatOption(s));
			if(instance.getSourceCSVFormat() == null)
				return printHelp("Invalid " + SOURCE_CSV_FORMAT + " value; expected one of [default, excel, mysql, rfc4180, tdf], received " + s, options, 10); 
		}
		
		if(commandLine.hasOption(SOURCE_DIR))
			instance.setSourceDir(new File(commandLine.getOptionValue(SOURCE_DIR)));
		else
			instance.setSourceDir(new File("."));
		
		if(!commandLine.hasOption(SOURCE_FILES))
			return printHelp("Expected " + SOURCE_FILES, options, 10);
		instance.setSourceFiles(commandLine.getOptionValue(SOURCE_FILES));
		
		// target arguments
		if(commandLine.hasOption(TARGET_FORMAT)) {
			String s = commandLine.getOptionValue(TARGET_FORMAT);
			instance.setTargetFormat(getFileFormatOption(s));
			if(instance.getTargetFormat() == null)
				return printHelp("Invalid " + TARGET_FORMAT + " value; expected one of [" + FileFormat.VALUES + "], received " + s, options, 10);
		}
		
		if(commandLine.hasOption(TARGET_ENCODING)) {
			if(instance.getTargetFormat() != null && instance.getTargetFormat() != FileFormat.FEATURE && instance.getTargetFormat() != FileFormat.CSV)
				return printHelp("Invalid command line arguments; " + TARGET_ENCODING + " is only valid for source formats of " + FileFormat.FEATURE.getExtension() + " and " + FileFormat.CSV.getExtension(), options, 10);
			
			String s = commandLine.getOptionValue(TARGET_ENCODING);
			instance.setTargetEncoding(getEncodingOption(s));
			if(instance.getTargetEncoding() == null)
				return printHelp("Invalid " + TARGET_ENCODING + " value; expected valid charset, received " + s, options, 10);
		}
		
		if(commandLine.hasOption(TARGET_CSV_FORMAT)) {
			if(instance.getTargetFormat() != null && instance.getTargetFormat() != FileFormat.CSV)
				return printHelp("Invalid command line arguments; " + TARGET_CSV_FORMAT + " is only valid for target format of " + FileFormat.CSV.getExtension(), options, 10);
			
			String s = commandLine.getOptionValue(TARGET_CSV_FORMAT);
			instance.setTargetCSVFormat(getCSVFormatOption(s));
			if(instance.getTargetCSVFormat() == null)
				return printHelp("Invalid " + TARGET_CSV_FORMAT + " value; expected one of [default, excel, mysql, rfc4180, tdf], received " + s, options, 10); 
		}
		
		if(commandLine.hasOption(TARGET_DIR))
			instance.setTargetDir(new File(commandLine.getOptionValue(TARGET_DIR)));
		else
			instance.setTargetDir(new File("."));
		
		if(commandLine.hasOption(CREATE_TARGET_DIRECTORIES)) {
			String s = commandLine.getOptionValue(CREATE_TARGET_DIRECTORIES);
			Boolean b = getBooleanOption(s);
			if(b == null)
				return printHelp("Invalid " + CREATE_TARGET_DIRECTORIES + " value; expected boolean value, retrieved " + s, options, 10);
			
			instance.setCreateTargetDirectories(b);
		}
		
		try {
			instance.convert();
			return 0;
		}
		catch(Exception e) {
			log.error(e.getClass().getSimpleName() + " caught:", e);
			return 1;
		}
	}
	
	protected int printHelp(String message, Options options, int result) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(100, "java fherkin.Main", message, options, null, true);
		return result;
	}
	
	protected Options createOptions() {
		Options options = new Options();
		
		// help
		options.addOption(Option.builder()
				.argName(HELP)
				.longOpt(HELP)
				.desc("displays this help message")
				.build());
		
		// source
		options.addOption(Option.builder()
				.argName(SOURCE_FORMAT)
				.longOpt(SOURCE_FORMAT)
				.hasArg(true)
				.desc("source file format (feature, csv, xls, xlsx) -- if this is blank, the source format will be inferred based on the extension of the source filename")
				.build());
		options.addOption(Option.builder()
				.argName(SOURCE_ENCODING)
				.longOpt(SOURCE_ENCODING)
				.hasArg(true)
				.desc("source file encoding -- if this is blank, the JVM default charset will be used (this only applies to text file formats, such as feature and CSV)")
				.build());
		options.addOption(Option.builder()
				.argName(SOURCE_CSV_FORMAT)
				.longOpt(SOURCE_CSV_FORMAT)
				.hasArg(true)
				.desc("source CSV format (default, excel, mysql, rfc4180, tdf) -- if this is blank, the commons-csv default format will be used (this only applies to CSV file formats)")
				.build());
		options.addOption(Option.builder()
				.argName(SOURCE_DIR)
				.longOpt(SOURCE_DIR)
				.hasArg(true)
				.desc("source directory name -- if this is blank, the current directory will be used")
				.build());
		options.addOption(Option.builder()
				.argName(SOURCE_FILES)
				.longOpt(SOURCE_FILES)
				.hasArg(true)
				.desc("source file path, using ANT notation; ie. **/*.feature")
				.build());
		
		// target
		options.addOption(Option.builder()
				.argName(TARGET_FORMAT)
				.longOpt(TARGET_FORMAT)
				.hasArg(true)
				.desc("target file format (feature, csv, xls, xslx) -- if this is blank, feature will be used by default")
				.build());
		options.addOption(Option.builder()
				.argName(TARGET_ENCODING)
				.longOpt(TARGET_ENCODING)
				.hasArg(true)
				.desc("target file encoding -- if this is blank, the JVM default charset will be used (this only applies to text file formats, such as feature and CSV)")
				.build());
		options.addOption(Option.builder()
				.argName(TARGET_CSV_FORMAT)
				.longOpt(TARGET_CSV_FORMAT)
				.hasArg(true)
				.desc("target CSV format (default, excel, mysql, rfc4180, tdf) -- if this is blank, the commons-csv default format will be used (this only applies to CSV file formats)")
				.build());
		options.addOption(Option.builder()
				.argName(TARGET_DIR)
				.longOpt(TARGET_DIR)
				.hasArg(true)
				.desc("target directory name -- if this is blank, the current directory will be used")
				.build());
		options.addOption(Option.builder()
				.argName(CREATE_TARGET_DIRECTORIES)
				.longOpt(CREATE_TARGET_DIRECTORIES)
				.hasArg(true)
				.desc("create target directories indicator")
				.build());
		
		return options;
	}
	
	protected FileFormat getFileFormatOption(String s) {
		for(FileFormat value : FileFormat.values())
			if(value.name().equalsIgnoreCase(s))
				return value;
		return null;
	}
	
	protected Charset getEncodingOption(String s) {
		try {
			return Charset.forName(s);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	protected CSVFormat getCSVFormatOption(String s) {
		if("default".equalsIgnoreCase(s))
			return CSVFormat.DEFAULT;
		if("excel".equalsIgnoreCase(s))
			return CSVFormat.EXCEL;
		if("mysql".equalsIgnoreCase(s))
			return CSVFormat.MYSQL;
		if("rfc4180".equalsIgnoreCase(s))
			return CSVFormat.RFC4180;
		if("tdf".equalsIgnoreCase(s))
			return CSVFormat.TDF;
		
		return null;
	}
	
	protected Boolean getBooleanOption(String s) {
		if("true".equalsIgnoreCase(s)
		|| "yes".equalsIgnoreCase(s)
		|| "on".equalsIgnoreCase(s))
			return true;
		
		if("false".equalsIgnoreCase(s)
		|| "no".equalsIgnoreCase(s)
		|| "off".equalsIgnoreCase(s))
			return false;
		
		return null;
	}
	
	protected Converter newConverter() {
		return new Converter();
	}

}