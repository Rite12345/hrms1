package com.dizitiveit.hrms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.AllowancesDao;
import com.dizitiveit.hrms.dao.EmployeeAllowancesDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.model.Allowances;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.EmployeeAllowances;
import com.dizitiveit.hrms.model.EmployeeDeductions;
import com.dizitiveit.hrms.pojo.EmployeeAllowancePojo;
import com.dizitiveit.hrms.pojo.EmployeeAllowancesPojo;
import com.dizitiveit.hrms.pojo.EmployeeDeductionsPojo;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/empAllowance")
public class EmployeeAllowancesController {

	@Autowired
	private AllowancesDao allowancesDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmployeeAllowancesDao employeeAllowanceDao;
	
	
	@PostMapping("saveEmpAllowance/{employeeId}/{allowanceName}")
	 public ResponseEntity<?> saveEmployeeAllowance(@RequestBody EmployeeAllowances employeeAllowances,@PathVariable String employeeId,@PathVariable String allowanceName)
	 {
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		Allowances allowances = allowancesDao.findByAllowanceName(allowanceName);
		if(allowances==null)
		{
		employeeAllowances.setEmployee(employee);
		employeeAllowances.setAllowances(allowances);
		employeeAllowanceDao.save(employeeAllowances);
		employeeAllowances.setCreatedAt(new Date());
		employeeAllowanceDao.save(employeeAllowances);
		employeeAllowances.setStatus(true);
		 employeeAllowanceDao.save(employeeAllowances);
		return ResponseEntity.ok(Responses.builder().message("EmpAllowance details Saved Sucessfully").build());
	 }
	 
		else
		{
			return ResponseEntity.ok(Responses.builder().message("Allowances name already exists").build());
		}
	 }
	
		
	@GetMapping("/getAllAllowancesForEmployee/{employeeId}/{month}/{year}")
	public ResponseEntity<?> getAllDeductionsForEmployee(@PathVariable String employeeId, @PathVariable int month,
			@PathVariable int year) {
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		List<EmployeeAllowances> employeeAllowancesList = employeeAllowanceDao.getEmployeeAllowances(year, employee.getEmplyoeeCode());
		if (employeeAllowancesList.size() != 0) {
			List<EmployeeAllowancePojo> empAllowancesPojo = new ArrayList<EmployeeAllowancePojo>();
			for (EmployeeAllowances empAllowances : employeeAllowancesList) {
				EmployeeAllowancePojo allowancePojo = new EmployeeAllowancePojo();
				allowancePojo.setEmpAllowanceId(empAllowances.getEmpAllowanceId());
				allowancePojo.setEmployeeId(employeeId);
				allowancePojo.setAllowanceName(empAllowances.getAllowances().getAllowanceName());
				allowancePojo.setFromMonth(empAllowances.getFromMonth());
				allowancePojo.setFromYear(empAllowances.getFromYear());
				allowancePojo.setToMonth(empAllowances.getToMonth());
				allowancePojo.setToYear(empAllowances.getToYear());
				allowancePojo.setValue(empAllowances.getValue());
				empAllowancesPojo.add(allowancePojo);
			}
			HashMap<String, List<EmployeeAllowancePojo>> map = new HashMap<String, List<EmployeeAllowancePojo>>();
			map.put("EmployeeDeductions", empAllowancesPojo);
			return ResponseEntity.ok(map);
		} else {
			return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build());
		}
	}
	
	@PutMapping("updateEmpAllow/{empAllowanceId}")
	public ResponseEntity<?> updateEmpAllow(@RequestBody EmployeeAllowances employeeAllowances,@PathVariable long empAllowanceId)
	{
		EmployeeAllowances empAllow=employeeAllowanceDao.findByEmpAllowanceId(empAllowanceId);
		if(empAllow!=null)
		{
			empAllow.setFromMonth(employeeAllowances.getFromMonth());
			empAllow.setFromYear(employeeAllowances.getFromYear());
			empAllow.setToMonth(employeeAllowances.getToMonth());
			empAllow.setToYear(employeeAllowances.getToYear());
			empAllow.setValue(employeeAllowances.getValue());
			empAllow.setStatus(employeeAllowances.isStatus());
			//empAllow.setAllowAllowId(employeeAllowances.getAllowAllowId());
			employeeAllowanceDao.save(empAllow);
			//return ResponseEntity.ok("Employee Allowances Updated Successfully.");
			return ResponseEntity.ok(Responses.builder().message("Employee Allowances Updated Successfully.").build());
	    }
		else
	    {
			
			return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build());
	    }
	}
		
	
	@DeleteMapping("/deleteEmpAllow/{empAllowanceId}")
	public ResponseEntity<?> deleteEmp(@PathVariable long empAllowanceId)
	{
		EmployeeAllowances empDetails=employeeAllowanceDao.findByEmpAllowanceId(empAllowanceId);
		if(empDetails!=null)
		{
			employeeAllowanceDao.deleteById(empAllowanceId);
			//return ResponseEntity.ok("Employee Allowances are Deleted Successfully.");
			return ResponseEntity.ok(Responses.builder().message("Employee Allowances are Deleted Successfully.").build());
		 }
		   else
			 {
			   return (ResponseEntity<?>) ResponseEntity.badRequest();
			  // return ResponseEntity.badRequest().body(Responses.builder().message("No data found").build());
			  }
		}
	
	
	
	@PostMapping("/saveListEmpAllownaces/{employeeId}")
	public ResponseEntity<?> saveListEmpAllownaces(@RequestBody List<EmployeeAllowancesPojo> employeeAllowancesPojo,
			@PathVariable String employeeId) {
		Employee employee = employeeDao.findByEmployeeId(employeeId);
//		System.out.println("employeeId" + employee.getEmplyoeeCode());
//		System.out.println("size" + employeeAllowancesPojo.size());
		String errorRes = "";
		List<String> empAllowance = allowancesDao.findByallowanceName();
//		System.out.println("empAllowance:" + empAllowance);

		for (EmployeeAllowancesPojo empallowance : employeeAllowancesPojo) {
//			System.out.println("employee allowance name" + empallowance.getFromMonth());
//			System.out.println("month" + empallowance.getFromMonth());
//			System.out.println("year" + empallowance.getFromYear());

			for (String allowanceName : empAllowance) {
				Allowances allowances = allowancesDao.findByAllowanceName(allowanceName);
//				System.out.println("allowanceId" + allowances.getAllowanceId());

				EmployeeAllowances employeeAllowancesExisting = employeeAllowanceDao.getExistingRecord(
						empallowance.getFromMonth(), empallowance.getFromYear(), employee.getEmplyoeeCode(),
						allowances.getAllowanceId());
				if (employeeAllowancesExisting == null) {
					EmployeeAllowances allowance = new EmployeeAllowances();
					allowance.setEmployee(employee);
					allowance.setAllowances(allowances);
					allowance.setFromMonth(empallowance.getFromMonth());
					allowance.setFromYear(empallowance.getFromYear());
					allowance.setToMonth(empallowance.getToMonth());
					allowance.setToYear(empallowance.getToYear());
					allowance.setCreatedAt(new Date());
					
//					System.out.println("allowanceName---"+allowanceName);
//					System.out.println("empallowance---"+empallowance.getAllowanceName());
					
					double val = 0.0;
					if(empallowance.getAllowanceName().contains(allowanceName))
					{
						
						int index = empallowance.getAllowanceName().indexOf(allowanceName);
//						System.out.println("index of allwance name: "+index);
						val=empallowance.getValue().get(index);
//						System.out.println("value of allwance index: "+val);
					}
					allowance.setValue(val);
					

//					List<Double> empValue = employeeAllowanceDao.findByValue();
//					for (double value : empValue) {
//						allowance.setValue(value);
////						System.out.println("value: " + value);
//					}
					employeeAllowanceDao.save(allowance);

				} else {
					errorRes = errorRes + "," + employeeAllowancesExisting.getAllowances().getAllowanceName();
				}
			}
		}
		if (errorRes == "") {
			return ResponseEntity.ok(Responses.builder().message("Saved Sucessfully").build());
		} else {
			return ResponseEntity.badRequest()
					.body(Responses.builder().message(errorRes + " Allowance for this month already exists").build());
		}

	}
	  

	
}

