package com.dizitiveit.hrms.Reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.dizitiveit.hrms.model.UnsavedPayrollData;
import com.dizitiveit.hrms.service.MyUserDetailsService;

public class ExcelGenerator {

	
	@Autowired
	private static MyUserDetailsService userService;
	
	 public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = {"EmployeeId","Employee Name","HRA","Conveyance","City Allowance","Pf Employeer Contribution","BasicSalary","PT","Casual Leaves","Sick Leaves","Loss Of Pays","Days Paid" };
	  static String SHEET = "Unsaved Payroll Report";

	  public static ByteArrayInputStream transactionsToExcel(List<UnsavedPayrollData> unsavedPayrollList) {
		  Workbook workbook = new XSSFWorkbook(); 
	    try (
	    		ByteArrayOutputStream out = new ByteArrayOutputStream()
	    				;) {
	      Sheet sheet = workbook.createSheet(SHEET);
	      XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
	      cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
	      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


	      // Header
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	        cell.setCellStyle(cellStyle);
	      }

	      int rowIdx = 1;
	      for ( UnsavedPayrollData unsavedPayrollData : unsavedPayrollList) {
	        Row row = sheet.createRow(rowIdx++);
	       System.out.println(unsavedPayrollList.size());
	        row.createCell(0).setCellValue(unsavedPayrollData.getEmployeeId());
	        row.createCell(1).setCellValue(unsavedPayrollData.getEmployeeName());
	        row.createCell(2).setCellValue(unsavedPayrollData.getHRA());
	        row.createCell(3).setCellValue(unsavedPayrollData.getConveyance());
	        row.createCell(4).setCellValue(unsavedPayrollData.getCityAllowances());
	        row.createCell(5).setCellValue(unsavedPayrollData.getPfEmployeerContributor());
	        row.createCell(6).setCellValue(unsavedPayrollData.getBasicSalary());
	        row.createCell(7).setCellValue(unsavedPayrollData.getPT());
	        row.createCell(8).setCellValue(unsavedPayrollData.getCasualLeaves());
	        row.createCell(9).setCellValue(unsavedPayrollData.getSickLeaves());
	        row.createCell(10).setCellValue(unsavedPayrollData.getLOP());
	        row.createCell(11).setCellValue(unsavedPayrollData.getDayspaid());
	    
	      }

	      workbook.write(out);
	      
	      
	  
	      
	      return new ByteArrayInputStream(out.toByteArray());
	      
	    } 
	    catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
}
