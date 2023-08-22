package com.dizitiveit.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Payslips;

public interface PayslipsDao extends JpaRepository<Payslips, Long> {

	 @Query(value = "select * FROM pay_slips where employee_emplyoee_code=?1 and month=?2 and year=?3 ", nativeQuery = true)
	 Payslips getByMonthAndYear(long employeeCode,String currentMonth,int currentYear);
	 
	 

	 @Query(value = "select * FROM pay_slips where employee_emplyoee_code=?1 and month=?2 and year=?3 ", nativeQuery = true)
	 Payslips getByMonthAndYear(long employeeCode,int currentMonth,String currentYear);
}
