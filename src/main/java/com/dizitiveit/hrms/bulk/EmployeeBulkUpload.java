package com.dizitiveit.hrms.bulk;

import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.time.ZoneId;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dizitiveit.hrms.dao.BankDetailsDao;
import com.dizitiveit.hrms.dao.BranchDao;
import com.dizitiveit.hrms.dao.DeptDao;
import com.dizitiveit.hrms.dao.DesigDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.RolesDao;
import com.dizitiveit.hrms.model.BankDetails;
import com.dizitiveit.hrms.model.Branch;
import com.dizitiveit.hrms.model.Department;
import com.dizitiveit.hrms.model.Designation;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.Roles;
import com.dizitiveit.hrms.pojo.Responses;


@RestController
@RequestMapping("/bulkEmployee")

public class EmployeeBulkUpload {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private BankDetailsDao bankDetailsDao;
	
	@Autowired
	private DesigDao desigDao;
	
	@Autowired
	private DeptDao deptDao;
	
	@Autowired
	private RolesDao rolesDao;
	
	@Autowired
	private BranchDao branchDao;
	

	@PostMapping("/bulkUploadEmployee")
	public ResponseEntity<?>  bulkUploadEmployee(@RequestParam ("excelfile") MultipartFile excelfile)
	{
	 try {
         int i = 1;
         //Creates a workbook object from the uploaded excelfile
      
         XSSFWorkbook workbook = new  XSSFWorkbook(excelfile.getInputStream());
         //Creates a worksheet object representing the first sheet
         
         XSSFSheet worksheet = workbook.getSheetAt(0);
         
         //Reads the data in excel file until last row is encountered
         System.out.println(worksheet.getLastRowNum());
         DataFormatter formatter = new DataFormatter();
         String errorEmpid="";
         
         while (i <= worksheet.getLastRowNum()) 
         {
        	 XSSFRow row = worksheet.getRow(i++);
	           
        	 System.out.println(i+","+worksheet.getLastRowNum());
             //Creates an object for the Candidate  Model
        	  BankDetails bankDetails = new BankDetails();
			  Employee employee = new Employee();
			  String employeeId=formatter.formatCellValue(row.getCell(0));
			  Employee existingEmployee=employeeDao.findByEmployeeId(employeeId);
			  System.out.println(employeeId);
				
        	if(existingEmployee==null)
        	{	 
        	 employee.setEmployeeId(employeeId);
             employee.setFirstName(formatter.formatCellValue(row.getCell(1)));
             System.out.println(employee.getFirstName());
             if(row.getCell(2)!=null)
             {
        	 employee.setLastName(formatter.formatCellValue(row.getCell(2)));
        	}
        	 System.out.println(employee.getLastName());
      
        	 String empPhn = formatter.formatCellValue(row.getCell(3));
        	 employee.setPhoneNumber(empPhn);
        	 System.out.println(employee.getPhoneNumber());
        	 employee.setOfficialEmailId(formatter.formatCellValue(row.getCell(4)));
        	 System.out.println(employee.getOfficialEmailId());
        	 employee.setGender(formatter.formatCellValue(row.getCell(5)));
        	 employee.setAdharNumber(formatter.formatCellValue(row.getCell(6)));
        	 employee.setPanCardNumber(formatter.formatCellValue(row.getCell(7)));	 
        	 employee.setDeputeBranch(formatter.formatCellValue(row.getCell(8)));
        	 employee.setLocation(formatter.formatCellValue(row.getCell(9)));
        	
              if(row.getCell(10)!=null)
              {
        	 Date dateVal=row.getCell(10).getDateCellValue();
	         //LocalDate localDate = dateVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	         employee.setDateOfbirth(dateVal);
              }
        	 
              if(row.getCell(11)!=null)
              {
			 Date dateVali=row.getCell(11).getDateCellValue();
	         //LocalDate localDates = dateVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	         employee.setDateOfJoining(dateVali);
              }
              
	        
        	   
        	 employee.setPresentAddress(row.getCell(12).getStringCellValue());
        	 
        	 String roleName = formatter.formatCellValue(row.getCell(13));
        	 Roles roles= rolesDao.findByroleName(roleName);
        	 employee.setRoles(roles);
        	 
        	 String branchName = formatter.formatCellValue(row.getCell(14));
        	 Branch branch= branchDao.findBybranchName(branchName);
        	 employee.setBranch(branch);
        	 
        	 String deptName = formatter.formatCellValue(row.getCell(15));
        	 Department dept= deptDao.findByDeptName(deptName);
        	 employee.setDepartment(dept);
        	 
        	 String desigName = formatter.formatCellValue(row.getCell(16));
        	 //System.out.println("Designation name is:" +desigName);
        	 Designation desg= desigDao.findByDesigName(desigName);
        	 //System.out.println(desg.getDesigName());
        	 employee.setDesg(desg);
        	 
        	 String supervisorId = formatter.formatCellValue(row.getCell(17));
        	 Employee supervisor=employeeDao.findByEmployeeId(supervisorId);
        	 employee.setSupervisor(supervisor);
        	 employee.setStatus(true);
        	 employeeDao.save(employee);
            	
        	
        	Employee employeeBank = employeeDao.findByEmployeeId(employeeId);
        	BankDetails bankDetailsExisting = bankDetailsDao.findByEmployee(employeeBank);
        	if(bankDetailsExisting==null)
        	{
        	System.out.println(existingEmployee);
        	  bankDetails.setAccountHolderName(formatter.formatCellValue(row.getCell(18)));
              bankDetails.setIfscCode(formatter.formatCellValue(row.getCell(19)));
              bankDetails.setAccountNumber(formatter.formatCellValue(row.getCell(20)));
              bankDetails.setBankName(formatter.formatCellValue(row.getCell(21)));
              bankDetails.setBranchName(formatter.formatCellValue(row.getCell(22))); 
              bankDetails.setEmployee(employeeBank);
       
              bankDetailsDao.save(bankDetails);
        	 
        	} 
            }
        	else
        	{
        		errorEmpid=errorEmpid+","+existingEmployee.getEmployeeId();
        	}
         }
         if(errorEmpid=="")
         {
        return ResponseEntity.ok(Responses.builder().message("Employee Bulk Uploaded Successfully").build()); 
         }
         else
         {
        	 return ResponseEntity.ok(Responses.builder().message("Employees saved except "+errorEmpid+" because they already exists").build()); 
        	 
         }
         
	 } 
	 
	 catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Exception has raised:" + e.getMessage());
     } 
 }
}
