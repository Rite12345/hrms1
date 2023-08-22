package com.dizitiveit.hrms.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.time.ZoneId;


import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.dizitiveit.hrms.dao.AllowancesDao;
import com.dizitiveit.hrms.dao.BankDetailsDao;
import com.dizitiveit.hrms.dao.DeductionsDao;
import com.dizitiveit.hrms.dao.EmployeeAllowancesDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.EmployeeDeductionsDao;
import com.dizitiveit.hrms.dao.LeaveManagementDao;
import com.dizitiveit.hrms.dao.UnsavedPayrollDataDao;
import com.dizitiveit.hrms.model.Allowances;
import com.dizitiveit.hrms.model.BankDetails;
import com.dizitiveit.hrms.model.Deductions;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.EmployeeAllowances;
import com.dizitiveit.hrms.model.EmployeeDeductions;
import com.dizitiveit.hrms.model.LeaveManagement;
import com.dizitiveit.hrms.model.UnsavedPayrollData;
import com.dizitiveit.hrms.pojo.Responses;



@RequestMapping("/bulk")
@RestController
public class BulkUploadController {
	
	@Value("${file.excel}")
	private String fileBasePath;
	
	@Autowired
	private EmployeeAllowancesDao empAllowancesDao;
	
	@Autowired
	private EmployeeDeductionsDao employeeDeductionDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private LeaveManagementDao leaveManagementDao;
	
	@Autowired
	private BankDetailsDao bankDetailsDao;
	
	@Autowired
	private AllowancesDao allowancesDao;
	
	@Autowired
	private DeductionsDao deductionsDao;
	
	@Autowired
	private UnsavedPayrollDataDao unsavedPayrollDataDao;
	
	

	@RequestMapping(value = "/bulkPayrollSave", method = RequestMethod.POST)
	public ResponseEntity<?>  bulkPayrollSave(@RequestParam ("excelfile2") MultipartFile excelfile2)

