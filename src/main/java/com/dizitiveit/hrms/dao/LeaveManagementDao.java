package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.EmployeeDeductions;
import com.dizitiveit.hrms.model.LeaveManagement;


public interface LeaveManagementDao extends JpaRepository<LeaveManagement,Long> {

LeaveManagement findByEmployee(Employee employee);
	
List<LeaveManagement> findByemployee(Employee employee);

Boolean existsByEmployee(Employee employee);

 @Query(value = "SELECT * FROM leave_management WHERE MONTH(created_at) =?1  and YEAR(created_at) =?2 and employee_emplyoee_code=?3", nativeQuery = true)
 LeaveManagement findByExistingLeave(long month,long year,long emplyoeeCode);

 @Query(value = "SELECT * FROM leave_management WHERE MONTH(created_at) =?1  and YEAR(created_at) =?2 and employee_emplyoee_code=?3 ", nativeQuery = true)
 LeaveManagement getByemployee(long month,long year,long emplyoeeCode);
	
}
