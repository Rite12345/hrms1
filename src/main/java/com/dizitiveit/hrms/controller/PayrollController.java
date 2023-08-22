package com.dizitiveit.hrms.controller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.AllowancesDao;
import com.dizitiveit.hrms.dao.AttendanceDao;
import com.dizitiveit.hrms.dao.BankDetailsDao;
import com.dizitiveit.hrms.dao.DeductionsDao;
import com.dizitiveit.hrms.dao.EmployeeAllowancesDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.EmployeeDeductionsDao;
import com.dizitiveit.hrms.dao.LeaveBalanceDao;
import com.dizitiveit.hrms.dao.MonthlySalarySummaryDao;
import com.dizitiveit.hrms.dao.PaySlipLineItemDao;
import com.dizitiveit.hrms.dao.PayslipsDao;
import com.dizitiveit.hrms.model.AdditionalDetails;
import com.dizitiveit.hrms.model.Allowances;
import com.dizitiveit.hrms.model.Attendance;
import com.dizitiveit.hrms.model.BankDetails;
import com.dizitiveit.hrms.model.Deductions;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.EmployeeAllowances;
import com.dizitiveit.hrms.model.EmployeeDeductions;
import com.dizitiveit.hrms.model.LeaveBalance;
import com.dizitiveit.hrms.model.MonthlySalarySummary;
import com.dizitiveit.hrms.model.PaySlipLineItem;
import com.dizitiveit.hrms.model.Payslips;
import com.dizitiveit.hrms.pojo.Responses;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;


import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.LinkProvider;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.FileRetrieveImpl;
import com.itextpdf.tool.xml.XMLWorkerHelper;


@RequestMapping("/payRollGeneration")
@RestController
public class PayrollController {

	@Value("${file.fileBasePath}")
	private String fileBasePath;
	
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private PaySlipLineItemDao paySlipLineItemDao;
	
	@Autowired
	private MonthlySalarySummaryDao monthlySalarySummaryDao;
	
	@Autowired
	private LeaveBalanceDao leaveBalanceDao;
	
	@Autowired
	private BankDetailsDao bankDetailsDao; 
	
	@Autowired
	private PayslipsDao payslipsDao;
	
	@Autowired
	private AttendanceDao attendanceDao;
	
	@Autowired
	private EmployeeAllowancesDao employeeAllowancesDao;
	
	@Autowired
	private EmployeeDeductionsDao employeeDeductionsDao;
	
	@Autowired
	private AllowancesDao allowancesDao;
	
	@Autowired
	private DeductionsDao deductionsDao;

	@Autowired
	private AttendanceController attendance;
	
	@Autowired
	private PayrollController payRollGeneration;
	