	{	 
		 try {
	         int i = 1;
	         //Creates a workbook object from the uploaded excelfile
	      
	         XSSFWorkbook workbook = new  XSSFWorkbook(excelfile2.getInputStream());
	         //Creates a worksheet object representing the first sheet
	         
	         XSSFSheet worksheet = workbook.getSheetAt(0);
	         
	         //Reads the data in excel file until last row is encountered
	         System.out.println(worksheet.getLastRowNum());
	         
	         while (i <= worksheet.getLastRowNum()) 
	         {
	        	 System.out.println(i+","+worksheet.getLastRowNum());
	             //Creates an object for the Candidate  Model
	        	 
	        	 EmployeeDeductions empDeductions = new EmployeeDeductions();
	        	 LeaveManagement leaveManagement = new LeaveManagement();
	             //Creates an object representing a single row in excel
	        	 //i=i+1;
	        	 
	        	
	           XSSFRow row = worksheet.getRow(i++);
	           
	             //Sets the Read data to the model class 
	           
	           //System.out.println(row.getFirstCellNum());
	           String employeeId=row.getCell(0).getStringCellValue();
	           Employee employee = employeeDao.findByEmployeeId(employeeId);
				/*
				 * if(employee.getLocation() == null && employee.getDeputeBranch() == null) {
				 * employee.setLocation(row.getCell(12).getStringCellValue());
				 * employee.setDeputeBranch(row.getCell(13).getStringCellValue());
				 * employeeDao.save(employee); }
				 */
	           Date dateVal=row.getCell(12).getDateCellValue();
	             
	           LocalDate localDate = dateVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				long month = localDate.getMonthValue();
				long year = localDate.getYear();
	           List<String> allowancesList= new ArrayList<>(List.of("HRA", "Conveyance","CityAllowances","pfEmployeerContributor","BasicSalary"));
	           UnsavedPayrollData unsavedPayrollData = new UnsavedPayrollData();
	          
	           boolean existingRecord=false;
	           for(int j=0;j<allowancesList.size();j++ )
	           {
		         
		              System.out.println(dateVal);  
	        	   EmployeeAllowances empAllowances = new EmployeeAllowances();
	        	  System.out.println(row.getCell(0).getStringCellValue());
	        	   empAllowances.setEmployee(employee);
		           Allowances allowancesHra = allowancesDao.findByAllowanceName(allowancesList.get(j));
		           EmployeeAllowances empallowancesExisting = empAllowancesDao.findByExistingRecord(month, year,employee.getEmplyoeeCode(),allowancesHra.getAllowanceId());
		           if(empallowancesExisting==null)
		           {
		           empAllowances.setAllowances(allowancesHra);
		           double Hra=row.getCell(j+2).getNumericCellValue();
		           empAllowances.setValue(Hra);
		           empAllowances.setEffectFromDate(dateVal);
		           SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		           isoFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
					String  payrollDate = isoFormat.format(new Date());
		           Date date = isoFormat.parse(payrollDate);
		           empAllowances.setCreatedAt(new Date());
		           empAllowancesDao.save(empAllowances);
		           }
		           else {
		        	  
		        	   existingRecord=true;
		        	   String allowanceName = allowancesHra.getAllowanceName();
		        	   switch(allowanceName){  
		        	    //Case statements  
		        	    case "HRA": unsavedPayrollData.setHRA(row.getCell(3).getNumericCellValue());
		        	    break;  
		        	    case "Conveyance": unsavedPayrollData.setConveyance(row.getCell(4).getNumericCellValue());
		        	    break;  
		        	    case "CityAllowances": unsavedPayrollData.setCityAllowances(row.getCell(5).getNumericCellValue());
		        	    break;  
		        	    case "pfEmployeerContributor": unsavedPayrollData.setPfEmployeerContributor(row.getCell(6).getNumericCellValue());
		        	    break;
		        	    case "BasicSalary": unsavedPayrollData.setBasicSalary(row.getCell(7).getNumericCellValue());
		        	    break;
		                    
		        	    }  
		        	   
		        	   
		           }
	           }
	           
	           empDeductions.setEmployee(employee);
	           double pt=row.getCell(7).getNumericCellValue();
	           Deductions deductions = deductionsDao.findByDeductionName("PT");
	           EmployeeDeductions empdeductionsExisting = employeeDeductionDao.findByExistingDeduction(month, year,employee.getEmplyoeeCode(),deductions.getDeductionId());
	           if(empdeductionsExisting==null)
	           {
	           empDeductions.setDeductions(deductions);
	           
	           empDeductions.setValue(pt);
	           empDeductions.setEffectFromDate(dateVal);
	           employeeDeductionDao.save(empDeductions);
	           }
	           else
	           {
	        	   existingRecord=true;
	        	   unsavedPayrollData.setPT(pt);
	           }
	           LeaveManagement leaveExisting = leaveManagementDao.findByExistingLeave(month, year,employee.getEmplyoeeCode());
	           Integer cl=(int) row.getCell(8).getNumericCellValue();
	           Integer sl=(int) row.getCell(9).getNumericCellValue();
	           Integer lop=(int) row.getCell(10).getNumericCellValue();
	           Integer dayPaid=(int) row.getCell(11).getNumericCellValue();
	          if(leaveExisting==null)
	          {
	        
	         leaveManagement.setCasualLeaves(cl);
	         
	         leaveManagement.setEmployee(employee);
	         
	         
	         leaveManagement.setSickLeaves(sl);
	         
	         leaveManagement.setCreatedAt(dateVal);
	         
	         
	         leaveManagement.setLOP(lop);
	     
	         leaveManagement.setTotalDays(dayPaid);
	         leaveManagementDao.save(leaveManagement);
	          }
	          else
	          {
	        	  existingRecord=true;
	        	  unsavedPayrollData.setCasualLeaves(cl);
	        	  unsavedPayrollData.setSickLeaves(sl);
	        	  unsavedPayrollData.setLOP(lop);
	        	  unsavedPayrollData.setDayspaid(dayPaid);
	          }
	          if(existingRecord==true)
              {
            	  unsavedPayrollData.setEmployeeId(employeeId);
            	  unsavedPayrollData.setEmployeeName(employee.getFirstName());
            	  //unsavedPayrollData.setLocation(row.getCell(12).getStringCellValue());
 				  //unsavedPayrollData.setDeputeBranch(row.getCell(13).getStringCellValue());
            	  unsavedPayrollData.setCreatedAt(dateVal);
            	  unsavedPayrollDataDao.save(unsavedPayrollData); 
              }
				/*
				 * BankDetails bankBetailsExists = bankDetailsDao.findByEmployee(employee);
				 * 
				 * if(bankBetailsExists==null) { BankDetails bankBetails = new BankDetails();
				 * bankBetails.setEmployee(employee);
				 * bankBetails.setBankName(row.getCell(12).getStringCellValue());
				 * bankBetails.setAccountNumber(row.getCell(13).getStringCellValue());
				 * bankBetails.setBranchName(row.getCell(14).getStringCellValue());
				 * bankBetails.setLocation(row.getCell(15).getStringCellValue());
				 * bankBetails.setDeputeBranch(row.getCell(16).getStringCellValue());
				 * bankDetailsDao.save(bankBetails); } else { existingRecord=true;
				 * unsavedPayrollData.setBankName(row.getCell(12).getStringCellValue());
				 * unsavedPayrollData.setAccountNumbe(row.getCell(13).getStringCellValue());
				 * unsavedPayrollData.setBaseBranch(row.getCell(14).getStringCellValue()) ;
				 * unsavedPayrollData.setLocation(row.getCell(15).getStringCellValue());
				 * unsavedPayrollData.setDeputeBranch(row.getCell(16).getStringCellValue()); }
				 */
	             
	       
	         }
	         return ResponseEntity.ok(Responses.builder().message("PayRoll Data Uploaded Sucessfully").build());  
		 } 
		 
		 catch (Exception e) {
	         e.printStackTrace();
	         throw new RuntimeException("Exception has raised:" + e.getMessage());
	     } 
		
	}
	
	@GetMapping("/download")
	public ResponseEntity<?> download(){
		String fileName = "PayrollExcelTemplate.xlsx";
		Path path = Paths.get(fileBasePath+"/"+fileName);
		//Path path = Paths.get(fileBasePath+"\\"+fileName);
		System.out.println("path is"+path);
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return  ResponseEntity.badRequest().body(e.getMessage());
		}
		
		 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
			        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
			        .body(resource);
	}
	

}
