package fherkin;

/**
 * Enumerated type defining the supported file formats.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public enum FileFormat {
	
	FEATURE("feature"),
	CSV("csv"),
	XLS("xls"),
	XLSX("xlsx");
	
	public static final String VALUES = FEATURE.getExtension() + ", " + CSV.getExtension() + ", " + XLS.getExtension() + ", " + XLSX.getExtension();
	
	private String extension;
	
	private FileFormat(String extension) {
		this.extension = extension;
	}
	
	public String getExtension() {
		return extension;
	}

}