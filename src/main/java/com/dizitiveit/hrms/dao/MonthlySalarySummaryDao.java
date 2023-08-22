package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Attendance;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.MonthlySalarySummary;

public interface MonthlySalarySummaryDao extends JpaRepository<MonthlySalarySummary, Integer> {

	//MonthlySalarySummary findByMonthlySalaryID(int monthlySalaryID);

	@Query(value = "select * FROM monthly_salary_summary where employee_emplyoee_code=?1 and current_month=?2 and current_year=?3", nativeQuery = true)
	MonthlySalarySummary findByEmployee(long employeeCode, String currentMonth, String currentYear);

	
	
	//MonthlySalarySummary findbyEmployee(Employee employee);
	
	 @Query(value = "select * FROM monthly_salary_summary where employee_emplyoee_code=?1 and current_month=?2 and current_year=?3", nativeQuery = true)
	 MonthlySalarySummary findwithmonthAndYear(long employeeCode,String currentMonth,long currentYear);
	 
	 MonthlySalarySummary findByEmployee (Employee employee);
	 
	 @Query(value = "select monthly_salaryid FROM monthly_salary_summary where employee_emplyoee_code=?1 and current_month=?2 and current_year=?3", nativeQuery = true)
	 int findmonthlysummaryId(long employeeCode,String currentMonth,String currentYear);
	 
	 MonthlySalarySummary findByMonthlySalaryID(int monthlySummaryId);

	@Query(value = "select * FROM monthly_salary_summary where current_month=?1 and current_year=?2", nativeQuery = true)
	MonthlySalarySummary findmonthAndYear(String month, int year);
	
	
	@Query(value = "select * FROM monthly_salary_summary where current_month=?1 and current_year=?2", nativeQuery = true)
	List<MonthlySalarySummary> findMonthAndYear(String month, int year);
	
	 @Query(value = "select * FROM monthly_salary_summary where employee_emplyoee_code=?1 and current_month=?2 and current_year=?3", nativeQuery = true)
	 MonthlySalarySummary findWithYear(long employeeCode,String currentMonth,int currentYear);


	 @Query(value = "select * FROM monthly_salary_summary where current_month=?1 and current_year=?2 and employee_emplyoee_code=?3", nativeQuery = true)
	MonthlySalarySummary findmonthAndYear(String month, int year,long employeeCode);
}
