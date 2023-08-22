package com.dizitiveit.hrms.bulk;

//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

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

import com.dizitiveit.hrms.dao.AttendanceDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.model.Attendance;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/bulkAttendance")

public class AttendanceBulkUpload {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private AttendanceDao attendanceDao;

	@PostMapping("/bulkUploadAttendance")
	public ResponseEntity<?>  bulkUploadAttendance(@RequestParam ("excelfile") MultipartFile excelfile)
	{
	 try {
         int i = 1;
         //Creates a workbook object from the uploaded excelfile
      
         XSSFWorkbook workbook = new  XSSFWorkbook(excelfile.getInputStream());
         //Creates a worksheet object representing the first sheet
         
         XSSFSheet worksheet = workbook.getSheetAt(0);
         
         //Reads the data in excel file until last row is encountered
         System.out.println(worksheet.getLastRowNum());
        
         
         while (i <= worksheet.getLastRowNum()) 
         {
        	 XSSFRow row = worksheet.getRow(i++);
	           
        	 System.out.println(i+","+worksheet.getLastRowNum());
             //Creates an object for the Candidate  Model
        	 String employeeId=row.getCell(0).getStringCellValue();
        	 Employee employee = employeeDao.findByEmployeeId(employeeId); 
        	Attendance attendance = new Attendance();
        	 Date dateVal=row.getCell(3).getDateCellValue();
             //Creates an object representing a single row in excel
        	 //i=i+1;
        	Attendance attendanceExisting = attendanceDao.findByattendanceDay(employee.getEmplyoeeCode(),dateVal);
        	if(attendanceExisting==null)
        	{	
        	
           
             //Sets the Read data to the model class 
               
           //System.out.println(row.getFirstCellNum());
        		
         
			attendance.setEmployee(employee);
			
			 LocalDate localDate = dateVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	           attendance.setAttendanceDay(dateVal);
	           attendance.setCreatedAt(dateVal);
	           String TotalHours = row.getCell(4).getStringCellValue();
	           attendance.setTotalHours(TotalHours);
	           
               String InTime = row.getCell(1).getStringCellValue();
               SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
             // Time val=(Time) sdf.parse(InTime);
              LocalTime localInTime = LocalTime.parse(InTime);
              attendance.setInTime(localInTime);
              System.out.println(localInTime);
              
              String OutTime = row.getCell(2).getStringCellValue();
              LocalTime localOutTime = LocalTime.parse(OutTime);
              attendance.setOutTime(localOutTime);
              System.out.println(localOutTime);
              attendanceDao.save(attendance);
       
         }
         }
         return ResponseEntity.ok(Responses.builder().message("Saved Sucessfully").build());  
	 } 
	 
	 catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Exception has raised:" + e.getMessage());
     } 
	
}
}
