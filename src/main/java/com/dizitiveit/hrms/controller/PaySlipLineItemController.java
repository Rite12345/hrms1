package com.dizitiveit.hrms.controller;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.EmployeeAllowancesDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.EmployeeDeductionsDao;
import com.dizitiveit.hrms.dao.LeaveBalanceDao;
import com.dizitiveit.hrms.dao.MonthlySalarySummaryDao;
import com.dizitiveit.hrms.dao.PaySlipLineItemDao;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.EmployeeAllowances;
import com.dizitiveit.hrms.model.EmployeeDeductions;
import com.dizitiveit.hrms.model.LeaveBalance;
import com.dizitiveit.hrms.model.MonthlySalarySummary;
import com.dizitiveit.hrms.model.PaySlipLineItem;
import com.dizitiveit.hrms.pojo.EmployeePojo;
import com.dizitiveit.hrms.pojo.PaySlipListPojo;
import com.dizitiveit.hrms.pojo.Responses;



@RestController
@RequestMapping("/paySlipLineItem")
public class PaySlipLineItemController {

	@Autowired
	private PaySlipLineItemDao paySlipLineItemDao;

	@Autowired
	private MonthlySalarySummaryDao monthlySalarySummaryDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmployeeAllowancesDao employeeAllowancesDao;

	@Autowired
	private EmployeeDeductionsDao employeeDeductionsDao;

	@PostMapping("/savePaySlip/{employeeId}/{month}/{year}")
	public ResponseEntity<?> savePaySlip(@RequestBody PaySlipLineItem paySlipItem, @PathVariable String employeeId,@PathVariable int month,@PathVariable int year)
	{
		  Month months = Month.of(month);
		  System.out.println(months.name());
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		if(employee!=null)
		{
		 MonthlySalarySummary monthlySalarySummaryExisting = monthlySalarySummaryDao.findWithYear(employee.getEmplyoeeCode(), months.name(), year);
		 if(monthlySalarySummaryExisting!=null)
			{
			 MonthlySalarySummary monthly=monthlySalarySummaryDao.findByMonthlySalaryID(monthlySalarySummaryExisting.getMonthlySalaryID());
			 PaySlipLineItem paySlipLineItem = paySlipLineItemDao.findByItemDetails(paySlipItem.getItemDetails(), months.name(), year,monthlySalarySummaryExisting.getMonthlySalaryID());
				if(paySlipLineItem==null)
				{
					
				paySlipItem.setMonthlySalarySummary(monthly);
				paySlipItem.setCurrentMonth(months.name());
				paySlipItem.setCurrentYear(year);
				paySlipItem.setUpdatedDate(new Date());	
				paySlipLineItemDao.save(paySlipItem);
				return ResponseEntity.ok(Responses.builder().message(" PaySlipLineItem saved successfully").build());
				}
					else {
						return ResponseEntity.badRequest().body(Responses.builder().message("Payslip details already present for this month").build());
					}
				}
				else {
					return ResponseEntity.badRequest().body(Responses.builder().message("please enter the basic salary for the employee").build());
				}
		}
		else {
			return ResponseEntity.badRequest().body(Responses.builder().message("No data available for employee").build());
		}
		
	}
	
	
	@GetMapping("/getItemDetails")
	public ResponseEntity<?> getItemDetails() {
		List<String> paySlip = paySlipLineItemDao.findByItemDetails();
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		map.put("ItemDetails", paySlip);
		return ResponseEntity.ok(map);
	}
	
	
	
	@GetMapping("/getPaySlipList/{employeeId}")
	public ResponseEntity<?> getLeave(@PathVariable String employeeId) {
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		MonthlySalarySummary monthly=monthlySalarySummaryDao.findByEmployee(employee);
		List<PaySlipLineItem> payList = paySlipLineItemDao.findByMonthlySalarySummary(monthly.getMonthlySalaryID());
		List<PaySlipListPojo> payslipPojoList = new ArrayList<PaySlipListPojo>();
		for (PaySlipLineItem payslip : payList) {
			PaySlipListPojo payslipPojo = new PaySlipListPojo();
			payslipPojo.setEmployeeId(payslip.getMonthlySalarySummary().getEmployee().getEmployeeId());
			payslipPojo.setCurrentMonth(payslip.getCurrentMonth());
			payslipPojo.setCurrentYear(payslip.getCurrentYear());
			payslipPojo.setItemDetails(payslip.getItemDetails());
			payslipPojo.setItemType(payslip.isItemType());
			payslipPojo.setItemValue(payslip.getItemValue());
			payslipPojoList.add(payslipPojo);
			
		}

		HashMap<String, List<PaySlipListPojo>> map = new HashMap<String, List<PaySlipListPojo>>();
		map.put("PaySlipList", payslipPojoList);
		return ResponseEntity.ok(map);

	}
	
	
	
	
}
