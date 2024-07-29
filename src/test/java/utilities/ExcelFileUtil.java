package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelFileUtil {
	Workbook wb;

	// constructor for reading excel path
	public ExcelFileUtil(String Excelpath) throws Throwable {
		FileInputStream fi = new FileInputStream(Excelpath);
		wb = WorkbookFactory.create(fi);
	}

	// count no of rows in a sheet
	public int rowCount(String sheetName) {
		return wb.getSheet(sheetName).getLastRowNum();
	}

	// method for reading the cell data
	// if any cell contains integer type converting to string in if block
	public String getCellData(String sheetName, int row, int column) {
		String data;
		if (wb.getSheet(sheetName).getRow(row).getCell(column).getCellType() == CellType.NUMERIC) {
			int celldata = (int) wb.getSheet(sheetName).getRow(row).getCell(column).getNumericCellValue();
			data = String.valueOf(celldata);
		} else {
			data = wb.getSheet(sheetName).getRow(row).getCell(column).getStringCellValue();
			// if any cell contains string type
		}
		return data;

	}

	// method for set data->write data
	public void setCellData(String sheetName, int row, int column, String status, String writeExcelPath) throws Throwable {
		// get sheet from workbook
		Sheet ws = wb.getSheet(sheetName);
		// get row from sheet
		Row rowNum = ws.getRow(row);
		// create cell
		Cell cell = rowNum.createCell(column);
		// write status
		cell.setCellValue(status);
		if (status.equalsIgnoreCase("pass")) {
			// use cell style
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			// set to green color
			font.setColor(IndexedColors.GREEN.getIndex());
			// store that green color font into style object
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);
		} else if (status.equalsIgnoreCase("fail")) {
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setColor(IndexedColors.RED.getIndex());
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);
		} else if (status.equalsIgnoreCase("Blocked")) {
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setColor(IndexedColors.BLUE.getIndex());
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);
		}
		FileOutputStream fo = new FileOutputStream(writeExcelPath);
		wb.write(fo);
	}

	public static void main(String[] args) throws Throwable {
		ExcelFileUtil xl = new ExcelFileUtil("D:/sample.xlsx");
		int rc = xl.rowCount("Employee Details");
		System.out.println(rc);
		for (int i = 1; i <= rc; i++) {
			String fname = xl.getCellData("Employee Details", i, 0);
			String mname = xl.getCellData("Employee Details", i, 1);
			String lname = xl.getCellData("Employee Details", i, 2);
			String eid = xl.getCellData("Employee Details", i, 3);
			System.out.println(fname + " " + mname + " " + lname + " " + eid);
			// write pass
//			xl.setCellData("Employee Details", i, 4, "pass", "D:/Results.xlsx");
//			xl.setCellData("Employee Details", i, 4, "fail", "D:/Results.xlsx");
			xl.setCellData("Employee Details", i, 4, "Blocked", "D:/Results.xlsx");
		}

	}

}
