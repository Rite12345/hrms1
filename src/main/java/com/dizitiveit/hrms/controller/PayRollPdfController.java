package com.dizitiveit.hrms.controller;

import java.text.DateFormat;


import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dizitiveit.hrms.dao.AttendanceDao;
import com.dizitiveit.hrms.dao.BankDetailsDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.LeaveBalanceDao;
import com.dizitiveit.hrms.dao.LeaveMasterDao;
import com.dizitiveit.hrms.dao.LeaveRequestDao;
import com.dizitiveit.hrms.dao.LeavesAuditDao;
import com.dizitiveit.hrms.dao.MonthlySalarySummaryDao;
import com.dizitiveit.hrms.dao.PaySlipLineItemDao;
import com.dizitiveit.hrms.model.Attendance;
import com.dizitiveit.hrms.model.BankDetails;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.LeaveBalance;
import com.dizitiveit.hrms.model.LeaveMaster;
import com.dizitiveit.hrms.model.LeaveRequest;
import com.dizitiveit.hrms.model.LeavesAudit;
import com.dizitiveit.hrms.model.MonthlySalarySummary;
import com.dizitiveit.hrms.model.PaySlipLineItem;
import com.dizitiveit.hrms.pojo.PayRollGenerationPojo;

@RequestMapping("/payRollPdf")
@Controller
public class PayRollPdfController {
	
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
	private LeavesAuditDao leavesAuditDao;
	
	@Autowired
	private LeaveMasterDao leaveMasterDao;

	@Autowired
	private AttendanceDao attendanceDao;
	
	@Autowired
	private PayrollController payrollController;
	
	@Autowired
	private AttendanceController attendance;

	@Autowired
	private PayrollController payRollController; 
	
	@Autowired
	private LeavesAuditDao leaveAuditDao;
	
	@Autowired
	private LeaveRequestDao leaveRequestDao;
	
	@Autowired
	private MonthlySalaryController monthlySalaryController;

