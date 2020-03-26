package fherkin.style;

import fherkin.model.ObjectHelper;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.json.simple.JSONObject;

/**
 * Class representing the configuration of a cell style.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class CellStyleConfig {
	
	private HorizontalAlignment alignment;
	private VerticalAlignment verticalAlignment;
	private Short indention;
	private BorderStyle borderTop;
	private BorderStyle borderLeft;
	private BorderStyle borderRight;
	private BorderStyle borderBottom;
	private Short topBorderColor;
	private Short leftBorderColor;
	private Short rightBorderColor;
	private Short bottomBorderColor;
	private FillPatternType fillPattern;
	private Short fillBackgroundColor;
	private Short fillForegroundColor;
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null || !(o instanceof CellStyleConfig))
			return false;
		
		CellStyleConfig that = (CellStyleConfig) o;
		return ObjectHelper.equals(alignment, that.alignment)
			&& ObjectHelper.equals(verticalAlignment, that.verticalAlignment)
			&& ObjectHelper.equals(indention, that.indention)
			&& ObjectHelper.equals(borderTop, that.borderTop)
			&& ObjectHelper.equals(borderLeft, that.borderLeft)
			&& ObjectHelper.equals(borderRight, that.borderRight)
			&& ObjectHelper.equals(borderBottom, that.borderBottom)
			&& ObjectHelper.equals(topBorderColor, that.topBorderColor)
			&& ObjectHelper.equals(leftBorderColor, that.leftBorderColor)
			&& ObjectHelper.equals(rightBorderColor, that.rightBorderColor)
			&& ObjectHelper.equals(bottomBorderColor, that.bottomBorderColor)
			&& ObjectHelper.equals(fillPattern, that.fillPattern)
			&& ObjectHelper.equals(fillBackgroundColor, that.fillBackgroundColor)
			&& ObjectHelper.equals(fillForegroundColor, that.fillForegroundColor);
	}
	
	@Override
	public int hashCode() {
		return ObjectHelper.hashCode(
				alignment,
				verticalAlignment,
				indention,
				borderTop,
				borderLeft,
				borderRight,
				borderBottom,
				topBorderColor,
				leftBorderColor,
				rightBorderColor,
				bottomBorderColor,
				fillPattern,
				fillBackgroundColor,
				fillForegroundColor);
	}
	
	public void populate(String group, JSONObject object) {
		if(object.containsKey("alignment"))
			alignment = StyleConfigHelper.toHorizontalAlignment(group, "alignment", object.get("alignment").toString());
		if(object.containsKey("verticalAlignment"))
			verticalAlignment = StyleConfigHelper.toVerticalAlignment(group, "verticalAlignment", object.get("verticalAlignment").toString());
		if(object.containsKey("indention"))
			indention = StyleConfigHelper.toShort(group, "indention", object.get("indention").toString());
		if(object.containsKey("borderTop"))
			borderTop = StyleConfigHelper.toBorderStyle(group, "borderTop", object.get("borderTop").toString());
		if(object.containsKey("borderLeft"))
			borderLeft = StyleConfigHelper.toBorderStyle(group, "borderLeft", object.get("borderLeft").toString());
		if(object.containsKey("borderRight"))
			borderRight = StyleConfigHelper.toBorderStyle(group, "borderRight", object.get("borderRight").toString());
		if(object.containsKey("borderBottom"))
			borderBottom = StyleConfigHelper.toBorderStyle(group, "borderBottom", object.get("borderBottom").toString());
		if(object.containsKey("topBorderColor"))
			topBorderColor = StyleConfigHelper.toColor(group, "topBorderColor", object.get("topBorderColor").toString());
		if(object.containsKey("leftBorderColor"))
			leftBorderColor = StyleConfigHelper.toColor(group, "leftBorderColor", object.get("leftBorderColor").toString());
		if(object.containsKey("rightBorderColor"))
			rightBorderColor = StyleConfigHelper.toColor(group, "rightBorderColor", object.get("rightBorderColor").toString());
		if(object.containsKey("bottomBorderColor"))
			bottomBorderColor = StyleConfigHelper.toColor(group, "bottomBorderColor", object.get("bottomBorderColor").toString());
		if(object.containsKey("fillPattern"))
			fillPattern = StyleConfigHelper.toFillPatternType(group, "fillPattern", object.get("fillPattern").toString());
		if(object.containsKey("fillBackgroundColor"))
			fillBackgroundColor = StyleConfigHelper.toColor(group, "fillBackgroundColor", object.get("fillBackgroundColor").toString());
		if(object.containsKey("fillForegroundColor"))
			fillForegroundColor = StyleConfigHelper.toColor(group, "fillForegroundColor", object.get("fillForegroundColor").toString());
	}
	
	public CellStyleConfig clone() {
		CellStyleConfig clone = new CellStyleConfig();
		clone.alignment = alignment;
		clone.verticalAlignment = verticalAlignment;
		clone.indention = indention;
		clone.borderTop = borderTop;
		clone.borderLeft = borderLeft;
		clone.borderRight = borderRight;
		clone.borderBottom = borderBottom;
		clone.topBorderColor = topBorderColor;
		clone.leftBorderColor = leftBorderColor;
		clone.rightBorderColor = rightBorderColor;
		clone.bottomBorderColor = bottomBorderColor;
		clone.fillPattern = fillPattern;
		clone.fillBackgroundColor = fillBackgroundColor;
		clone.fillForegroundColor = fillForegroundColor;
		return clone;
	}
	
    public void setTo(CellStyle style) {
		if(alignment != null)
			style.setAlignment(alignment);
		if(verticalAlignment != null)
			style.setVerticalAlignment(verticalAlignment);
		if(indention != null)
			style.setIndention(indention);
		if(borderTop != null)
			style.setBorderTop(borderTop);
		if(borderLeft != null)
			style.setBorderLeft(borderLeft);
		if(borderRight != null)
			style.setBorderRight(borderRight);
		if(borderBottom != null)
			style.setBorderBottom(borderBottom);
		if(topBorderColor != null)
			style.setTopBorderColor(topBorderColor);
		if(leftBorderColor != null)
			style.setLeftBorderColor(leftBorderColor);
		if(rightBorderColor != null)
			style.setRightBorderColor(rightBorderColor);
		if(bottomBorderColor != null)
			style.setBottomBorderColor(bottomBorderColor);
		if(fillPattern != null)
			style.setFillPattern(fillPattern);
		if(fillBackgroundColor != null)
			style.setFillBackgroundColor(fillBackgroundColor);
		if(fillForegroundColor != null)
			style.setFillForegroundColor(fillForegroundColor);
    }
	
	///// getters

	public HorizontalAlignment getAlignment() {
		return alignment;
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public Short getIndention() {
		return indention;
	}

	public BorderStyle getBorderTop() {
		return borderTop;
	}

	public BorderStyle getBorderLeft() {
		return borderLeft;
	}

	public BorderStyle getBorderRight() {
		return borderRight;
	}

	public BorderStyle getBorderBottom() {
		return borderBottom;
	}

	public Short getTopBorderColor() {
		return topBorderColor;
	}

	public Short getLeftBorderColor() {
		return leftBorderColor;
	}

	public Short getRightBorderColor() {
		return rightBorderColor;
	}

	public Short getBottomBorderColor() {
		return bottomBorderColor;
	}

	public FillPatternType getFillPattern() {
		return fillPattern;
	}

	public Short getFillBackgroundColor() {
		return fillBackgroundColor;
	}

	public Short getFillForegroundColor() {
		return fillForegroundColor;
	}
	
	///// setters

	void setAlignment(HorizontalAlignment alignment) {
		this.alignment = alignment;
	}

	void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	void setIndention(Short indention) {
		this.indention = indention;
	}

	void setBorderTop(BorderStyle borderTop) {
		this.borderTop = borderTop;
	}

	void setBorderLeft(BorderStyle borderLeft) {
		this.borderLeft = borderLeft;
	}

	void setBorderRight(BorderStyle borderRight) {
		this.borderRight = borderRight;
	}

	void setBorderBottom(BorderStyle borderBottom) {
		this.borderBottom = borderBottom;
	}

	void setTopBorderColor(Short topBorderColor) {
		this.topBorderColor = topBorderColor;
	}

	void setLeftBorderColor(Short leftBorderColor) {
		this.leftBorderColor = leftBorderColor;
	}

	void setRightBorderColor(Short rightBorderColor) {
		this.rightBorderColor = rightBorderColor;
	}

	void setBottomBorderColor(Short bottomBorderColor) {
		this.bottomBorderColor = bottomBorderColor;
	}

	void setFillPattern(FillPatternType fillPattern) {
		this.fillPattern = fillPattern;
	}

	void setFillBackgroundColor(Short fillBackgroundColor) {
		this.fillBackgroundColor = fillBackgroundColor;
	}

	void setFillForegroundColor(Short fillForegroundColor) {
		this.fillForegroundColor = fillForegroundColor;
	}

}