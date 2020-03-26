package fherkin.style;

import fherkin.model.ObjectHelper;
import org.apache.poi.ss.usermodel.Font;
import org.json.simple.JSONObject;

/**
 * Class representing the configuration of a font.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class FontConfig {
	
	private Boolean bold;
	private Boolean italic;
	private Byte underline;
	private Boolean strikeout;
	private Short color;
	private Short fontHeight;
	private Short fontHeightInPoints;
	private String fontName;
	private Short typeOffset;
	private Integer charSet;
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null || !(o instanceof FontConfig))
			return false;
		
		FontConfig that = (FontConfig) o;
		return ObjectHelper.equals(bold, that.bold)
			&& ObjectHelper.equals(italic, that.italic)
			&& ObjectHelper.equals(underline, that.underline)
			&& ObjectHelper.equals(strikeout, that.strikeout)
			&& ObjectHelper.equals(color, that.color)
			&& ObjectHelper.equals(fontHeight, that.fontHeight)
			&& ObjectHelper.equals(fontHeightInPoints, that.fontHeightInPoints)
			&& ObjectHelper.equals(fontName, that.fontName)
			&& ObjectHelper.equals(typeOffset, that.typeOffset)
			&& ObjectHelper.equals(charSet, that.charSet);
	}
	
	@Override
	public int hashCode() {
		return ObjectHelper.hashCode(
				bold,
				italic,
				underline,
				strikeout,
				color,
				fontHeight,
				fontHeightInPoints,
				fontName,
				typeOffset,
				charSet);
	}
	
	public void populate(String group, JSONObject object) {
		if(object.containsKey("bold"))
			bold = StyleConfigHelper.toBoolean(group, "bold", object.get("bold").toString());
		if(object.containsKey("italic"))
			italic = StyleConfigHelper.toBoolean(group, "italic", object.get("italic").toString());
		if(object.containsKey("underline"))
			underline = StyleConfigHelper.toByte(group, "underline", object.get("underline").toString());
		if(object.containsKey("strikeout"))
			strikeout = StyleConfigHelper.toBoolean(group, "strikeout", object.get("strikeout").toString());
		if(object.containsKey("color"))
			color = StyleConfigHelper.toColor(group, "color", object.get("color").toString());
		if(object.containsKey("fontHeight"))
			fontHeight = StyleConfigHelper.toShort(group, "fontHeight", object.get("fontHeight").toString());
		if(object.containsKey("fontHeightInPoints"))
			fontHeightInPoints = StyleConfigHelper.toShort(group, "fontHeightInPoints", object.get("fontHeightInPoints").toString());
		if(object.containsKey("fontName"))
			fontName = object.get("fontName").toString();
		if(object.containsKey("typeOffset"))
			typeOffset = StyleConfigHelper.toShort(group, "typeOffset", object.get("typeOffset").toString());
		if(object.containsKey("charSet"))
			charSet = StyleConfigHelper.toInteger(group, "charSet", object.get("charSet").toString());
	}
	
	public FontConfig clone() {
		FontConfig clone = new FontConfig();
		clone.bold = bold;
		clone.italic = italic;
		clone.underline = underline;
		clone.strikeout = strikeout;
		clone.color = color;
		clone.fontHeight = fontHeight;
		clone.fontHeightInPoints = fontHeightInPoints;
		clone.fontName = fontName;
		clone.typeOffset = typeOffset;
		clone.charSet = charSet;
		return clone;
	}
	
	public void setTo(Font font) {
		if(bold != null)
			font.setBold(bold);
		if(italic != null)
			font.setItalic(italic);
		if(underline != null)
			font.setUnderline(underline);
		if(strikeout != null)
			font.setStrikeout(strikeout);
		if(color != null)
			font.setColor(color);
		if(fontHeight != null)
			font.setFontHeight(fontHeight);
		if(fontHeightInPoints != null)
			font.setFontHeightInPoints(fontHeightInPoints);
		if(fontName != null)
			font.setFontName(fontName);
		if(typeOffset != null)
			font.setTypeOffset(typeOffset);
		if(charSet != null)
			font.setCharSet(charSet);
	}

	public Boolean getBold() {
		return bold;
	}

	public Boolean getItalic() {
		return italic;
	}

	public Byte getUnderline() {
		return underline;
	}

	public Boolean getStrikeout() {
		return strikeout;
	}

	public Short getColor() {
		return color;
	}

	public Short getFontHeight() {
		return fontHeight;
	}

	public Short getFontHeightInPoints() {
		return fontHeightInPoints;
	}

	public String getFontName() {
		return fontName;
	}

	public Short getTypeOffset() {
		return typeOffset;
	}

	public Integer getCharSet() {
		return charSet;
	}

	void setBold(Boolean bold) {
		this.bold = bold;
	}

	void setItalic(Boolean italic) {
		this.italic = italic;
	}

	void setUnderline(Byte underline) {
		this.underline = underline;
	}

	void setStrikeout(Boolean strikeout) {
		this.strikeout = strikeout;
	}

	void setColor(Short color) {
		this.color = color;
	}

	void setFontHeight(Short fontHeight) {
		this.fontHeight = fontHeight;
	}

	void setFontHeightInPoints(Short fontHeightInPoints) {
		this.fontHeightInPoints = fontHeightInPoints;
	}

	void setFontName(String fontName) {
		this.fontName = fontName;
	}

	void setTypeOffset(Short typeOffset) {
		this.typeOffset = typeOffset;
	}

	void setCharSet(Integer charSet) {
		this.charSet = charSet;
	}

}