	@RequestMapping(path = "/uploadpdf/{employeeId}/{month}/{year}")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response,@PathVariable String employeeId,@PathVariable String month,@PathVariable String year) throws IOException {

		 /* Send the response as downloadable PDF */
		try {
		String vFilePath = fileBasePath+"/"+employeeId; 
		System.out.println(vFilePath);
		File fileFolder = new File(vFilePath);
		if (!fileFolder.exists())
		  {

			fileFolder.canWrite();
			System.out.println("access of the folder"+fileFolder.canWrite());
			fileFolder.mkdir(); 
			System.out.print("path"+fileFolder);
			System.out.println("making directory"+fileFolder.mkdir());
			System.out.println("after directory");
		  }
		
		return ResponseEntity.ok(Responses.builder().message("folder created").build());	
		}
		catch(Exception E)
		{
			E.printStackTrace();
			return ResponseEntity.unprocessableEntity().body(E.getMessage());
		}

    }
	
	 @RequestMapping(method=RequestMethod.GET,value="/downloadPayslip/{month}/{year}/{employeeId}")
		public ResponseEntity<?> downloadPayslip(@PathVariable String month,@PathVariable String year,@PathVariable String employeeId) 
	 {
			
			try {

				String vFilePath = fileBasePath+"\\"+employeeId; 
		         String fileName = month+"_"+year+"Payslip.pdf";
		         
		         File file = new File(vFilePath+"\\"+month+"_"+year+"Payslip.pdf");
		         
		         if(file.exists())
		         {
		
			Path path = Paths.get(fileBasePath +"/" +employeeId+"/"+ fileName);
			System.out.println("After path");
			Resource resource = null;
			
				resource = new UrlResource(path.toUri());
				System.out.println(resource);
				System.out.println("After resource");
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_PDF)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		         }
		         else
		         {
		        	 
		        	 return ResponseEntity.badRequest().body(Responses.builder().message("Payslip is not generated for this month").build());
		         }
			

			}
			catch (MalformedURLException e) {
				e.printStackTrace();
				return  ResponseEntity.badRequest().body(e.getMessage());
			}
			catch(Exception E)
			{
				E.printStackTrace();
				return ResponseEntity.unprocessableEntity().body("error see logs");
			}
			
		}
	 
	 @PostMapping("/lop/{employeeId}/{month}/{year}")
	 public double lop(@PathVariable String employeeId, @PathVariable int month, @PathVariable int year)
	 {
		 Employee emp = employeeDao.findByEmployeeId(employeeId);
		 
		  Month months = Month.of(month);
		  System.out.println(months.name());
		  
		 MonthlySalarySummary monthlySalarySummary = monthlySalarySummaryDao.findWithYear(emp.getEmplyoeeCode(), months.name(), year);
		    
		  int workingDays = attendance.workingDaysCount(month, year,emp.getEmployeeId());
		  System.out.println("workingDays"+workingDays);
		 //  List<PaySlipLineItem> allowanceList = paySlipLineItemDao.findByItemtype( monthlySalarySummary.getMonthlySalaryID(), months.name(), year, true);
		   double totalAllowances=0;
		   double totalAllowance=0;
		   double Earnings =0;
		  // for(PaySlipLineItem allowance : allowanceList)
		   //{
			 //totalAllowances = totalAllowances + allowance.getItemValue();
			  
		  // }
		   Earnings = monthlySalarySummary.getTotalEarnings();
		
		   List<Attendance> attendanceList=attendanceDao.findByAbsentDays(month,year,emp.getEmplyoeeCode());  
		   System.out.println(attendanceList.size());
			int lopCount=workingDays-attendanceList.size();
			System.out.println("lop count"+lopCount);
			
			double lopPerDay = Earnings/30;
			System.out.print("lop per day is"+lopPerDay);
			double totalLopDeduction= lopPerDay*lopCount;
			System.out.print("salary is"+totalLopDeduction);
			
			 return totalLopDeduction;
		   
	 }
	 
	 
	 public String addMonthlySalary(@PathVariable String employeeId, @PathVariable int month, @PathVariable int year)
		{
			
			  Employee emp = employeeDao.findByEmployeeId(employeeId);
			
			  Month months = Month.of(month);
			  System.out.println(months.name());
			 
			 MonthlySalarySummary monthlySalarySummary = monthlySalarySummaryDao.findWithYear(emp.getEmplyoeeCode(), months.name(), year);
			
			 if(monthlySalarySummary!=null)
			 {
			  
			  int workingDays = attendance.workingDaysCount(month, year,emp.getEmployeeId());
			  System.out.println("workingDays"+workingDays);
			   List<PaySlipLineItem> allowanceList = paySlipLineItemDao.findByItemtype( monthlySalarySummary.getMonthlySalaryID(), months.name(), year, true);
			   double totalAllowances=0;
			   for(PaySlipLineItem allowance : allowanceList)
			   {
				 totalAllowances = totalAllowances + allowance.getItemValue();
				 
			   }
			 
			   List<Attendance> attendanceList=attendanceDao.findByAbsentDays(month,year,emp.getEmplyoeeCode());
			   System.out.println(attendanceList.size());
				int lopCount=workingDays-attendanceList.size();
				System.out.println("lop count"+lopCount);
				
				List<PaySlipLineItem> deductionsList = paySlipLineItemDao.findByItemtype( monthlySalarySummary.getMonthlySalaryID(), months.name(), year, false);
				double deductions = 0;
				
				double total = monthlySalarySummary.getBasicSalary() + totalAllowances;
				System.out.println("total"+total);
				
				double lopPerDay = total/30;
				double totalLopDeduction= lopPerDay*lopCount;
				System.out.print("lop amount is"+totalLopDeduction);
				System.out.print("lop per day is"+lopPerDay);
				System.out.println(deductionsList.size());
				for(PaySlipLineItem deduction : deductionsList)
				{
					deductions = deductions + deduction.getItemValue();
					 System.out.println("item value"+deduction.getItemValue());
					
				}
				System.out.println("deductions"+deductions);
				double totalDeductions = deductions+totalLopDeduction;
				System.out.println("total deductions"+totalDeductions);
				double netSalary = total - totalDeductions;
				System.out.println("netSalary"+netSalary);
				double roundNetSalary = Math.round(netSalary);
				System.out.println("round net salary"+roundNetSalary);
				monthlySalarySummary.setNetSalary(roundNetSalary);
				monthlySalarySummary.setTotalEarnings(Math.round(total));
				monthlySalarySummary.setTotalDeductions(Math.round(totalDeductions));
				monthlySalarySummary.setDaysPaid(workingDays);
				
			      
			    monthlySalarySummaryDao.save(monthlySalarySummary);
			       
			 }
	            return "saved";
			
	}
	
	 
	 
	 @PostMapping("/saveFile/{employeeId}/{month}/{year}")
		public ResponseEntity<?> saveFile(@PathVariable String employeeId,@PathVariable int month,@PathVariable int year)
		{
			Employee employee = employeeDao.findByEmployeeId(employeeId);
			 Month months = Month.of(month);
			  System.out.println(months.name());
			 Payslips payslip = payslipsDao.getByMonthAndYear(employee.getEmplyoeeCode(), months.name(), year);
			 if(payslip==null)
			 {
			     /* Do Business Logic*/
			        ByteArrayOutputStream target = new ByteArrayOutputStream();
			        /*Setup converter properties. */
			       
			        ConverterProperties properties = new ConverterProperties();
			        MediaDeviceDescription mediaDeviceDescription =
			            new MediaDeviceDescription(MediaType.ALL_VALUE);
			     
			        properties.setMediaDeviceDescription(mediaDeviceDescription);
			
			       
			        //String ADDRESSUrl = " http://localhost:8081/payRollPdf/viewPayslip/"+employeeId+"/"+month+"/"+year;
			       // String ADDRESSUrl = " http://localhost:8083/payRollPdf/viewPayslip/"+employeeId+"/"+month+"/"+year;
			        String ADDRESSUrl = "http://103.60.213.11:9001/payRollPdf/viewPayslip/"+employeeId+"/"+months.name()+"/"+year;
			      
			       
			        try {
						HtmlConverter.convertToPdf(new URL(ADDRESSUrl).openStream(), target,properties);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        /* extract output as bytes */
			        byte[] bytes = target.toByteArray();
				
				 Payslips pay = new Payslips();
				 pay.setEmployee(employee);
				 pay.setMonth(months.name());
				 pay.setYear(year);
				 pay.setContentType("application/pdf");
				 pay.setPayslip(bytes);
				 payslipsDao.save(pay);
			         return ResponseEntity.ok(Responses.builder().message("file Saved Sucessfully").build());
			 }
			 else {
		        	 return ResponseEntity.badRequest().body(Responses.builder().message("Already pdf exists for this month").build());
			 }
			
		}
	 
	 
	 
		@GetMapping("/getAttachment/{month}/{year}/{employeeId}")
		public ResponseEntity<?> getAttachment(@PathVariable int month,@PathVariable int year,@PathVariable String employeeId)
		{
			Employee employee = employeeDao.findByEmployeeId(employeeId);
			if(employee!=null)
			{
			 Month months = Month.of(month);
			
			System.out.println(employee.getEmplyoeeCode());
			 Payslips payslip = payslipsDao.getByMonthAndYear(employee.getEmplyoeeCode(), months.name(), year);
			
			 if (payslip!= null)
			 {
				 String fileName = month+"_"+year+"Payslip.pdf";
				 try
				 {
					 System.out.println("filename"+fileName);
			      return ResponseEntity.ok().contentType(MediaType.parseMediaType(payslip.getContentType()))
			          .header("x-suggested-filename", fileName)
			          .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" +fileName + "\"")
			          .body(new ByteArrayResource(payslip.getPayslip()));
				 }
				 catch(Exception E)
					{
						E.printStackTrace();
						return ResponseEntity.unprocessableEntity().body(E.getMessage());
					}
				 
			 }
			    else {
		        	 return ResponseEntity.badRequest().body(Responses.builder().message("Payslip is not generated for this month").build());
			    }
			}
			else {
	        	 return ResponseEntity.badRequest().body(Responses.builder().message("No Data Available For Employee Payslip").build());
		    }
			
		}
		
		 @PostMapping("/AllowancepercentageCalculation/{year}/{employeeId}")
		 public double AllowancepercentageCalculation(@PathVariable int year, @PathVariable String employeeId)
		 {
			 Employee employee = employeeDao.findByEmployeeId(employeeId);
			 List<Allowances> allowanceList = allowancesDao.findByAllowanceType("Percentage");
			 Allowances allowanceName = allowancesDao.findByAllowanceName("Basic Salary");
			 double basicSalary = 0;
			 double totalAllowanceValue =0;
			 double percentageValue = 0;
			 List<EmployeeAllowances> empallowancesBasicList = employeeAllowancesDao.getAllowancePercentage(year,employee.getEmplyoeeCode(),allowanceName.getAllowanceId());
			 for(EmployeeAllowances basic : empallowancesBasicList)
			 {
				 basicSalary = basic.getValue();
				 System.out.println("basic salary"+basicSalary);
			 }
			
			 for(Allowances allowance : allowanceList )
			 {
			 List<EmployeeAllowances> empallowancesList = employeeAllowancesDao.getAllowancePercentage(year,employee.getEmplyoeeCode(),allowance.getAllowanceId());
			 for(EmployeeAllowances empallowance: empallowancesList)
			 {  
				
				 System.out.println("allowance name"+empallowance.getAllowances().getAllowanceName());		 
					 System.out.println("value"+empallowance.getValue());
					double allowanceValue = empallowance.getValue()/100;
					System.out.println("multiple"+allowanceValue);
					 //System.out.println("allowance value"+totalAllowanceValue);
					  double LooptotalAllowanceValue = allowanceValue*basicSalary;
					  System.out.println("Loop allowance value"+LooptotalAllowanceValue);
					  
					  totalAllowanceValue=totalAllowanceValue+LooptotalAllowanceValue;
					  System.out.println("Total allowance value"+totalAllowanceValue);
			 }
			 }
			               return totalAllowanceValue;
			
		 }
		 
		 @PostMapping("/DeductionspercentageCalculation/{year}/{employeeId}")
		 public double DeductionspercentageCalculation(@PathVariable int year, @PathVariable String employeeId)
		 {
			 
			 Employee employee = employeeDao.findByEmployeeId(employeeId);
			 List<Deductions> deductionList = deductionsDao.findByDeductionType("Percentage");
			System.out.println("deduction list"+deductionList.size());
			 double basicSalary = 0;
			 double totalDeductionValue =0;
			 double LooptotalAllowanceValue =0;
			 Allowances allowanceName = allowancesDao.findByAllowanceName("Basic Salary");
			List<EmployeeAllowances> empallowancesBasicList = employeeAllowancesDao.getAllowancePercentage(year,employee.getEmplyoeeCode(),allowanceName.getAllowanceId());
			 System.out.println("allowance name"+allowanceName.getAllowanceId());
			 //System.out.println("employee allowanceBsic"+empallowancesBasicList);
			 for(EmployeeAllowances basic : empallowancesBasicList)
			 {
				 System.out.println("in loop");
				 basicSalary = basic.getValue();
			    // basicSalary = empallowancesBasicList.getValue();
				 System.out.println("basic salary"+basicSalary);
				 
				 
			 }
			 for(Deductions deduction : deductionList )
			 {
				// System.out.println("employee code"+employee.getEmplyoeeCode());
				 System.out.println("deduction Id"+deduction.getDeductionId());
			 List<EmployeeDeductions> empDeductionsList = employeeDeductionsDao.getPercentage(year,employee.getEmplyoeeCode(),deduction.getDeductionId());
			 System.out.println("employee deduction list"+empDeductionsList.size());
			 System.out.println("before value"+LooptotalAllowanceValue);
			 for(EmployeeDeductions empdeduction : empDeductionsList)
			 {
				 double deductionValue = empdeduction.getValue()/100;
				 System.out.println("m"+deductionValue);
				 LooptotalAllowanceValue = deductionValue*basicSalary;
				 System.out.println("k"+LooptotalAllowanceValue);
				 totalDeductionValue=totalDeductionValue+LooptotalAllowanceValue;
				 System.out.println("deduction"+totalDeductionValue);	 
				
			 }
                       
			 }
			 return totalDeductionValue;
				
		 }
}
