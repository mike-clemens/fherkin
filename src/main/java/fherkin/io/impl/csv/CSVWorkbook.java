package fherkin.io.impl.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Class representing a CSV workbook.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class CSVWorkbook implements Workbook {
	
	private CSVSheet sheet = new CSVSheet();
	
	@Override
	public int getNumberOfSheets() {
		return 1;
	}

	@Override
	public Sheet getSheetAt(int index) {
		return sheet;
	}

	@Override
	public Sheet createSheet() {
		return sheet;
	}

	@Override
	public Sheet createSheet(String name) {
		return sheet;
	}

	@Override
	public Iterator<Sheet> sheetIterator() {
		List<Sheet> list = new ArrayList<Sheet>();
		list.add(sheet);
		return list.iterator();
	}

	///// unsupported operations

	@Override
	public Iterator<Sheet> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int addOlePackage(byte[] arg0, String arg1, String arg2, String arg3) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int addPicture(byte[] arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addToolPack(UDFFinder arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Sheet cloneSheet(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellStyle createCellStyle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public DataFormat createDataFormat() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Font createFont() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Name createName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Font findFont(boolean arg0, short arg1, short arg2, String arg3, boolean arg4, boolean arg5, short arg6, byte arg7) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getActiveSheetIndex() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<? extends Name> getAllNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<? extends PictureData> getAllPictures() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellStyle getCellStyleAt(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CreationHelper getCreationHelper() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFirstVisibleTab() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Font getFontAt(short arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Font getFontAt(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getForceFormulaRecalculation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MissingCellPolicy getMissingCellPolicy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Name getName(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Name getNameAt(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNameIndex(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<? extends Name> getNames(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNumCellStyles() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short getNumberOfFonts() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNumberOfFontsAsInt() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNumberOfNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getPrintArea(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Sheet getSheet(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getSheetIndex(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getSheetIndex(Sheet arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSheetName(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SheetVisibility getSheetVisibility(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SpreadsheetVersion getSpreadsheetVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isHidden() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSheetHidden(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSheetVeryHidden(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int linkExternalWorkbook(String arg0, Workbook arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeName(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeName(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeName(Name arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removePrintArea(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeSheetAt(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setActiveSheet(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFirstVisibleTab(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setForceFormulaRecalculation(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHidden(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMissingCellPolicy(MissingCellPolicy arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPrintArea(int arg0, String arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPrintArea(int arg0, int arg1, int arg2, int arg3, int arg4) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSelectedTab(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSheetHidden(int arg0, boolean arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSheetName(int arg0, String arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSheetOrder(String arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSheetVisibility(int arg0, SheetVisibility arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(OutputStream arg0) throws IOException {
		throw new UnsupportedOperationException();
	}

}