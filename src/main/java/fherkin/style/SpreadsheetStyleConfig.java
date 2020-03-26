package fherkin.style;

/**
 * Interface defining the configuration of spreadsheet styles.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public interface SpreadsheetStyleConfig {
	
	CellStyleConfig getFeatureCellStyle();
	CellStyleConfig getFeatureTextCellStyle();
	CellStyleConfig getBackgroundCellStyle();
	CellStyleConfig getScenarioCellStyle();
	CellStyleConfig getScenarioOutlineCellStyle();
	CellStyleConfig getBackgroundTextCellStyle();
	CellStyleConfig getScenarioTextCellStyle();
	CellStyleConfig getScenarioOutlineTextCellStyle();
	CellStyleConfig getGivenCellStyle();
	CellStyleConfig getWhenCellStyle();
	CellStyleConfig getThenCellStyle();
	CellStyleConfig getAndCellStyle();
	CellStyleConfig getButCellStyle();
	CellStyleConfig getGivenTextCellStyle();
	CellStyleConfig getWhenTextCellStyle();
	CellStyleConfig getThenTextCellStyle();
	CellStyleConfig getAndTextCellStyle();
	CellStyleConfig getButTextCellStyle();
	CellStyleConfig getExamplesCellStyle();
	CellStyleConfig getTagCellStyle();
	CellStyleConfig getDataTableHeadingCellStyleAlignLeft();
	CellStyleConfig getDataTableHeadingCellStyleAlignRight();
	CellStyleConfig getDataTableRowCellStyleAlignLeft();
	CellStyleConfig getDataTableRowCellStyleAlignRight();
	CellStyleConfig getCommentCellStyle();
	
	FontConfig getFeatureFont();
	FontConfig getFeatureTextFont();
	FontConfig getBackgroundFont();
	FontConfig getScenarioFont();
	FontConfig getScenarioOutlineFont();
	FontConfig getBackgroundTextFont();
	FontConfig getScenarioTextFont();
	FontConfig getScenarioOutlineTextFont();
	FontConfig getGivenFont();
	FontConfig getWhenFont();
	FontConfig getThenFont();
	FontConfig getAndFont();
	FontConfig getButFont();
	FontConfig getGivenTextFont();
	FontConfig getWhenTextFont();
	FontConfig getThenTextFont();
	FontConfig getAndTextFont();
	FontConfig getButTextFont();
	FontConfig getGivenTextVariableFont();
	FontConfig getWhenTextVariableFont();
	FontConfig getThenTextVariableFont();
	FontConfig getAndTextVariableFont();
	FontConfig getButTextVariableFont();
	FontConfig getExamplesFont();
	FontConfig getTagFont();
	FontConfig getDataTableHeadingFontAlignLeft();
	FontConfig getDataTableHeadingFontAlignRight();
	FontConfig getDataTableRowFontAlignLeft();
	FontConfig getDataTableRowFontAlignRight();
	FontConfig getCommentFont();
	
}