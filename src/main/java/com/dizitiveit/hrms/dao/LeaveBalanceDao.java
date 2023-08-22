package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.LeaveBalance;
import com.dizitiveit.hrms.model.PaySlipLineItem;

public interface LeaveBalanceDao extends JpaRepository<LeaveBalance,Integer>{
	
	LeaveBalance findByEmployee(Employee employee);
	
	 @Query(value = "select * FROM leave_balance where employee_emplyoee_code=?1 and year=?2 and leave_name=?3", nativeQuery = true)
	 LeaveBalance findByItemType(long employeeCode,int year,String leaveName);

}
