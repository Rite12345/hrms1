package com.dizitiveit.hrms.controller;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.AllowancesDao;
import com.dizitiveit.hrms.dao.BankDetailsDao;
import com.dizitiveit.hrms.dao.DeductionsDao;
import com.dizitiveit.hrms.dao.EmployeeAllowancesDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.EmployeeDeductionsDao;
import com.dizitiveit.hrms.dao.LeaveManagementDao;
import com.dizitiveit.hrms.model.BankDetails;
import com.dizitiveit.hrms.model.Department;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.EmployeeAllowances;
import com.dizitiveit.hrms.model.EmployeeDeductions;
import com.dizitiveit.hrms.model.LeaveManagement;
import com.dizitiveit.hrms.pojo.PayrollPojo;

@RestController
@RequestMapping("/payroll")
public class PayrollGenerationController {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmployeeDeductionsDao employeeDeductionDao;
	
	@Autowired
	private EmployeeAllowancesDao empAllowancesDao;
	
	@Autowired
	private LeaveManagementDao leaveManagementDao;
	
	@Autowired
	private BankDetailsDao bankDetailsDao;
	
	@Autowired
	private AllowancesDao allowancesDao;
	
	@Autowired
	private DeductionsDao deductionsDao;
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	

	@GetMapping("/genratingPayroll/{employeeId}/{month}/{year}")	
	public ResponseEntity<?>  generatingPayroll(@PathVariable String employeeId,@PathVariable long month,@PathVariable long year)
	{
		double allowancesTotal=0;
		double deductionsTotal=0;
	     
		
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		PayrollPojo payroll = new PayrollPojo();
		payroll.setAadharNo(employee.getAdharNumber());
		payroll.setDesignation(employee.getDesg().getDesigName());
		payroll.setGrade(employee.getDesg().getGrade());
		payroll.setEmployeeId(employee.getEmployeeId());
		String Name = employee.getFirstName() +" "+ employee.getLastName();
		payroll.setEmployeeName(Name);
		payroll.setBaseBranch(employee.getBranch().getBranchName());
		payroll.setLocation(employee.getLocation());
		payroll.setDeputeBranch(employee.getDeputeBranch());
		payroll.setPanCardNumber(employee.getPanCardNumber());
		DateFormat dateOfJoiningFormat = new SimpleDateFormat("dd-MM-yyyy");
		payroll.setDateOfJoining(dateOfJoiningFormat.format(employee.getDateOfJoining()));
			
		  BankDetails bankDetails = bankDetailsDao.findByEmployee(employee);
          if(bankDetails!=null)
          {
		  payroll.setAccountNo(bankDetails.getAccountNumber());
		  payroll.setBankName(bankDetails.getBankName());
          }
		 
		LeaveManagement leaveManagement = leaveManagementDao.getByemployee(month, year, employee.getEmplyoeeCode());
		payroll.setCasualLeaves(leaveManagement.getCasualLeaves());
		payroll.setSickLeaves(leaveManagement.getSickLeaves());
		payroll.setLOP(leaveManagement.getLOP());
		payroll.setTotalDays(leaveManagement.getTotalDays());
		int daysPaid = leaveManagement.getTotalDays();
		SimpleDateFormat month_date = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		//String strDate = dateFormat.format(leaveManagement.getCreatedAt());
		String month_name = month_date.format(leaveManagement.getCreatedAt());
		System.out.println("Month :" + month_name);
		payroll.setPayRollMonth(month_name);
		
		List<EmployeeAllowances> employeeAllowancesList = empAllowancesDao.findByemployee(month,year,employee.getEmplyoeeCode());
		System.out.println(employeeAllowancesList.size());
		for(EmployeeAllowances employeeAllowance : employeeAllowancesList )
		{
			if(employeeAllowance.getAllowances().getAllowanceName().equalsIgnoreCase("HRA"))
			{
				
				payroll.setHRA(df.format(employeeAllowance.getValue()));
			}
			else if(employeeAllowance.getAllowances().getAllowanceName().equalsIgnoreCase("Conveyance"))
			{
				payroll.setConveyance(df.format(employeeAllowance.getValue()));
			}
			else if(employeeAllowance.getAllowances().getAllowanceName().equalsIgnoreCase("CityAllowances"))
			{
				payroll.setCityAllowances(df.format(employeeAllowance.getValue()));
				
			}
			else if(employeeAllowance.getAllowances().getAllowanceName().equalsIgnoreCase("pfEmployeerContributor"))
			{
				payroll.setPfEmployeerContributor(df.format(employeeAllowance.getValue()));
			
			}
			
			else if(employeeAllowance.getAllowances().getAllowanceName().equalsIgnoreCase("BasicSalary"))
			{
				
				payroll.setBasicSalary(df.format(employeeAllowance.getValue()));
			
			}
			
			 allowancesTotal = allowancesTotal+employeeAllowance.getValue();
			 System.out.println(allowancesTotal);
			 //payroll.setPayrollDate(employeeAllowance.getCreatedAt());
			 SimpleDateFormat payrolldate = new SimpleDateFormat(" dd MMM yyyy  HH:mm", Locale.ENGLISH);
				DateFormat dateFormatPayroll = new SimpleDateFormat("dd-mm-yy HH:mm");
				String payrollGenerationdate = payrolldate.format(employeeAllowance.getCreatedAt());
				payroll.setPayrollDate(payrollGenerationdate);
		
		}
	
		payroll.setTotalEarnings(df.format(allowancesTotal));
		
		List<EmployeeDeductions> employeeDeductionsList = employeeDeductionDao.getByemployee(month,year,employee.getEmplyoeeCode());
		for(EmployeeDeductions employeeDeduction : employeeDeductionsList)
		{
			if(employeeDeduction.getDeductions().getDeductionName().equalsIgnoreCase("PT"))
			{
				
				payroll.setPT(df.format(employeeDeduction.getValue()));
			}
			
			 deductionsTotal = deductionsTotal+employeeDeduction.getValue();
		}
		if(payroll.getLOP()>0)
		{
			double oneDaySal= Math.round(allowancesTotal/payroll.getTotalDays());
			System.out.println("one day paid is"+ payroll.getTotalDays());
			System.out.println("one day salary is"+oneDaySal);
			double lopDeduction = Math.round(oneDaySal* leaveManagement.getLOP());
			System.out.println("lop deduction is"+lopDeduction);
			payroll.setLopDeduction(df.format(lopDeduction));
			deductionsTotal=deductionsTotal+lopDeduction;
			daysPaid = daysPaid-payroll.getLOP();
		}
		payroll.setTotalDeductions(df.format(deductionsTotal));
		payroll.setDaysPaid(daysPaid);
		double NetSalary =  Math.round(allowancesTotal - deductionsTotal);
		payroll.setNetPay(df.format(NetSalary));
	
		HashMap<String, PayrollPojo> map=new HashMap<String, PayrollPojo>();
	      map.put("PayRoll", payroll);
	      return ResponseEntity.ok(map);
		
	}

}
