package fherkin.io.impl.csv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Class representing a row in a CSV workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class CSVRow implements Row {
	
	private Sheet sheet;
	private int rowNum;
	private List<Cell> cells = new ArrayList<Cell>();
	
	public CSVRow(Sheet sheet, int rowNum) {
		this.sheet = sheet;
		this.rowNum = rowNum;
	}

	@Override
	public Sheet getSheet() {
		return sheet;
	}

	@Override
	public int getRowNum() {
		return rowNum;
	}

	@Override
	public Iterator<Cell> iterator() {
		return cells.iterator();
	}

	@Override
	public Iterator<Cell> cellIterator() {
		return iterator();
	}

	@Override
	public Cell getCell(int num) {
		return cells.size() < num ? null : cells.get(num);
	}
	
	@Override
	public Cell createCell(int num) {
		while(cells.size() <= num)
			cells.add(new CSVCell(this, cells.size()));
		return cells.get(num);
	}

	@Override
	public int getPhysicalNumberOfCells() {
		return cells.size();
	}

	///// unsupported operations

	@Override
	public Cell createCell(int arg0, CellType arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cell getCell(int arg0, MissingCellPolicy arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public short getFirstCellNum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short getHeight() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getHeightInPoints() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short getLastCellNum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getOutlineLevel() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellStyle getRowStyle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getZeroHeight() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFormatted() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeCell(Cell arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHeight(short arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHeightInPoints(float arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRowNum(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRowStyle(CellStyle arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setZeroHeight(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shiftCellsLeft(int arg0, int arg1, int arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shiftCellsRight(int arg0, int arg1, int arg2) {
		throw new UnsupportedOperationException();
	}

}