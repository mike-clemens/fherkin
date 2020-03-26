package fherkin.io.impl.csv;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Class representing a cell in a CSV workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class CSVCell implements Cell {
	
	private Row row;
	private int columnIndex;
	private String value;
	
	public CSVCell(Row row, int columnIndex) {
		this.row = row;
		this.columnIndex = columnIndex;
	}

	@Override
	public Row getRow() {
		return row;
	}

	@Override
	public int getColumnIndex() {
		return columnIndex;
	}

	@Override
	public CellType getCellType() {
		return CellType.STRING;
	}

	@Override
	public String getStringCellValue() {
		return value;
	}

	@Override
	public void setCellValue(String value) {
		this.value = value;
	}
	
	@Override
	public void setCellValue(double value) {
		this.value = Double.toString(value);
		if(this.value.endsWith(".0"))
			this.value = this.value.substring(0, this.value.length() - 2);
	}

	///// unsupported operations

	@Override
	public CellAddress getAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellRangeAddress getArrayFormulaRange() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getBooleanCellValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellType getCachedFormulaResultType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellType getCachedFormulaResultTypeEnum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Comment getCellComment() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCellFormula() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellStyle getCellStyle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellType getCellTypeEnum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getDateCellValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte getErrorCellValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Hyperlink getHyperlink() {
		throw new UnsupportedOperationException();
	}

	@Override
	public LocalDateTime getLocalDateTimeCellValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getNumericCellValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public RichTextString getRichStringCellValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRowIndex() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Sheet getSheet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPartOfArrayFormulaGroup() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeCellComment() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeFormula() throws IllegalStateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeHyperlink() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAsActiveCell() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setBlank() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellComment(Comment arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellErrorValue(byte arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellFormula(String arg0) throws FormulaParseException, IllegalStateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellStyle(CellStyle arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellType(CellType arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellValue(Date arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellValue(LocalDateTime arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellValue(Calendar arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellValue(RichTextString arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCellValue(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHyperlink(Hyperlink arg0) {
		throw new UnsupportedOperationException();
	}

}