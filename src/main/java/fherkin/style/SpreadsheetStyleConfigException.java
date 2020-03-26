package fherkin.style;

/**
 * Exception thrown to indicate a problem with spreadsheet style configuration.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class SpreadsheetStyleConfigException extends RuntimeException {
	
	private static final long serialVersionUID = -1912275332945484605L;

	public SpreadsheetStyleConfigException(String message) {
		super(message);
	}
	
	public SpreadsheetStyleConfigException(Throwable cause) {
		super(cause);
	}
	
	public SpreadsheetStyleConfigException(String message, Throwable cause) {
		super(message, cause);
	}

}