	  @RequestMapping("/viewPayslip/{employeeId}/{month}/{year}")
		public String viewPayslip(Model model,@PathVariable String employeeId,@PathVariable String month,@PathVariable int year)
		{
	    	
			PayRollGenerationPojo payRollPojo = new   PayRollGenerationPojo();
			Employee employee = employeeDao.findByEmployeeId(employeeId);
			payRollPojo.setEmployeeId(employee.getEmployeeId());
			String employeeName = employee.getFirstName() +" "+ employee.getLastName();
			payRollPojo.setEmployeeName(employeeName);
			payRollPojo.setBaseBranch(employee.getBranch().getBranchName());
			payRollPojo.setDeputeBranch(employee.getDeputeBranch());
			payRollPojo.setLocation(employee.getLocation());
			payRollPojo.setDateOfJoining(employee.getDateOfJoining());
			payRollPojo.setPanCardNumber(employee.getPanCardNumber());
			if(employee.getDesg()!=null)
			{
			payRollPojo.setGrade(employee.getDesg().getGrade());
			payRollPojo.setDesignation(employee.getDesg().getDesigName());	
			}
			BankDetails bankDetails = bankDetailsDao.findByEmployee(employee);
			if(bankDetails!=null)
			{
			payRollPojo.setAccountNumber(bankDetails.getAccountNumber());
			payRollPojo.setBankName(bankDetails.getBankName());
			}
			  System.out.println("empcode"+employee.getEmplyoeeCode()+"month"+month+"year"+year);
			MonthlySalarySummary monthlySalary = monthlySalarySummaryDao.findwithmonthAndYear(employee.getEmplyoeeCode(), month, year);
		  
		     //System.out.println("monthly salary id"+monthlySalary.getMonthlySalaryID());
		    //long monthlySalaryId = monthlySalary.getMonthlySalaryID();
		     if(monthlySalary!=null)
		     {
			List<PaySlipLineItem> allowanceList = paySlipLineItemDao.findByItemType(monthlySalary.getMonthlySalaryID(), month, year, true);
			/*
			 * double basicSalary=0; for(PaySlipLineItem payslip : allowanceList) {
			 * if(payslip.getItemDetails().trim().equalsIgnoreCase("Basic Salary")) {
			 * basicSalary = payslip.getItemValue(); break; } }
			 */
			/*
			 * for(int i=0;i<allowanceList.size();i++) {
			 * if(allowanceList.get(i).getItemDetails().trim().equalsIgnoreCase("Conveyance"
			 * )) { System.out.println("if condition"); double
			 * oldValue=allowanceList.get(i).getItemValue(); double percentageVal =
			 * oldValue/100; double newValue = percentageVal*basicSalary;
			 * allowanceList.get(i).setItemValue(newValue); } }
			 */	
				
			
			List<PaySlipLineItem> deductionsList = paySlipLineItemDao.findByItemType(monthlySalary.getMonthlySalaryID(), month, year, false);
			
			/*
			 * for(int i=0;i<deductionsList.size();i++) {
			 * if(deductionsList.get(i).getItemDetails().trim().
			 * equalsIgnoreCase("PF Employer Contribution") &&
			 * deductionsList.get(i).getItemDetails().trim().
			 * equalsIgnoreCase("PF Employee Contribution")) {
			 * System.out.println("if condition"); double
			 * oldValue=deductionsList.get(i).getItemValue(); double percentageVal =
			 * oldValue/100; double newValue = percentageVal*basicSalary;
			 * deductionsList.get(i).setItemValue(newValue); } }
			 */
			//MonthlySalarySummary monthlySalary=monthlySalarySummaryDao.findByMonthlySalaryID(monthlySalaryId);
			
			String monthAndYear = monthlySalary.getCurrentMonth()+" "+monthlySalary.getCurrentYear();
			
			//LeaveBalance leaveBalanceSick = leaveBalanceDao.findByItemType(employee.getEmplyoeeCode(),year,"Sick Leave");
			//LeaveBalance leaveBalanceCasual = leaveBalanceDao.findByItemType(employee.getEmplyoeeCode(),year,"Casual Leave");
			List<LeaveMaster> leaveMaster=leaveMasterDao.findAll();
			System.out.println("leave master"+leaveMaster.size());
			for(LeaveMaster leavemaster : leaveMaster)
			  {
		    LeavesAudit auditDetails=leavesAuditDao.findByQuarter(employee.getEmplyoeeCode(),leavemaster.getLeaveId());
		    System.out.println( auditDetails.getLeaveBalance());
		    if(auditDetails.getLeaveMaster().getLeaveType().equalsIgnoreCase("Casual"))
		    {
		    payRollPojo.setCasualLeave(auditDetails.getLeaveBalance());
		    }
		    else if(auditDetails.getLeaveMaster().getLeaveType().equalsIgnoreCase("Sick"))
		    {
		    	payRollPojo.setSickLeave(auditDetails.getLeaveBalance());
		    	System.out.println("sick leave"+auditDetails.getLeaveBalance());
		    }
			  }
			
			
			Date date;
			int monthNumber=0;
			
			try {
				date = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(month);
				Format f = new SimpleDateFormat("M");
				String strMonth = f.format(new Date());
				monthNumber=Integer.parseInt(strMonth);  

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	YearMonth yearMonthObject = YearMonth.of(monthlySalary.getCurrentYear(), monthNumber);
			
			int daysInMonth = yearMonthObject.lengthOfMonth(); 
			DateFormat dfIn = new SimpleDateFormat("dd-MMM-yyyy' 'HH:mm");
			Date paySlipDate = DateUtils.addHours(new Date(), +5);
			Date paySlipDateMin = DateUtils.addMinutes(paySlipDate,+30);
			//System.out.println("payslip in date"+paySlipDateMin);
			
			String payslipDate=dfIn.format(paySlipDateMin);

			payRollPojo.setGeneratedDate(payslipDate);
			//payRollPojo.setDaysPaid(daysInMonth);
			payRollPojo.setDaysPaid(30);
			
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(new SimpleDateFormat("MMM").parse(month));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		   int monthInt = cal.get(Calendar.MONTH)+1;
		   
		   
			
		   int workingDays = attendance.workingDaysCount(monthInt, year,employee.getEmployeeId());
			  System.out.println("workingDays"+workingDays);
			  List<Attendance> attendanceList=attendanceDao.findByAbsentDays(monthInt,year,employee.getEmplyoeeCode());
			   System.out.println(attendanceList.size());
			   LeaveMaster leaveMasterCasual =  leaveMasterDao.findByleaveType("Casual");
			     LeaveMaster leaveMasterSick =  leaveMasterDao.findByleaveType("Sick");
			     LeavesAudit leaveAuditCasual=leaveAuditDao.getByleaveBalance(employee.getEmplyoeeCode(),leaveMasterCasual.getLeaveId());
			     LeavesAudit leaveAuditSick=leaveAuditDao.getByleaveBalance(employee.getEmplyoeeCode(),leaveMasterSick.getLeaveId());
			     int employeeAbsentDays = monthlySalaryController.absentDays(monthInt, year,employee.getEmployeeId());
			     List<LeaveRequest> leaveRequestList = leaveRequestDao.getLeavecount(employee.getEmplyoeeCode(),monthInt,monthInt,year,year,"APPROVED");
				    int employeeLeaves = leaveRequestList.size();
				    int x = employeeAbsentDays - employeeLeaves - leaveAuditCasual.getLeaveBalance();
				    System.out.println("count of x"+x);
				    int y = employeeAbsentDays - employeeLeaves - leaveAuditSick.getLeaveBalance();
				    System.out.println("count of y"+y);
				    if(employeeLeaves==0&&employeeAbsentDays!=0)
				    {
				int lopCount=workingDays-attendanceList.size();
				System.out.println("lop count"+lopCount);
				payRollPojo.setLopDays(lopCount);
				
				double lopAmount =	payrollController.lop(employeeId, monthInt, year);
				   System.out.println("lop amount"+lopAmount);
					
				   payRollPojo.setLop(Math.round(lopAmount));
			     }
				    else if(x>0||y>0)
				    {
				    	int lopCount=workingDays-attendanceList.size();
						System.out.println("lop count"+lopCount);
						payRollPojo.setLopDays(lopCount);
						
						double lopAmount =	payrollController.lop(employeeId, monthInt, year);
						   System.out.println("lop amount"+lopAmount);
							
						   payRollPojo.setLop(Math.round(lopAmount));
				    }
		   
		   
			
			payRollPojo.setMonthAndYear(monthAndYear);
			payRollPojo.setMonthlySalarySummary(monthlySalary);
			model.addAttribute("allowanceList",allowanceList);
			model.addAttribute("deductionsList",deductionsList);
			model.addAttribute("payRoll",payRollPojo );
			
		    }
		     
			return "payslip";
		}
}
