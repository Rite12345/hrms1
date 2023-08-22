package com.dizitiveit.hrms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.checkerframework.common.reflection.qual.GetClass;
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
import com.dizitiveit.hrms.dao.DeductionsDao;
import com.dizitiveit.hrms.dao.EmployeeAllowancesDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.EmployeeDeductionsDao;
import com.dizitiveit.hrms.model.Allowances;
import com.dizitiveit.hrms.model.Deductions;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.EmployeeDeductions;
import com.dizitiveit.hrms.pojo.EmployeeDeductionPojo;
import com.dizitiveit.hrms.pojo.EmployeeDeductionsPojo;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/empDeduction")
public class EmployeeDeductionsController {

	
	@Autowired
	private DeductionsDao deductionsDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmployeeDeductionsDao employeeDeductionDao;
	
	
	@PostMapping("saveEmpDeductions/{employeeId}/{deductionName}")
	 public ResponseEntity<?> saveEmpDeductions(@RequestBody EmployeeDeductions employeeDeductions,@PathVariable String employeeId,@PathVariable String deductionName)
	 {
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		Deductions deductions = deductionsDao.findByDeductionName(deductionName);
		if(deductions==null)
		{
			employeeDeductions.setEmployee(employee);
			employeeDeductions.setDeductions(deductions);
			employeeDeductions.setCreatedAt(new Date());
			employeeDeductionDao.save(employeeDeductions);
			employeeDeductions.setStatus(true);
			employeeDeductionDao.save(employeeDeductions);

		return ResponseEntity.ok(Responses.builder().message("EmpDeduction details Saved Sucessfully").build());
	 }
	 
		else
		{
			return ResponseEntity.ok(Responses.builder().message("Deduction name already exists").build());
		}
	 }
	
	@PostMapping("/saveListEmpDeductions/{employeeId}")
	public ResponseEntity<?> saveListEmpDeductions(@RequestBody List<EmployeeDeductionsPojo> employeeDeductionsPojo,
			@PathVariable String employeeId) {
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		System.out.println("size" + employeeDeductionsPojo.size());
		String errorRes = "";
		for (EmployeeDeductionsPojo empdeduction : employeeDeductionsPojo) {
			List<String> empDeduction = deductionsDao.findBydeductionName();
			for (String deductionName : empDeduction) {
				System.out.println("employee allowance name" + empdeduction.getFromMonth());
				Deductions deductions = deductionsDao.findByDeductionName(deductionName);
				System.out.println("month" + empdeduction.getFromMonth());
				System.out.println("year" + empdeduction.getFromYear());
				System.out.println("employee" + employee.getEmplyoeeCode());
				System.out.println("deductions" + deductions.getDeductionId());
				EmployeeDeductions employeeDeductionsExisting = employeeDeductionDao.getExistingRecord(
						empdeduction.getFromMonth(), empdeduction.getFromYear(), employee.getEmplyoeeCode(),
						deductions.getDeductionId());
				if (employeeDeductionsExisting == null) {
					EmployeeDeductions deduction = new EmployeeDeductions();
					deduction.setEmployee(employee);
					deduction.setDeductions(deductions);
					deduction.setFromMonth(empdeduction.getFromMonth());
					deduction.setFromYear(empdeduction.getFromYear());
					deduction.setToMonth(empdeduction.getToMonth());
					deduction.setToYear(empdeduction.getToYear());
					deduction.setCreatedAt(new Date());
					
//					System.out.println("allowanceName---"+allowanceName);
//					System.out.println("empallowance---"+empallowance.getAllowanceName());
					
					double val = 0.0;
					if(empdeduction.getDeductionName().contains(deductionName))
					{
						int index = empdeduction.getDeductionName().indexOf(deductionName);
//						System.out.println("index of dedictopm name: "+index);
						val=empdeduction.getValue().get(index);
//						System.out.println("value of allwance index: "+val);
					}
					deduction.setValue(val);
					
//					List<Double> empValue = employeeDeductionDao.findByValue();
//					for (double value : empValue) {
//						deduction.setValue(value);
//						System.out.println("value: " + value);
//					}

					employeeDeductionDao.save(deduction);
				} else {
					errorRes = errorRes + "," + employeeDeductionsExisting.getDeductions().getDeductionName();
				}
			}
		}
		if (errorRes == "") {
			return ResponseEntity.ok(Responses.builder().message("Saved Sucessfully").build());
		} else {
			return ResponseEntity.badRequest().body(
					Responses.builder().message(errorRes + " Deductions for this month already exists").build());
		}

	}
	
	

