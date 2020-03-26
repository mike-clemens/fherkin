package fherkin.io.impl.csv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.AutoFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PaneInformation;

/**
 * Class representing a sheet in a CSV workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class CSVSheet implements Sheet {
	
	private List<Row> rows = new ArrayList<Row>();
	
	@Override
	public String getSheetName() {
		return "";
	}

	@Override
	public Iterator<Row> iterator() {
		return rows.iterator();
	}
	
	@Override
	public Iterator<Row> rowIterator() {
		return iterator();
	}

	@Override
	public Row getRow(int num) {
		return rows.size() < num ? rows.get(num) : null;
	}

	@Override
	public Row createRow(int num) {
		while(rows.size() <= num)
			rows.add(new CSVRow(this, rows.size()));
		return rows.get(num);
	}

	@Override
	public int getLastRowNum() {
		return rows.size();
	}

	@Override
	public int getPhysicalNumberOfRows() {
		return rows.size();
	}

	@Override
	public int addMergedRegion(CellRangeAddress range) {
		return 0;
	}

	@Override
	public void autoSizeColumn(int column) {
	}

	///// unsupported operations

	@Override
	public int addMergedRegionUnsafe(CellRangeAddress arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addValidationData(DataValidation arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void autoSizeColumn(int arg0, boolean arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Drawing<?> createDrawingPatriarch() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createFreezePane(int arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createFreezePane(int arg0, int arg1, int arg2, int arg3) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createSplitPane(int arg0, int arg1, int arg2, int arg3, int arg4) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellAddress getActiveCell() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getAutobreaks() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Comment getCellComment(CellAddress arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<CellAddress, ? extends Comment> getCellComments() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int[] getColumnBreaks() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnOutlineLevel(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellStyle getColumnStyle(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnWidth(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getColumnWidthInPixels(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DataValidationHelper getDataValidationHelper() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<? extends DataValidation> getDataValidations() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getDefaultColumnWidth() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short getDefaultRowHeight() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getDefaultRowHeightInPoints() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getDisplayGuts() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Drawing<?> getDrawingPatriarch() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFirstRowNum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getFitToPage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Footer getFooter() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getForceFormulaRecalculation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Header getHeader() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getHorizontallyCenter() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Hyperlink getHyperlink(CellAddress arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Hyperlink getHyperlink(int arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<? extends Hyperlink> getHyperlinkList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short getLeftCol() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getMargin(short arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellRangeAddress getMergedRegion(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<CellRangeAddress> getMergedRegions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNumMergedRegions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public PaneInformation getPaneInformation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public PrintSetup getPrintSetup() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getProtect() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellRangeAddress getRepeatingColumns() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellRangeAddress getRepeatingRows() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int[] getRowBreaks() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getRowSumsBelow() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getRowSumsRight() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getScenarioProtect() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SheetConditionalFormatting getSheetConditionalFormatting() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short getTopRow() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getVerticallyCenter() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Workbook getWorkbook() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void groupColumn(int arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void groupRow(int arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isColumnBroken(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isColumnHidden(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDisplayFormulas() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDisplayGridlines() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDisplayRowColHeadings() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDisplayZeros() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPrintGridlines() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPrintRowAndColumnHeadings() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRightToLeft() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRowBroken(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSelected() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void protectSheet(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellRange<? extends Cell> removeArrayFormula(Cell arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeColumnBreak(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeMergedRegion(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeMergedRegions(Collection<Integer> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeRow(Row arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeRowBreak(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setActiveCell(CellAddress arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellRange<? extends Cell> setArrayFormula(String arg0, CellRangeAddress arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AutoFilter setAutoFilter(CellRangeAddress arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAutobreaks(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setColumnBreak(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setColumnGroupCollapsed(int arg0, boolean arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setColumnHidden(int arg0, boolean arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setColumnWidth(int arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDefaultColumnStyle(int arg0, CellStyle arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDefaultColumnWidth(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDefaultRowHeight(short arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDefaultRowHeightInPoints(float arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDisplayFormulas(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDisplayGridlines(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDisplayGuts(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDisplayRowColHeadings(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDisplayZeros(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFitToPage(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setForceFormulaRecalculation(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHorizontallyCenter(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMargin(short arg0, double arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPrintGridlines(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPrintRowAndColumnHeadings(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRepeatingColumns(CellRangeAddress arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRepeatingRows(CellRangeAddress arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRightToLeft(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRowBreak(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRowGroupCollapsed(int arg0, boolean arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRowSumsBelow(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRowSumsRight(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSelected(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setVerticallyCenter(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setZoom(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shiftColumns(int arg0, int arg1, int arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shiftRows(int arg0, int arg1, int arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shiftRows(int arg0, int arg1, int arg2, boolean arg3, boolean arg4) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void showInPane(int arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void ungroupColumn(int arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void ungroupRow(int arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void validateMergedRegions() {
		throw new UnsupportedOperationException();
	}

}