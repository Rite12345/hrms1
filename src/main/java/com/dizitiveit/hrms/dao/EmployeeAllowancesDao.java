package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Designation;
import com.dizitiveit.hrms.model.EmployeeAllowances;
import com.dizitiveit.hrms.model.EmployeeDeductions;


public interface EmployeeAllowancesDao extends JpaRepository<EmployeeAllowances,Long> {

	 @Query(value = "SELECT * FROM employee_allowances WHERE MONTH(effect_from_date) =?1  and YEAR(effect_from_date) =?2 and employee_emplyoee_code=?3 and allowances_allowance_id=?4", nativeQuery = true)
	 EmployeeAllowances findByExistingRecord(long month,long year,long emplyoeeCode,long allowanceId);
	 
	 @Query(value = "SELECT * FROM employee_allowances WHERE MONTH(effect_from_date) =?1  and YEAR(effect_from_date) =?2 and employee_emplyoee_code=?3 ", nativeQuery = true)
	 List<EmployeeAllowances> findByemployee(long month,long year,long emplyoeeCode);
	 
	 
	 @Query(value = "select * FROM dems.employee_allowances order by emp_allowance_id asc", nativeQuery = true)
	 List<EmployeeAllowances> findAll();

	 
	 @Query(value = "select * FROM hrms_new1.employee_allowances where allowance_name=?1", nativeQuery = true)
	 EmployeeAllowances findByAllowanceName(String allowanceName);
	 
	 @Query(value = "SELECT * FROM employee_allowances WHERE  from_year =?1 and employee_emplyoee_code=?2 ", nativeQuery = true)
	 List<EmployeeAllowances> getEmployeeAllowances(int year,long emplyoeeCode);
	
	 
	 @Query(value = "SELECT * FROM employee_allowances WHERE from_month=?1  and from_year =?2 and employee_emplyoee_code=?3 and allowances_allowance_id=?4", nativeQuery = true)
	 EmployeeAllowances getExistingRecord(int month,int year,long emplyoeeCode,long allowanceId);
	 
	 @Query(value = "SELECT * FROM employee_allowances WHERE employee_emplyoee_code=?1 and from_year=?2 and allowances_allowance_id=?3", nativeQuery = true)
	 List<EmployeeAllowances> getLeastEmpAllowances(long emplyoeeCode,int fromYear,long allowanceId);
	 
	 @Query(value = "SELECT * FROM employee_allowances WHERE from_year =?1 and employee_emplyoee_code=?2 and allowances_allowance_id=?3", nativeQuery = true)
	 List<EmployeeAllowances> getAllowancePercentage(int year,long emplyoeeCode,long allowanceId);
	 
	 @Query(value = "SELECT * FROM employee_allowances WHERE from_month=?1  and from_year =?2 and employee_emplyoee_code=?3 and allowances_allowance_id=?4", nativeQuery = true)
	 EmployeeAllowances  getAllowancesPercentage(int month,int year,long emplyoeeCode,long allowanceId);
	 
	 EmployeeAllowances findByEmpAllowanceId(long empAllowanceId);
	
	 
	 //@Query(value = "select * FROM dems.allowances order by allowance_name asc", nativeQuery = true)
	 //List<String> findByAllowancename();
	 
	 @Query(value = "select * FROM employee_allowances order by value asc", nativeQuery = true)
	 List<Double> findByValue();
}
