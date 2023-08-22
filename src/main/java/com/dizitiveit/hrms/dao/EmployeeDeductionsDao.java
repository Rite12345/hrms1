package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.EmployeeAllowances;
import com.dizitiveit.hrms.model.EmployeeDeductions;

public interface EmployeeDeductionsDao extends JpaRepository<EmployeeDeductions,Long> {
	
	
	 @Query(value = "SELECT * FROM employee_deductions WHERE MONTH(effect_from_date) =?1  and YEAR(effect_from_date) =?2 and employee_emplyoee_code=?3 and deductions_deduction_id=?4", nativeQuery = true)
	 EmployeeDeductions findByExistingDeduction(long month,long year,long emplyoeeCode,long deductionId);
	 
	 @Query(value = "SELECT * FROM employee_deductions WHERE MONTH(effect_from_date) =?1  and YEAR(effect_from_date) =?2 and employee_emplyoee_code=?3 ", nativeQuery = true)
	 List<EmployeeDeductions> getByemployee(long month,long year,long emplyoeeCode);
	 
	 @Query(value = "SELECT * FROM employee_deductions WHERE from_month=?1  and from_year =?2 and employee_emplyoee_code=?3 and deductions_deduction_id=?4", nativeQuery = true)
	 EmployeeDeductions getExistingRecord(int month,int year,long emplyoeeCode,long deductionId);
	 
	 @Query(value = "SELECT * FROM employee_deductions WHERE from_year=?1 and employee_emplyoee_code=?2", nativeQuery = true)
	 List<EmployeeDeductions> getEmployeeDeductions(int year,long emplyoeeCode);
	
	
	 @Query(value = "select * FROM dems.employee_deductions order by emp_deduction_id asc", nativeQuery = true)
	 List<EmployeeDeductions> findAll();
	
	 @Query(value = "SELECT * FROM employee_deductions WHERE employee_emplyoee_code=?1 and from_year=?2 and deductions_deduction_id=?3 ", nativeQuery = true)
	 List<EmployeeDeductions> getLeastEmpDeductions(long emplyoeeCode,int fromYear,long deductionId);
	 
	 @Query(value = "SELECT * FROM employee_deductions WHERE from_year =?1 and employee_emplyoee_code=?2 and deductions_deduction_id=?3", nativeQuery = true)
	 List<EmployeeDeductions> getPercentage(int year,long emplyoeeCode,long deductionId);
	 
	 @Query(value = "SELECT * FROM employee_deductions WHERE from_month=?1  and from_year =?2 and employee_emplyoee_code=?3 and deductions_deduction_id=?4", nativeQuery = true)
	 EmployeeDeductions getUpdateDeductions(int month,int year,long emplyoeeCode,long deductionId);
	 
	 EmployeeDeductions findByEmpDeductionId(long empDeductionId);
	 
	 @Query(value = "select * FROM employee_deductions order by value asc", nativeQuery = true)
	 List<Double> findByValue();
}
