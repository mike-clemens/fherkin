package fherkin;

import fherkin.lang.GherkinKeywords;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  Main
 * 
 * @author Mike Clemens
 */
public class MainTest extends Main {
	
	private Map<String, URL> resources;
	private Converter converter;
	
	@Before
	public void before() {
		resources = new HashMap<String, URL>();
		
		getDefaultLogger().removeAllAppenders();
		getDefaultLogger().setLevel(Level.OFF);
	}
	
	///// command line arguments
	
	@Test
	public void testMainHelp() {
		Assert.assertEquals(0, doMain(new String[] {
				"--help"
		}));
	}
	
	@Test
	public void testMainInvalidCommandLine() {
		Assert.assertEquals(10, doMain(new String[] {
				"--xyz"
		}));
	}
	
	@Test
	public void testMainLogLevel() {
		Assert.assertEquals(0, doMain(new String[] {
				"--logLevel",
				"FaTaL",
				"--sourceFiles",
				"*.xls"
		}));
		
		Assert.assertEquals(Level.FATAL, getDefaultLogger().getLevel());
	}
	
	@Test
	public void testMainLogLeveDefaultl() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.xls"
		}));
		
		Assert.assertEquals(Level.INFO, getDefaultLogger().getLevel());
	}
	
	@Test
	public void testMainLogLevelInvalid() {
		Assert.assertEquals(10, doMain(new String[] {
				"--logLevel",
				"really_really_serious",
				"--sourceFiles",
				"*.xls"
		}));
	}
	
	@Test
	public void testMainSourceFormatDefault() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.xls"
		}));
		
		Assert.assertNull(converter.getSourceFormat());
	}
	
	@Test
	public void testMainSourceFormat() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFormat",
				"feature",
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertEquals(FileFormat.FEATURE, converter.getSourceFormat());
	}
	
	@Test
	public void testMainInvalidSourceFormat() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceFormat",
				"xyz"
		}));
	}
	
	@Test
	public void testMainSourceEncodingDefault() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFormat",
				"feature",
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertNull(converter.getSourceEncoding());
	}
	
	@Test
	public void testMainSourceEncoding() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFormat",
				"feature",
				"--sourceEncoding",
				"utf8",
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertEquals(Charset.forName("utf8"), converter.getSourceEncoding());
	}
	
	@Test
	public void testMainSourceEncodingForNonText() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceFormat",
				"xlsx",
				"--sourceEncoding",
				"utf8"
		}));
	}
	
	@Test
	public void testMainInvalidSourceEncoding() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceEncoding",
				"xyz"
		}));
	}
	
	@Test
	public void testMainSourceCSVFormatDefault() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFormat",
				"csv",
				"--sourceFiles",
				"*.csv"
		}));
		
		Assert.assertEquals(CSVFormat.DEFAULT, converter.getSourceCSVFormat());
	}
	
	@Test
	public void testMainSourceCSVFormat() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFormat",
				"csv",
				"--sourceCSVFormat",
				"mysql",
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertEquals(CSVFormat.MYSQL, converter.getSourceCSVFormat());
	}
	
	@Test
	public void testMainSourceCSVFormatForNonCSV() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceFormat",
				"feature",
				"--sourceCSVFormat",
				"excel"
		}));
	}
	
	@Test
	public void testMainInvalidSourceCSVFormat() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceCSVFormat",
				"xyz"
		}));
	}
	
	@Test
	public void testMainSourceDir() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceDir",
				"/tmp/abc/def/xyz",
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertEquals("xyz", converter.getSourceDir().getName());
	}
	
	@Test
	public void testMainSourceDirDefault() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertEquals(".", converter.getSourceDir().getName());
	}
	
	@Test
	public void testMainSourceFilesNotSpecified() {
		Assert.assertEquals(10, doMain(new String[] {}));
	}
	
	@Test
	public void testMainSourceFiles() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertEquals("*.feature", converter.getSourceFiles());
	}
	
	@Test
	public void testMainTargetFormatDefault() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertEquals(FileFormat.FEATURE, converter.getTargetFormat());
	}
	
	@Test
	public void testMainTargetFormat() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetFormat",
				"feature"
		}));
		
		Assert.assertEquals(FileFormat.FEATURE, converter.getTargetFormat());
	}
	
	@Test
	public void testMainInvalidTargetFormat() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetFormat",
				"xyz"
		}));
	}
	
	@Test
	public void testMainTargetEncodingDefault() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetFormat",
				"feature"
		}));
		
		Assert.assertNull(converter.getTargetEncoding());
	}
	
	@Test
	public void testMainTargetEncoding() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetFormat",
				"feature",
				"--targetEncoding",
				"utf8"
		}));
		
		Assert.assertEquals(Charset.forName("utf8"), converter.getTargetEncoding());
	}
	
	@Test
	public void testMainTargetEncodingForNonText() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetFormat",
				"xls",
				"--targeteEncoding",
				"utf8"
		}));
	}
	
	@Test
	public void testMainInvalidTargetEncoding() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetEncoding",
				"xyz"
		}));
	}
	
	@Test
	public void testMainTargetCSVFormatDefault() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetFormat",
				"csv"
		}));
		
		Assert.assertEquals(CSVFormat.DEFAULT, converter.getTargetCSVFormat());
	}
	
	@Test
	public void testMainTargetCSVFormat() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetFormat",
				"csv",
				"--targetCSVFormat",
				"mysql"
		}));
		
		Assert.assertEquals(CSVFormat.MYSQL, converter.getTargetCSVFormat());
	}
	
	@Test
	public void testMainTargetCSVFormatForNonCSV() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetFormat",
				"feature",
				"--targetCSVFormat",
				"excel"
		}));
	}
	
	@Test
	public void testMainInvalidTargetCSVFormat() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetCSVFormat",
				"xyz"
		}));
	}
	
	@Test
	public void testMainTargetDir() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--targetDir",
				"/tmp/abc/def/xyz"
		}));
		
		Assert.assertEquals("xyz", converter.getTargetDir().getName());
	}
	
	@Test
	public void testMainTargetDirDefault() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertEquals(".", converter.getTargetDir().getName());
	}
	
	@Test
	public void testMainCreateTargetDirectoriesDefault() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature"
		}));
		
		Assert.assertFalse(converter.isCreateTargetDirectories());
	}
	
	@Test
	public void testMainCreateTargetDirectories() {
		Assert.assertEquals(0, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--createTargetDirectories",
				"Yes"
		}));
		
		Assert.assertTrue(converter.isCreateTargetDirectories());
	}
	
	@Test
	public void testMainCreateTargetDirectoriesInvalid() {
		Assert.assertEquals(10, doMain(new String[] {
				"--sourceFiles",
				"*.feature",
				"--createTargetDirectories",
				"Maybe"
		}));
	}
	
	///// get option methods
	
	@Test
	public void testGetFileFormatOption() {
		Assert.assertEquals(FileFormat.FEATURE, getFileFormatOption("FeAtUrE"));
		Assert.assertEquals(FileFormat.CSV,     getFileFormatOption("CsV"));
		Assert.assertEquals(FileFormat.XLS,     getFileFormatOption("XlS"));
		Assert.assertEquals(FileFormat.XLSX,    getFileFormatOption("XlSx"));
		Assert.assertNull(getFileFormatOption("xyz"));
	}
	
	@Test
	public void testGetEncodingOption() {
		Assert.assertEquals(Charset.forName("utf8"), getEncodingOption("utf8"));
		Assert.assertNull(getEncodingOption("xyz"));
	}
	
	@Test
	public void testGetCSVFormatOption() {
		Assert.assertEquals(CSVFormat.DEFAULT, getCSVFormatOption("DeFaUlT"));
		Assert.assertEquals(CSVFormat.EXCEL,   getCSVFormatOption("ExCeL"));
		Assert.assertEquals(CSVFormat.MYSQL,   getCSVFormatOption("MySqL"));
		Assert.assertEquals(CSVFormat.RFC4180, getCSVFormatOption("RfC4180"));
		Assert.assertEquals(CSVFormat.TDF,     getCSVFormatOption("TdF"));
		Assert.assertNull(getCSVFormatOption("xyz"));
	}
	
	@Test
	public void testGetBooleanOption() {
		Assert.assertTrue(getBooleanOption("TrUe"));
		Assert.assertTrue(getBooleanOption("YeS"));
		Assert.assertTrue(getBooleanOption("On"));
		
		Assert.assertFalse(getBooleanOption("FaLsE"));
		Assert.assertFalse(getBooleanOption("No"));
		Assert.assertFalse(getBooleanOption("OfF"));
		
		Assert.assertNull(getBooleanOption("xyz"));
	}
	
	@Test
	public void testGetLogLevelOption() {
		Assert.assertEquals(Level.ALL,   getLogLevelOption("aLL"));
		Assert.assertEquals(Level.TRACE, getLogLevelOption("TRaCe"));
		Assert.assertEquals(Level.DEBUG, getLogLevelOption("DeBuG"));
		Assert.assertEquals(Level.INFO,  getLogLevelOption("iNFo"));
		Assert.assertEquals(Level.WARN,  getLogLevelOption("WaRN"));
		Assert.assertEquals(Level.ERROR, getLogLevelOption("eRRoR"));
		Assert.assertEquals(Level.FATAL, getLogLevelOption("FaTaL"));
		Assert.assertEquals(Level.OFF,   getLogLevelOption("oFF"));
	}
	
	///// initLog4j
	
	@Test
	public void testInitLog4j() {
		initLog4j();
		Assert.assertTrue(getDefaultLogger().getAllAppenders().hasMoreElements());
	}
	
	@Test
	public void testInitLog4jProperitesFileExists() throws MalformedURLException {
		resources.put("log4j.properties", new URL("file:///"));
		initLog4j();
		Assert.assertFalse(getDefaultLogger().getAllAppenders().hasMoreElements());
	}
	
	@Test
	public void testInitLog4jXMLFileExists() throws MalformedURLException {
		resources.put("log4j.xml", new URL("file:///"));
		initLog4j();
		Assert.assertFalse(getDefaultLogger().getAllAppenders().hasMoreElements());
	}
	
	///// overridden methods
	
	@Override
	protected URL getResource(String name) {
		Assert.assertNotNull(super.getResource(GherkinKeywords.class.getPackage().getName().replaceAll("\\.", "/") + "/gherkin-languages.json"));
		return resources.get(name);
	}
	
	@Override
	protected Logger getDefaultLogger() {
		Assert.assertNotNull(super.getDefaultLogger());
		return Logger.getLogger("fherkin.Main.testing.default.logger");
	}
	
	@Override
	protected Converter newConverter() {
		Assert.assertNotNull(super.newConverter());
		converter = new Converter() {
			@Override
			public int convert() throws IOException {
				return 0;
			}
		};
		
		return converter;
	}

}