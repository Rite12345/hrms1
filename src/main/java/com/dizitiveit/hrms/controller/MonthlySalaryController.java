package com.dizitiveit.hrms.controller;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.AllowancesDao;
import com.dizitiveit.hrms.dao.AttendanceDao;
import com.dizitiveit.hrms.dao.AttendanceSummaryDao;
import com.dizitiveit.hrms.dao.BankDetailsDao;
import com.dizitiveit.hrms.dao.DeductionsDao;
import com.dizitiveit.hrms.dao.EmployeeAllowancesDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.EmployeeDeductionsDao;
import com.dizitiveit.hrms.dao.HolidaysDao;
import com.dizitiveit.hrms.dao.LeaveBalanceDao;
import com.dizitiveit.hrms.dao.LeaveMasterDao;
import com.dizitiveit.hrms.dao.LeaveRequestDao;
import com.dizitiveit.hrms.dao.LeavesAuditDao;
import com.dizitiveit.hrms.dao.MonthlySalarySummaryDao;
import com.dizitiveit.hrms.dao.PaySlipLineItemDao;
import com.dizitiveit.hrms.model.Allowances;
import com.dizitiveit.hrms.model.Attendance;
import com.dizitiveit.hrms.model.BankDetails;
import com.dizitiveit.hrms.model.Deductions;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.EmployeeAllowances;
import com.dizitiveit.hrms.model.EmployeeDeductions;
import com.dizitiveit.hrms.model.LeaveBalance;
import com.dizitiveit.hrms.model.LeaveMaster;
import com.dizitiveit.hrms.model.LeaveRequest;
import com.dizitiveit.hrms.model.LeavesAudit;
import com.dizitiveit.hrms.model.MonthlySalarySummary;
import com.dizitiveit.hrms.model.PaySlipLineItem;
import com.dizitiveit.hrms.pojo.EmployeeAllowancesPojo;
import com.dizitiveit.hrms.pojo.EmployeeDeductionsPojo;
import com.dizitiveit.hrms.pojo.PayslipLineItemPojo;
import com.dizitiveit.hrms.pojo.PayslipViewPojo;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/monthlySalary")
public class MonthlySalaryController {

	@Autowired
	private MonthlySalarySummaryDao monthlySalarySummaryDao;

	
	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private PaySlipLineItemDao paySlipLineItemDao;
	
	@Autowired
	private EmployeeAllowancesDao employeeAllowancesDao;

	@Autowired
	private EmployeeDeductionsDao employeeDeductionsDao;
	
	@Autowired
	private AttendanceController attendance;
	
	@Autowired
	private AllowancesDao allowancesDao;
	
	@Autowired
	private PayrollController payRollGeneration;
	
	@Autowired
	private DeductionsDao deductionsDao;
	
	@Autowired
	private LeaveMasterDao leaveMasterDao;
	
	@Autowired
	private LeavesAuditDao leaveAuditDao;
	
	@Autowired
	private AttendanceDao attendanceDao;
	
	@Autowired
	private LeaveBalanceDao leaveBalanceDao;
	
	
	@Autowired
	private BankDetailsDao bankDetailsDao; 
	
	@Autowired
	private PayrollController payRollController; 
	
	@Autowired
	private LeaveRequestDao leaveRequestDao;
	
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
		
	
	@GetMapping("/absentDays/{month}/{year}/{employeeId}")
	public int absentDays(@PathVariable int month,@PathVariable int year,@PathVariable String employeeId)
	{
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		 int workingDays = attendance.workingDaysCount(month, year,employee.getEmployeeId());
	 List<Attendance> attendanceList=attendanceDao.findByAbsentDays(month,year,employee.getEmplyoeeCode());  
	   System.out.println(attendanceList.size());
	  int absentDays=workingDays-attendanceList.size();
		System.out.println("absent days"+absentDays);
		return absentDays;
	}
	
	@GetMapping("leavesCount/{month}/{year}/{employeeId}")
	public long leavesCount(@PathVariable int month,@PathVariable int year,@PathVariable String employeeId)
	{
		 long days =0;
		 long actualDays =0;
		 long daysDiff=0;
		Employee employee = employeeDao.findByEmployeeId(employeeId);
        List<LeaveRequest> leaveRequestList = leaveRequestDao.getLeavecount(employee.getEmplyoeeCode(),month,month,year,year,"APPROVED");
        System.out.println("leave request size"+leaveRequestList.size());
        Calendar calendar = Calendar.getInstance();
        for(LeaveRequest leaveReq : leaveRequestList)
        {
        	DateFormat monthDateFormat = new SimpleDateFormat("MM");
        	String stDate = monthDateFormat.format(leaveReq.getStartDate());
        	//System.out.println("month start date"+stDate);
        	int i=Integer.parseInt(stDate);  
        	String eDate = monthDateFormat.format(leaveReq.getEndDate());
        	System.out.println("month start date"+i);
        	int j=Integer.parseInt(eDate);  
        	System.out.println("month end date"+j);
        	if(i==month && j==month)
        	{
        	System.out.println("startDate"+leaveReq.getStartDate());
        	System.out.println("endDate"+leaveReq.getEndDate());
        
        	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        	String strDate = dateFormat.format(leaveReq.getStartDate());
        	System.out.println("string start date"+strDate);
        	String endDate = dateFormat.format(leaveReq.getEndDate());
        	LocalDate localDate1 = LocalDate.parse(strDate);
        	System.out.println("start date local"+localDate1);
    		LocalDate localDate2 = LocalDate.parse(endDate);
    		System.out.println("end date local"+localDate2);
    		 days = ChronoUnit.DAYS.between(localDate1,localDate2);
    		 actualDays = days+1;
    		 System.out.println("no. of days"+actualDays);
    		 
    		 daysDiff = daysDiff+actualDays;
    	     System.out.println("days diff"+daysDiff);
        	}
        	else if(i==month && j!=month)
        	{
        		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        		String strDate = dateFormat.format(leaveReq.getStartDate());
        		System.out.println("start date in if"+strDate);
        		LocalDate convertedDate = LocalDate.parse(strDate);
        		convertedDate = convertedDate.withDayOfMonth(
        		                                convertedDate.getMonth().length(convertedDate.isLeapYear()));
            	LocalDate localDate1 = LocalDate.parse(strDate);
        		System.out.println("last date"+convertedDate);
        		days = ChronoUnit.DAYS.between(localDate1,convertedDate);
        		actualDays = days+1;
        		System.out.println("no.of start days"+actualDays);
        		//return actualDays;
        		 daysDiff = daysDiff+actualDays;
        	}
        	else if(i!=month && j==month)
        	{
        		System.out.println("i"+i);
        		System.out.println("j"+j);
        		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        		String endDate = dateFormat.format(leaveReq.getEndDate());
        		System.out.println("start date in second if"+endDate);
        		LocalDate localDate2 = LocalDate.parse(endDate);; 
        		LocalDate firstDayOfMonth = localDate2.with(TemporalAdjusters.firstDayOfMonth());
    		System.out.println("first date"+firstDayOfMonth);
        		days = ChronoUnit.DAYS.between(firstDayOfMonth,localDate2);
        		System.out.println("days"+days);
        		actualDays = days+1;
        		System.out.println("no.of start days"+actualDays);
        		 daysDiff = daysDiff+actualDays;
        	}
        }
        	
        return daysDiff;
	}
	
