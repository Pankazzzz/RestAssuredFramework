package api.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLUtility {
	
	public FileInputStream fileInputStream;
	public FileOutputStream fOutputStream;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row;
	public XSSFCell cell;
	public CellStyle style;
	String path;
	
	public XLUtility(String path)
	{
		this.path=path;
	}
	
	public int getRowcount(String sheetName) throws IOException
	{
		fileInputStream = new FileInputStream(path);
		workbook = new XSSFWorkbook(fileInputStream);
		sheet=workbook.getSheet(sheetName);
		int rowcount = sheet.getLastRowNum();
		workbook.close();
		fileInputStream.close();
		return rowcount;
	}
	
	public int getCellCount(String sheetName,int rownum) throws IOException
	{
		fileInputStream=new FileInputStream(path);
		workbook=new XSSFWorkbook(fileInputStream);
		sheet=workbook.getSheet(sheetName);
		row=sheet.getRow(rownum);
		int cellCount = row.getLastCellNum();
		workbook.close();
		fileInputStream.close();
		return cellCount;
		
	}
	
	public String getCellData(String sheetName,int rownum,int column) throws IOException {
	    fileInputStream = new FileInputStream(path);
	    workbook = new XSSFWorkbook(fileInputStream);
	    sheet = workbook.getSheet(sheetName);
	    row = sheet.getRow(rownum);
	    cell = row.getCell(column);

	    DataFormatter formatter = new DataFormatter();
	    String data;
	    try {
	        data = formatter.formatCellValue(cell); // Returns the formatted value of a cell as a String regardless of the data type
	    } catch(Exception e) {
	        data = "";
	    }

	    workbook.close();
	    fileInputStream.close();
	    return data;
	}

	public void setCellData(String sheetName, int rownum, int column, String data) throws IOException {
	    
		File xlfile = new File(path);
	    if (!xlfile.exists()) { // If file not exists then create new file
	        workbook = new XSSFWorkbook();
	        fOutputStream = new FileOutputStream(path);
	        workbook.write(fOutputStream);
	    }

	    fileInputStream = new FileInputStream(path);
	    workbook = new XSSFWorkbook(fileInputStream);

	    if (workbook.getSheetIndex(sheetName) == -1) { // If sheet not exists then create new Sheet
	        workbook.createSheet(sheetName);
	    }
	    sheet = workbook.getSheet(sheetName);

	    if (sheet.getRow(rownum) == null) { // If row not exists then create new Row
	        sheet.createRow(rownum);
	    }
	    row = sheet.getRow(rownum);
	    cell = row.createCell(column);
	    cell.setCellValue(data);
	    fOutputStream=new FileOutputStream(path);
	    workbook.write(fOutputStream);
	    workbook.close();
	    fileInputStream.close();
	    fOutputStream.close();
	}
	
	public void fillGreenColor(String sheetName, int rownum, int column) throws IOException {
	    fileInputStream = new FileInputStream(path);
	    workbook = new XSSFWorkbook(fileInputStream);
	    sheet = workbook.getSheet(sheetName);

	    row = sheet.getRow(rownum);
	    cell = row.getCell(column);

	    style = workbook.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    cell.setCellStyle(style);
	    workbook.write(fOutputStream);
	    workbook.close();
	    fileInputStream.close();
	    fOutputStream.close();
	}

	public void fillRedColor(String sheetName, int rownum, int column) throws IOException {
	    fileInputStream = new FileInputStream(path);
	    workbook = new XSSFWorkbook(fileInputStream);
	    sheet = workbook.getSheet(sheetName);

	    row = sheet.getRow(rownum);
	    cell = row.getCell(column);

	    style = workbook.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.RED.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    cell.setCellStyle(style);
	    workbook.write(fOutputStream);
	    workbook.close();
	    fileInputStream.close();
	    fOutputStream.close();
	}


	
	
	  
}