	@GetMapping("/getAllDeductionsForEmployee/{employeeId}/{month}/{year}")
	public ResponseEntity<?> getAllDeductionsForEmployee(@PathVariable String employeeId, @PathVariable int month,
			@PathVariable int year) {
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		List<EmployeeDeductions> employeeDeductionsList = employeeDeductionDao.getEmployeeDeductions(year,
				employee.getEmplyoeeCode());
		if (employeeDeductionsList.size() != 0) {
			List<EmployeeDeductionPojo> empDeductionsPojo = new ArrayList<EmployeeDeductionPojo>();
			for (EmployeeDeductions empDeductions : employeeDeductionsList) {
				EmployeeDeductionPojo deductionPojo = new EmployeeDeductionPojo();
				deductionPojo.setEmployeeId(employeeId);
				deductionPojo.setDeductionName(empDeductions.getDeductions().getDeductionName());
				deductionPojo.setFromMonth(empDeductions.getFromMonth());
				deductionPojo.setFromYear(empDeductions.getFromYear());
				deductionPojo.setToMonth(empDeductions.getToMonth());
				deductionPojo.setToYear(empDeductions.getToYear());
				deductionPojo.setValue(empDeductions.getValue());
				empDeductionsPojo.add(deductionPojo);
			}
			HashMap<String, List<EmployeeDeductionPojo>> map = new HashMap<String, List<EmployeeDeductionPojo>>();
			map.put("EmployeeDeductions", empDeductionsPojo);
			return ResponseEntity.ok(map);
		} else {
			return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build());
		}
	}
		
	 @PutMapping("updateEmpDeduc/{empDeductId}")
	  public ResponseEntity<?> updateEmpDeduc(@RequestBody EmployeeDeductions employeeDeductions,@PathVariable long empDeductId)
	  {
		  EmployeeDeductions empDeduc=employeeDeductionDao.findByEmpDeductionId(empDeductId);
	  if(empDeduc!=null)
	  {
		  empDeduc.setDeductionName(employeeDeductions.getDeductionName());
		  empDeduc.setFromMonth(employeeDeductions.getFromMonth());
		  empDeduc.setFromYear(employeeDeductions.getFromYear());
		  empDeduc.setToMonth(employeeDeductions.getToMonth());
		  empDeduc.setToYear(employeeDeductions.getToYear());
		  empDeduc.setStatus(employeeDeductions.isStatus());
		  empDeduc.setValue(employeeDeductions.getValue());
		  employeeDeductionDao.save(empDeduc);
	  return ResponseEntity.ok("Employee Deductions Updated Successfully.");
	  }
	  else
	  {
		  return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build());
		  }
	  }
	
	@DeleteMapping("/deleteEmpDeduc/{empDeductId}")
	public ResponseEntity<?> deleteEmpDeduc(@PathVariable long empDeductId)
	{
		EmployeeDeductions empDeducDetails=employeeDeductionDao.findByEmpDeductionId(empDeductId);
		if(empDeducDetails!=null)
		{
			employeeDeductionDao.deleteById(empDeductId);
			return ResponseEntity.ok("Employee Deduction details are Deleted Successfully.");
			//return ResponseEntity.ok(Responses.builder().message("Employee Deduction details are Deleted Successfully.").build());
		 }
		else
			 {
			return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build());
			  }
	}
}