	@PostMapping("/autoSave/{month}/{year}")
	public ResponseEntity<?> calculation(@PathVariable int month,@PathVariable int year)
	{
		 double totalAllowances=0;
		 double totalAllowance=0;
		 double totalDeductions=0;
		 double totalDeduction=0;
		 double totalLopDeduction=0;
		// int absentDays =0;
		  double Earnings =0;
		  double deduc =0;
		  double basicSalary = 0;
		  Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int currentYear = cal.get(Calendar.YEAR);
			int currentMonth = cal.get(Calendar.MONTH);
			Month months = Month.of(month);
			List<Employee> employeeList = employeeDao.findAllEmployee(true);
			System.out.println("emloyee List Count"+employeeList.size());
			for(Employee employee : employeeList )
			{
				 Allowances allowanceName = allowancesDao.findByAllowanceName("Basic Salary");
				  List<EmployeeAllowances> empallowancesBasicList = employeeAllowancesDao.getAllowancePercentage(year,employee.getEmplyoeeCode(),allowanceName.getAllowanceId());
					 System.out.println("allowance name"+allowanceName.getAllowanceId());
					 //System.out.println("employee allowanceBsic"+empallowancesBasicList);
					 for(EmployeeAllowances basic : empallowancesBasicList)
					 {
						 System.out.println("in basic salary loop");
						 basicSalary = basic.getValue();
					    // basicSalary = empallowancesBasicList.getValue();
						 System.out.println("basic salary"+basicSalary);
					 }
			//Employee employee = employeeDao.findByEmployeeId(employeeId);
				System.out.println("emplpoyee Id"+employee.getEmployeeId());
				MonthlySalarySummary monthlyExists = monthlySalarySummaryDao.findmonthAndYear(months.name(),year,employee.getEmplyoeeCode());
				if(monthlyExists==null)
				{
				 MonthlySalarySummary monthly = new MonthlySalarySummary();
				 monthly.setEmployee(employee);
					monthly.setCurrentMonth(months.name());
					monthly.setCurrentYear(year);
					monthlySalarySummaryDao.save(monthly);
					List<Allowances> allowanceList = allowancesDao.findAll();
				//	System.out.println("allowances count"+allowanceList.size());
				for(Allowances allowances : allowanceList)
				{
					String type=allowances.getAllowanceType().trim();
				//	System.out.println("type"+type);
				if(type.equalsIgnoreCase("Fixed"))
				{
					//System.out.println("employeecode,year,allowanceId"+employee.getEmplyoeeCode()+""+year+""+allowances.getAllowanceId());
					//System.out.println("allowancesId"+allowances.getAllowanceId());
			List<EmployeeAllowances> employeeAllowances = employeeAllowancesDao.getLeastEmpAllowances(employee.getEmplyoeeCode(),year,allowances.getAllowanceId());
			System.out.println("employee allowances size"+employeeAllowances.size());
			if(employeeAllowances.size()!=0)
			{
				//System.out.println("inside allowances size if");
			 for(EmployeeAllowances empAlowance : employeeAllowances)
			 {
				 PaySlipLineItem paySlipLineItemAllowance = new PaySlipLineItem();
				 paySlipLineItemAllowance.setCurrentMonth(months.name());
				 paySlipLineItemAllowance.setCurrentYear(year);
				 paySlipLineItemAllowance.setItemDetails(empAlowance.getAllowances().getAllowanceName());
				 paySlipLineItemAllowance.setItemValue(empAlowance.getValue());
				 paySlipLineItemAllowance.setItemType(true);
				 paySlipLineItemAllowance.setMonthlySalarySummary(monthly);
				
				// System.out.println("allowance type"+paySlipLineItemAllowance.getEmployeeAllowances().getAllowances().getAllowanceType());
				 paySlipLineItemDao.save(paySlipLineItemAllowance);		
				// System.out.println("employeeId"+employee.getEmployeeId());
				 System.out.println("before adding"+totalAllowances);
				 totalAllowances = totalAllowances+empAlowance.getValue();
					System.out.println("allowance fixed amount"+totalAllowances);
			 }
			 
			 
				}
				
				}
				if(allowances.getAllowanceType().equalsIgnoreCase("Percentage"))
				{
					List<EmployeeAllowances> employeeAllowances = employeeAllowancesDao.getLeastEmpAllowances(employee.getEmplyoeeCode(),year,allowances.getAllowanceId());
					if(employeeAllowances.size()!=0)
					{
					 for(EmployeeAllowances empAlowance : employeeAllowances)
					 {
					PaySlipLineItem	paySlipLineItemAllowance = new PaySlipLineItem();
					 paySlipLineItemAllowance.setCurrentMonth(months.name());
					 paySlipLineItemAllowance.setCurrentYear(year);
					 paySlipLineItemAllowance.setItemDetails(empAlowance.getAllowances().getAllowanceName());
					 double allowanceValue = empAlowance.getValue()/100;
						System.out.println("multiple"+allowanceValue);
						  double SingleAllowanceValue = allowanceValue*basicSalary;
					 System.out.println("single deduction value"+SingleAllowanceValue);
					 paySlipLineItemAllowance.setItemValue(SingleAllowanceValue);
					 paySlipLineItemAllowance.setItemType(true);
					 paySlipLineItemAllowance.setMonthlySalarySummary(monthly);
					 paySlipLineItemDao.save(paySlipLineItemAllowance);		
					totalAllowance	=payRollGeneration.AllowancepercentageCalculation(year, employee.getEmployeeId());
				  System.out.println("allowance percentage amount"+totalAllowance);
					 }
				}
			}
				}
				 Earnings = totalAllowances+totalAllowance;
				 System.out.println("total earnings"+Earnings);
				
					List<Deductions> deductions = deductionsDao.findAll();
					for(Deductions deduction : deductions)
					{
						
						if(deduction.getDeductionType().trim().equalsIgnoreCase("Fixed"))
						{
							System.out.println("deductionId"+deduction.getDeductionId());
				List <EmployeeDeductions> employeeDeductions = employeeDeductionsDao.getLeastEmpDeductions(employee.getEmplyoeeCode(),year,deduction.getDeductionId());
				if(employeeDeductions.size()!=0)
				{
				for(EmployeeDeductions empDeduction : employeeDeductions)
				{
					 PaySlipLineItem paySlipLineItemDeduction = new PaySlipLineItem();
					 paySlipLineItemDeduction.setItemValue(empDeduction.getValue());
					 paySlipLineItemDeduction.setItemDetails(empDeduction.getDeductions().getDeductionName());
					 paySlipLineItemDeduction.setCurrentMonth(months.name());
					 paySlipLineItemDeduction.setCurrentYear(year);
					 paySlipLineItemDeduction.setItemType(false);
					 paySlipLineItemDeduction.setMonthlySalarySummary(monthly);
					paySlipLineItemDao.save(paySlipLineItemDeduction);
					 System.out.println("employeeId"+employee.getEmployeeId());
					totalDeductions = totalDeductions+empDeduction.getValue();
					System.out.println("deduction value"+empDeduction.getValue());
					System.out.println("deductions fixed"+totalDeductions);
				}
				
				
						}
						}
						if(deduction.getDeductionType().equalsIgnoreCase("Percentage")) {
							List <EmployeeDeductions> employeeDeductions = employeeDeductionsDao.getLeastEmpDeductions(employee.getEmplyoeeCode(),year,deduction.getDeductionId());
							if(employeeDeductions.size()!=0)
							{
							for(EmployeeDeductions empDeduction : employeeDeductions)
							{
								PaySlipLineItem paySlipLineItemDeduction = new PaySlipLineItem();
								 double deductionValue = empDeduction.getValue()/100;
								 System.out.println("multiple deduction value"+deductionValue);
								 double singleDeductionValue = deductionValue*basicSalary;
								 System.out.println("single deduction value"+singleDeductionValue);
								 paySlipLineItemDeduction.setItemValue(singleDeductionValue);
								 paySlipLineItemDeduction.setItemDetails(empDeduction.getDeductions().getDeductionName());
								 paySlipLineItemDeduction.setCurrentMonth(months.name());
								 paySlipLineItemDeduction.setCurrentYear(year);
								 paySlipLineItemDeduction.setItemType(false);
								 paySlipLineItemDeduction.setMonthlySalarySummary(monthly);
								paySlipLineItemDao.save(paySlipLineItemDeduction);
							totalDeduction = payRollGeneration.DeductionspercentageCalculation(year, employee.getEmployeeId());
							}
							//deduc = totalDeductions+totalDeduction;
						}
							//deduc = totalDeductions+totalDeduction;
						}
						System.out.println("deductions percentage"+totalDeduction);
						 deduc = totalDeductions+totalDeduction;
						 System.out.println("deductions"+deduc);
						 LeaveMaster leaveMasterCasual =  leaveMasterDao.findByleaveType("Casual");
					     LeaveMaster leaveMasterSick =  leaveMasterDao.findByleaveType("Sick");
					     LeavesAudit leaveAuditCasual=leaveAuditDao.getByleaveBalance(employee.getEmplyoeeCode(),leaveMasterCasual.getLeaveId());
					   //  System.out.println("casual leave"+leaveAuditCasual.getLeaveBalance());
					     LeavesAudit leaveAuditSick=leaveAuditDao.getByleaveBalance(employee.getEmplyoeeCode(),leaveMasterSick.getLeaveId());
					   //  System.out.println("sick leave"+leaveAuditCasual.getLeaveBalance());
					     int employeeAbsentDays = absentDays(month, year,employee.getEmployeeId());
					     System.out.println("employee absent days"+employeeAbsentDays);
					     List<LeaveRequest> leaveRequestList = leaveRequestDao.getLeavecount(employee.getEmplyoeeCode(),month,month,year,year,"APPROVED");
					    int employeeLeaves = leaveRequestList.size();
					    int x = (employeeAbsentDays - employeeLeaves) - leaveAuditCasual.getLeaveBalance();
					    System.out.println("count of x"+x);
					    int y = (employeeAbsentDays - employeeLeaves) - leaveAuditSick.getLeaveBalance();
					    System.out.println("count of x"+y);
					    if(employeeLeaves==0&&employeeAbsentDays!=0)
					    {
					    	 double lopPerDay = Earnings/30;
							 totalLopDeduction= lopPerDay*employeeAbsentDays;
							System.out.print("salary is"+totalLopDeduction);
							System.out.print("lop per day is"+lopPerDay);
							deduc = totalDeductions+totalDeduction+totalLopDeduction;
					    }
					    else if(x>0||y>0)
					    {
					    	 double lopPerDay = Earnings/30;
							 totalLopDeduction= lopPerDay*employeeAbsentDays;
							System.out.print("salary is"+totalLopDeduction);
							System.out.print("lop per day is"+lopPerDay);
							deduc = totalDeductions+totalDeduction+totalLopDeduction;
					    }
					    
	}
					//PaySlipLineItem paySlip = paySlipLineItemDao.findByMonthlySalarySummary(monthly);
					MonthlySalarySummary monthlyExisting = monthlySalarySummaryDao.findmonthAndYear(months.name(),year,employee.getEmplyoeeCode());	
					List<PaySlipLineItem> paySlip = paySlipLineItemDao.findByMonthlySalarySummary(monthlyExisting);
					if(paySlip.size()!=0)
					{
					monthlyExisting.setTotalEarnings(Math.round(Earnings));
				//System.out.println("total earnings"+Math.round(Earnings));
				monthlyExisting.setTotalDeductions(Math.round(deduc));
				System.out.println("total deduc"+Math.round(deduc));
				double netSalary = Earnings-deduc;
				System.out.println("net salary"+Math.round(netSalary));
				monthlyExisting.setNetSalary(Math.round(netSalary));
			     monthlySalarySummaryDao.save(monthlyExisting);    
				 //return ResponseEntity.ok(Responses.builder().message("paysliplineitems added successfully").build());
					}	
			}
				  totalAllowances=0;
				 totalAllowance=0;
				  totalDeductions=0;
				  totalDeduction=0;
				  totalLopDeduction=0;
				   Earnings =0;
				   deduc =0;
				
	}
			 return ResponseEntity.ok(Responses.builder().message("paysliplineitems added successfully").build());
			
	}
	
	
	@PostMapping("/autoSaveForSingleEmployee/{month}/{year}/{employeeId}")
	public ResponseEntity<?> autoSaveForSingleEmployee(@PathVariable int month,@PathVariable int year,@PathVariable String employeeId)
	{
		 double totalAllowances=0;
		 double totalAllowance=0;
		 double totalDeductions=0;
		 double totalDeduction=0;
		 double totalLopDeduction=0;
		// int absentDays =0;
		  double Earnings =0;
		  double deduc =0;
		  double basicSalary = 0;
		
		  Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int currentYear = cal.get(Calendar.YEAR);
			int currentMonth = cal.get(Calendar.MONTH);
			Month months = Month.of(month);
			List<Employee> employeeList = employeeDao.findAllEmployee(true);
			//System.out.println("emloyee List Count"+employeeList.size());
			
			Employee employee = employeeDao.findByEmployeeId(employeeId);
			  Allowances allowanceName = allowancesDao.findByAllowanceName("Basic Salary");
			  List<EmployeeAllowances> empallowancesBasicList = employeeAllowancesDao.getAllowancePercentage(year,employee.getEmplyoeeCode(),allowanceName.getAllowanceId());
				 System.out.println("allowance name"+allowanceName.getAllowanceId());
				 //System.out.println("employee allowanceBsic"+empallowancesBasicList);
				 for(EmployeeAllowances basic : empallowancesBasicList)
				 {
					 System.out.println("in basic salary loop");
					 basicSalary = basic.getValue();
				    // basicSalary = empallowancesBasicList.getValue();
					 System.out.println("basic salary"+basicSalary);
				 }
				System.out.println("emplpoyee Id"+employee.getEmployeeId());
				System.out.println(months.name());
				MonthlySalarySummary monthlyExists = monthlySalarySummaryDao.findmonthAndYear(months.name(),year,employee.getEmplyoeeCode());
				if(monthlyExists!=null)
				{
					List<Allowances> allowanceList = allowancesDao.findAll();
				//	System.out.println("allowances count"+allowanceList.size());
				for(Allowances allowances : allowanceList)
				{
					String type=allowances.getAllowanceType().trim();
				//	System.out.println("type"+type);
		
				if(type.equalsIgnoreCase("Fixed"))
				{
					//System.out.println("employeecode,year,allowanceId"+employee.getEmplyoeeCode()+""+year+""+allowances.getAllowanceId());
					System.out.println("allowancesId"+allowances.getAllowanceId());
			List<EmployeeAllowances> employeeAllowances = employeeAllowancesDao.getLeastEmpAllowances(employee.getEmplyoeeCode(),year,allowances.getAllowanceId());
			//System.out.println("employee allowances size"+employeeAllowances.size());
			if(employeeAllowances.size()!=0)
			{
				//System.out.println("inside allowances size if");
			 for(EmployeeAllowances empAlowance : employeeAllowances)
			 {
				// System.out.println("employee allowance allowanceId"+empAlowance.getAllowances().getAllowanceId());
				List<PaySlipLineItem> paySlipLineItemExisting = paySlipLineItemDao.findByExistingPayslipLineItem(months.name(), currentYear, monthlyExists.getMonthlySalaryID());
			//	System.out.println("payslip line item list"+paySlipLineItemExisting.size());
				if(paySlipLineItemExisting.size()!= 0)
				{
					 System.out.println("before adding fixed"+totalAllowances);
					 totalAllowances = totalAllowances+empAlowance.getValue();
					 System.out.println("allowance fixed amount"+totalAllowances);
				}
				else {
					PaySlipLineItem	paySlipLineItemAllowance = new PaySlipLineItem();
					System.out.println("inside else");
					 paySlipLineItemAllowance.setCurrentMonth(months.name());
					 paySlipLineItemAllowance.setCurrentYear(year);
					 paySlipLineItemAllowance.setItemDetails(empAlowance.getAllowances().getAllowanceName());
					// System.out.println("employee allowance allowanceName"+empAlowance.getAllowances().getAllowanceName());
					 paySlipLineItemAllowance.setItemValue(empAlowance.getValue());
					 paySlipLineItemAllowance.setItemType(true);
					 paySlipLineItemAllowance.setMonthlySalarySummary(monthlyExists);
					 paySlipLineItemDao.save(paySlipLineItemAllowance);	
					// System.out.println("employeeId"+employee.getEmployeeId());
					 System.out.println("before adding fixed new"+totalAllowances);
					 totalAllowances = totalAllowances+empAlowance.getValue();
						System.out.println("allowance fixed amount new"+totalAllowances);
				}
				
				
			 }
			 
			 
				}
				
				}
				if(allowances.getAllowanceType().equalsIgnoreCase("Percentage"))
				{
					List<EmployeeAllowances> employeeAllowances = employeeAllowancesDao.getLeastEmpAllowances(employee.getEmplyoeeCode(),year,allowances.getAllowanceId());
					if(employeeAllowances.size()!=0)
					{
						System.out.println("employee allowances size"+employeeAllowances.size());
					 for(EmployeeAllowances empAlowance : employeeAllowances)
					 {
						 List<PaySlipLineItem> paySlipLineItemExisting = paySlipLineItemDao.findByExistingPayslipLineItem(months.name(), year, monthlyExists.getMonthlySalaryID());
							System.out.println("payslip size"+paySlipLineItemExisting.size());
							if(paySlipLineItemExisting.size()!= 0)
							{
								 System.out.println("before adding percentage"+totalAllowance);
								totalAllowance	=payRollGeneration.AllowancepercentageCalculation(year, employee.getEmployeeId());
								  System.out.println("allowance percentage amount"+totalAllowance);
							}
							else {
								PaySlipLineItem	paySlipLineItemAllowance = new PaySlipLineItem();
								 paySlipLineItemAllowance.setCurrentMonth(months.name());
								 paySlipLineItemAllowance.setCurrentYear(currentYear);
								 paySlipLineItemAllowance.setItemDetails(empAlowance.getAllowances().getAllowanceName());
								 double allowanceValue = empAlowance.getValue()/100;
								 System.out.println("multiple allowance value"+allowanceValue);
								 double singleAllowanceValue = allowanceValue*basicSalary;
								 System.out.println("single deduction value"+singleAllowanceValue);
								 paySlipLineItemAllowance.setItemValue(singleAllowanceValue);
								 paySlipLineItemAllowance.setItemType(true);
								 paySlipLineItemAllowance.setMonthlySalarySummary(monthlyExists);
								 paySlipLineItemDao.save(paySlipLineItemAllowance);	
								 System.out.println("before adding percentage new"+totalAllowance);
								 totalAllowance	=payRollGeneration.AllowancepercentageCalculation(year, employee.getEmployeeId());
								  System.out.println("allowance percentage amount new"+totalAllowance);
							}
							 
					
					 }
				}
			}
				}
				 Earnings = totalAllowances+totalAllowance;
				 System.out.println("total earnings"+Earnings);
				
					List<Deductions> deductions = deductionsDao.findAll();
					for(Deductions deduction : deductions)
					{
						
						if(deduction.getDeductionType().trim().equalsIgnoreCase("Fixed"))
						{
							System.out.println("deductionId"+deduction.getDeductionId());
				List <EmployeeDeductions> employeeDeductions = employeeDeductionsDao.getLeastEmpDeductions(employee.getEmplyoeeCode(),year,deduction.getDeductionId());
				if(employeeDeductions.size()!=0)
				{
				for(EmployeeDeductions empDeduction : employeeDeductions)
				{
					List<PaySlipLineItem> paySlipLineItemExisting = paySlipLineItemDao.findByExistingPayslipLineItem(months.name(), currentYear, monthlyExists.getMonthlySalaryID());

					if(paySlipLineItemExisting.size()!= 0)
					{
						//System.out.println("employeeId"+employee.getEmployeeId());
						 System.out.println("before adding fixed deduction"+totalDeductions);
						totalDeductions = totalDeductions+empDeduction.getValue();
						System.out.println("deductions fixed after"+totalDeductions);
					}
					else {
						System.out.println("deduction name"+empDeduction.getDeductions().getDeductionName());
						 PaySlipLineItem paySlipLineItemDeduction = new PaySlipLineItem();
						 paySlipLineItemDeduction.setItemValue(empDeduction.getValue());
						 paySlipLineItemDeduction.setItemDetails(empDeduction.getDeductions().getDeductionName());
						 System.out.println("employee deduction deductionName"+empDeduction.getDeductions().getDeductionName());
						 paySlipLineItemDeduction.setCurrentMonth(months.name());
						 paySlipLineItemDeduction.setCurrentYear(year);
						 paySlipLineItemDeduction.setItemType(false);
						 paySlipLineItemDeduction.setMonthlySalarySummary(monthlyExists);
						paySlipLineItemDao.save(paySlipLineItemDeduction);
						// System.out.println("employeeId"+employee.getEmployeeId());
				
						 System.out.println("before adding fixed deduction new"+totalDeductions);
							totalDeductions = totalDeductions+empDeduction.getValue();
							System.out.println("deductions fixed after new"+totalDeductions);
					}
					
					
				}
			}
						}
						if(deduction.getDeductionType().equalsIgnoreCase("Percentage")) {
							System.out.println("deduction Id"+deduction.getDeductionId());
							List <EmployeeDeductions> employeeDeductions = employeeDeductionsDao.getLeastEmpDeductions(employee.getEmplyoeeCode(),year,deduction.getDeductionId());
							//System.out.println("deduction percentage size"+employeeDeductions.size());
							if(employeeDeductions.size()!=0)
							{
							for(EmployeeDeductions empDeduction : employeeDeductions)
							{
								List<PaySlipLineItem> paySlipLineItemExisting = paySlipLineItemDao.findByExistingPayslipLineItem(months.name(), currentYear, monthlyExists.getMonthlySalaryID());
								//System.out.println("payslipLineItem Size"+paySlipLineItemExisting.size());
								if(paySlipLineItemExisting.size()!= 0)
								{
									 System.out.println("before adding percentage deduction"+totalDeduction);
									totalDeduction = payRollGeneration.DeductionspercentageCalculation(year, employee.getEmployeeId());
									 System.out.println("after adding percentage deduction"+totalDeduction);
                                         
									 
								}
								else {
									 PaySlipLineItem paySlipLineItemDeduction = new PaySlipLineItem();
									 paySlipLineItemDeduction.setItemDetails(empDeduction.getDeductions().getDeductionName());
									 paySlipLineItemDeduction.setCurrentMonth(months.name());
									 paySlipLineItemDeduction.setCurrentYear(year);
									 paySlipLineItemDeduction.setItemType(false);
									 paySlipLineItemDeduction.setMonthlySalarySummary(monthlyExists);
									 double deductionValue = empDeduction.getValue()/100;
									 System.out.println("multiple deduction value"+deductionValue);
									 double singleDeductionValue = deductionValue*basicSalary;
									 System.out.println("single deduction value"+singleDeductionValue);
									 paySlipLineItemDeduction.setItemValue(singleDeductionValue);
									 System.out.println("payslip line item value"+paySlipLineItemDeduction.getItemValue());
									 paySlipLineItemDao.save(paySlipLineItemDeduction);	
									 System.out.println("before adding percentage deduction new"+totalDeduction);
									totalDeduction = payRollGeneration.DeductionspercentageCalculation(year, employee.getEmployeeId());
									 System.out.println("after adding percentage deduction new"+totalDeduction);

								}
								
							//totalDeduction = payRollGeneration.DeductionspercentageCalculation(month, year, employee.getEmployeeId());
							}
							//deduc = totalDeductions+totalDeduction;
						}
							deduc = totalDeductions+totalDeduction;
						}
						System.out.println("deductions percentage"+totalDeduction);
						 System.out.println("deductions"+deduc);
						 LeaveMaster leaveMasterCasual =  leaveMasterDao.findByleaveType("Casual");
					     LeaveMaster leaveMasterSick =  leaveMasterDao.findByleaveType("Sick");
					     LeavesAudit leaveAuditCasual=leaveAuditDao.getByleaveBalance(employee.getEmplyoeeCode(),leaveMasterCasual.getLeaveId());
					     System.out.println("casual leave"+leaveAuditCasual.getLeaveBalance());
					     LeavesAudit leaveAuditSick=leaveAuditDao.getByleaveBalance(employee.getEmplyoeeCode(),leaveMasterSick.getLeaveId());
					     System.out.println("sick leave"+leaveAuditCasual.getLeaveBalance());
					     int employeeAbsentDays = absentDays(month, year,employee.getEmployeeId());
					     System.out.println("employee absent days"+employeeAbsentDays);
					     List<LeaveRequest> leaveRequestList = leaveRequestDao.getLeavecount(employee.getEmplyoeeCode(),month,month,year,year,"APPROVED");
					    int employeeLeaves = leaveRequestList.size();
					    int x = employeeAbsentDays - employeeLeaves - leaveAuditCasual.getLeaveBalance();
					    System.out.println("count of x"+x);
					    int y = employeeAbsentDays - employeeLeaves - leaveAuditSick.getLeaveBalance();
					    System.out.println("count of y"+y);
					    if(employeeLeaves==0&&employeeAbsentDays!=0)
					    {
					    	 double lopPerDay = Earnings/30;
							 totalLopDeduction= lopPerDay*employeeAbsentDays;
							System.out.print("salary is"+totalLopDeduction);
							System.out.print("lop per day is"+lopPerDay);
							deduc = totalDeductions+totalDeduction+totalLopDeduction;
							System.out.println("first if"+deduc);
					    }
					    else if(x>0||y>0)
					    {
					    	 double lopPerDay = Earnings/30;
							 totalLopDeduction= lopPerDay*employeeAbsentDays;
							System.out.print("salary is"+totalLopDeduction);
							System.out.print("lop per day is"+lopPerDay);
							deduc = totalDeductions+totalDeduction+totalLopDeduction;
							System.out.println("second if"+deduc);
					    }
					    
					    	 }
	
				
					monthlyExists.setTotalEarnings(Math.round(Earnings));
				//System.out.println("total earnings"+Math.round(Earnings));
					monthlyExists.setTotalDeductions(Math.round(deduc));
				System.out.println("total deduc"+Math.round(deduc));
				double netSalary = Earnings-deduc;
				System.out.println("net salary"+Math.round(netSalary));
				monthlyExists.setNetSalary(Math.round(netSalary));
			     monthlySalarySummaryDao.save(monthlyExists);    
				return ResponseEntity.ok(Responses.builder().message("paysliplineitems added successfully").build());
				}
				else {
					return ResponseEntity.ok(Responses.builder().message("There is no data with this employee").build());
				}
			}
	
				
	
			
			
	

	
	@GetMapping("/viewingMonthlyAllowances/{month}/{year}/{employeeId}")
	public ResponseEntity<?> viewingMonthlyAllowances(@PathVariable int month,@PathVariable int year,@PathVariable String employeeId)
	{
		Month months = Month.of(month);
        Employee employee = employeeDao.findByEmployeeId(employeeId);
       PayslipViewPojo paySlipViewPojo = new PayslipViewPojo();
       paySlipViewPojo.setEmployeeId(employeeId);
       String empName = employee.getFirstName() + " " + employee.getLastName();
       paySlipViewPojo.setEmployeeName(empName);
       
       BankDetails bankDetails=bankDetailsDao.findByEmployee(employee);
       if(bankDetails!=null)
		{
    	   paySlipViewPojo.setAccountNumber(bankDetails.getAccountNumber());
    	   paySlipViewPojo.setBankName(bankDetails.getBankName());
		}
 
       paySlipViewPojo.setBaseBranch(employee.getBranch().getBranchName());
       paySlipViewPojo.setDeputeBranch(employee.getDeputeBranch());
     
       paySlipViewPojo.setPanNumber(employee.getPanCardNumber());
       DateFormat dateOfJoiningFormat = new SimpleDateFormat("dd-MM-yyyy");
       paySlipViewPojo.setDateOfJoining(dateOfJoiningFormat.format(employee.getDateOfJoining()));
       paySlipViewPojo.setLocation(employee.getLocation());
       if(employee.getDesg()!=null)
		{
    	   paySlipViewPojo.setGrade(employee.getDesg().getGrade());
		  paySlipViewPojo.setDesigName(employee.getDesg().getDesigName());
		}
        List<PaySlipLineItem> paySlipLineItemList = paySlipLineItemDao.findByMonthAndYear(months.name(), year);
        List<PayslipLineItemPojo> payslipLineItemPojoList = new ArrayList(); 
        for(PaySlipLineItem paySlipLineItem : paySlipLineItemList )
        {
        	PayslipLineItemPojo payslipLineItemPojo = new  PayslipLineItemPojo();
        	payslipLineItemPojo.setCurrentMonth(paySlipLineItem.getCurrentMonth());
        	payslipLineItemPojo.setCurrentYear(paySlipLineItem.getCurrentYear());
        	payslipLineItemPojo.setItemDetails(paySlipLineItem.getItemDetails());
        	payslipLineItemPojo.setItemValue(paySlipLineItem.getItemValue());
        	payslipLineItemPojo.setItemType(paySlipLineItem.isItemType());
        	payslipLineItemPojoList.add(payslipLineItemPojo);
        }
        paySlipViewPojo.setPaySlipLineItemList(payslipLineItemPojoList);
        MonthlySalarySummary monthlySalarySummary = monthlySalarySummaryDao.findmonthAndYear(months.name(),year,employee.getEmplyoeeCode());
        paySlipViewPojo.setEarninnings(monthlySalarySummary.getTotalEarnings());
        paySlipViewPojo.setDeductions(monthlySalarySummary.getTotalDeductions());
        paySlipViewPojo.setNetSalary(monthlySalarySummary.getNetSalary());
       
        LeaveBalance leaveBalanceSick = leaveBalanceDao.findByItemType(employee.getEmplyoeeCode(),year,"Sick");
		LeaveBalance leaveBalanceCasual = leaveBalanceDao.findByItemType(employee.getEmplyoeeCode(),year,"Casual");
		if(leaveBalanceSick!=null)
		{	
			paySlipViewPojo.setSickLeaves(leaveBalanceSick.getEntitledLeaves());
		}
		if(leaveBalanceCasual!=null)
		{
			paySlipViewPojo.setCasualLeaves(leaveBalanceCasual.getEntitledLeaves());
		}
		
		double lopValue = payRollController.lop(employeeId,month, year);
		paySlipViewPojo.setLOP(lopValue);
		
		  int workingDays = attendance.workingDaysCount(month, year,employee.getEmployeeId());
		  paySlipViewPojo.setDaysPaid(workingDays);
		  List<Attendance> attendanceList=attendanceDao.findByAbsentDays(month,year,employee.getEmplyoeeCode());  
		int  lopDays=workingDays-attendanceList.size();
		paySlipViewPojo.setLopDays(lopDays);
		
		HashMap<String,PayslipViewPojo> response = new HashMap<String,PayslipViewPojo>();
		  response.put("payslip", paySlipViewPojo);
		  return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/updatePayslipLineItem/{employeeId}/{month}/{year}/{itemDetails}")
	public ResponseEntity<?> updatePayslipLineItem(@RequestBody  PaySlipLineItem paySlipLineItem, @PathVariable int month,@PathVariable int year,@PathVariable String employeeId,@PathVariable String itemDetails)
	{
		Month months = Month.of(month);
		   Employee employee = employeeDao.findByEmployeeId(employeeId);
		MonthlySalarySummary monthlySalarySummary = monthlySalarySummaryDao.findmonthAndYear(months.name(),year,employee.getEmplyoeeCode());
		 PaySlipLineItem paySlipLineItemExisting = paySlipLineItemDao.findByItemDetails(itemDetails,months.name(), year,monthlySalarySummary.getMonthlySalaryID());
		 System.out.println("item details"+paySlipLineItemExisting.getItemDetails());
		 if(paySlipLineItemExisting!=null)
		 {
			 paySlipLineItemExisting.setCurrentMonth(months.name());
			 paySlipLineItemExisting.setCurrentYear(year);
			 paySlipLineItemExisting.setItemDetails(itemDetails);
			 paySlipLineItemExisting.setItemType(paySlipLineItem.isItemType());
			 paySlipLineItemExisting.setItemValue(paySlipLineItem.getItemValue());
			 paySlipLineItemDao.save(paySlipLineItemExisting);
			 if(paySlipLineItemExisting.isItemType()==true)
			 {
			 Allowances allowances = allowancesDao.findByAllowanceName(itemDetails);
			 EmployeeAllowances employeeAllowancesExisting =  employeeAllowancesDao.getExistingRecord(month, year,employee.getEmplyoeeCode() ,allowances.getAllowanceId());
			 employeeAllowancesExisting.setFromMonth(month);
			 employeeAllowancesExisting.setFromYear(year);
			 employeeAllowancesExisting.setToMonth(month);
			 employeeAllowancesExisting.setToYear(year);
			 employeeAllowancesExisting.setValue(paySlipLineItemExisting.getItemValue());
			 employeeAllowancesExisting.setEmployee(employee);
			 employeeAllowancesDao.save(employeeAllowancesExisting);
			 }
			 else if(paySlipLineItemExisting.isItemType()==false)
			 {
		 Deductions deductions = deductionsDao.findByDeductionName(itemDetails);
		 EmployeeDeductions employeeDeductionsExisting = employeeDeductionsDao.getUpdateDeductions(month, year, employee.getEmplyoeeCode(), deductions.getDeductionId());
		 employeeDeductionsExisting.setFromMonth(month);
		 employeeDeductionsExisting.setFromYear(year);
		 employeeDeductionsExisting.setToMonth(month);
		 employeeDeductionsExisting.setToYear(year);
		 employeeDeductionsExisting.setEmployee(employee);
		 employeeDeductionsExisting.setValue(paySlipLineItemExisting.getItemValue());
		 employeeDeductionsDao.save(employeeDeductionsExisting);
			 }
			 return ResponseEntity.ok(Responses.builder().message("paysliplineitems Updated successfully").build());
		 }
		 else {
			 return ResponseEntity.ok(Responses.builder().message("paysliplineitems Doesnt Existing").build());
		 }
	
		 
	}
	
	@PostMapping("/UpdatingOnRequest/{employeeId}/{month}/{year}/{toMonth}/{toYear}")
	public ResponseEntity<?> ChangingOnRequest(@RequestBody PaySlipLineItem paySlipLineItem,@PathVariable String employeeId,@PathVariable int month,@PathVariable int year,@PathVariable int toMonth,@PathVariable int toYear)
	{
		Month months = Month.of(month);
		System.out.println(months.name());
		 Employee employee = employeeDao.findByEmployeeId(employeeId);
		 MonthlySalarySummary monthlySalarySummary = monthlySalarySummaryDao.findmonthAndYear(months.name(),year,employee.getEmplyoeeCode());
		// System.out.println(monthlySalarySummary.getEmployee().getEmployeeId());
		 if(monthlySalarySummary!=null)
		 {
			 PaySlipLineItem payslipItem = new PaySlipLineItem();
			 payslipItem.setCurrentMonth(months.name());
			 payslipItem.setCurrentYear(year);
			 payslipItem.setItemType(paySlipLineItem.isItemType());
			 payslipItem.setItemValue(paySlipLineItem.getItemValue());
			 payslipItem.setItemDetails(paySlipLineItem.getItemDetails());
			 payslipItem.setMonthlySalarySummary(monthlySalarySummary);
			 paySlipLineItemDao.save(payslipItem);
		 
			 if(paySlipLineItem.isItemType()==true)
			 {
			 
			 Allowances allowance = allowancesDao.findByAllowanceName(paySlipLineItem.getItemDetails());
		EmployeeAllowances empAllowance = new EmployeeAllowances();
		empAllowance.setFromMonth(month);
		empAllowance.setFromYear(year);
		empAllowance.setToMonth(toMonth);
		empAllowance.setToYear(toYear);
		empAllowance.setValue(paySlipLineItem.getItemValue());
		empAllowance.setEmployee(employee);
		empAllowance.setAllowances(allowance);
		employeeAllowancesDao.save(empAllowance);
		 }
			 else if(paySlipLineItem.isItemType()==false)
			 {
				 Deductions deduction = deductionsDao.findByDeductionName(paySlipLineItem.getItemDetails());
				 EmployeeDeductions empDeductions = new EmployeeDeductions();
				 empDeductions.setFromMonth(month);
				 empDeductions.setFromYear(year);
				 empDeductions.setToMonth(toMonth);
				 empDeductions.setToYear(toYear);
				 empDeductions.setValue(paySlipLineItem.getItemValue());
				 empDeductions.setEmployee(employee);
				 empDeductions.setDeductions(deduction);
				 employeeDeductionsDao.save(empDeductions);
			 }
		 }
		 return ResponseEntity.ok(Responses.builder().message("paysliplineitems saved successfully").build());
	}
	
} 







