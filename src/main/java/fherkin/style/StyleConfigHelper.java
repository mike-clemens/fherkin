package fherkin.style;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * Helper class for converting values from style config files.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class StyleConfigHelper {
	
	public static Boolean toBoolean(String group, String property, String value) {
		if(value == null)
			return null;
		value = value.trim();
		if(value.length() < 1)
			return null;
		
		if(value.equalsIgnoreCase("true")
		|| value.equalsIgnoreCase("yes")
		|| value.equalsIgnoreCase("on"))
			return true;
		
		if(value.equalsIgnoreCase("false")
		|| value.equalsIgnoreCase("no")
		|| value.equalsIgnoreCase("off"))
			return false;
		
		throw new SpreadsheetStyleConfigException("Invalid boolean value for " + property + " on " + group + ": " + value);
	}
	
	public static Byte toByte(String group, String property, String value) {
		if(value == null)
			return null;
		if(value.length() < 1)
			return null;
		
		try {
			return Byte.parseByte(value.trim());
		}
		catch(NumberFormatException e) {
			throw new SpreadsheetStyleConfigException("Invalid byte value for " + property + " on " + group + ": " + value);
		}
	}

	public static Short toShort(String group, String property, String value) {
		if(value == null)
			return null;
		if(value.length() < 1)
			return null;
		
		try {
			return Short.parseShort(value.trim());
		}
		catch(NumberFormatException e) {
			throw new SpreadsheetStyleConfigException("Invalid short value for " + property + " on " + group + ": " + value);
		}
	}

	public static Integer toInteger(String group, String property, String value) {
		if(value == null)
			return null;
		if(value.length() < 1)
			return null;
		
		try {
			return Integer.parseInt(value.trim());
		}
		catch(NumberFormatException e) {
			throw new SpreadsheetStyleConfigException("Invalid integer value for " + property + " on " + group + ": " + value);
		}
	}

	public static Short toColor(String group, String property, String value) {
		if(value == null)
			return null;
		if(value.length() < 1)
			return null;
		
		String key = formatForEnum(value);
		for(HSSFColorPredefined color : HSSFColorPredefined.values())
			if(formatForEnum(color.name()).equals(key))
				return color.getIndex();
		
		try {
			return Short.parseShort(value.trim());
		}
		catch(NumberFormatException e) {
			throw new SpreadsheetStyleConfigException("Invalid color value for " + property + " on " + group + ": " + value);
		}
	}
	
	public static HorizontalAlignment toHorizontalAlignment(String group, String property, String value) {
		if(value == null)
			return null;
		if(value.length() < 1)
			return null;
		
		String key = formatForEnum(value);
		for(HorizontalAlignment x : HorizontalAlignment.values())
			if(formatForEnum(x.name()).equals(key))
				return x;
		
		throw new SpreadsheetStyleConfigException("Invalid horizontal alignment value for " + property + " on " + group + ": " + value);
	}
	
	public static VerticalAlignment toVerticalAlignment(String group, String property, String value) {
		if(value == null)
			return null;
		if(value.length() < 1)
			return null;
		
		String key = formatForEnum(value);
		for(VerticalAlignment x : VerticalAlignment.values())
			if(formatForEnum(x.name()).equals(key))
				return x;
		
		throw new SpreadsheetStyleConfigException("Invalid vertical alignment value for " + property + " on " + group + ": " + value);
	}
	
	public static BorderStyle toBorderStyle(String group, String property, String value) {
		if(value == null)
			return null;
		if(value.length() < 1)
			return null;
		
		String key = formatForEnum(value);
		for(BorderStyle x : BorderStyle.values())
			if(formatForEnum(x.name()).equals(key))
				return x;
		
		throw new SpreadsheetStyleConfigException("Invalid border style value for " + property + " on " + group + ": " + value);
	}
	
	public static FillPatternType toFillPatternType(String group, String property, String value) {
		if(value == null)
			return null;
		if(value.length() < 1)
			return null;
		
		String key = formatForEnum(value);
		for(FillPatternType x : FillPatternType.values())
			if(formatForEnum(x.name()).equals(key))
				return x;
		
		throw new SpreadsheetStyleConfigException("Invalid fill pattern type value for " + property + " on " + group + ": " + value);
	}
	
	private static String formatForEnum(String value) {
		return value.toUpperCase().replaceAll("\\_", "").replaceAll("\\s", "");
	}

}