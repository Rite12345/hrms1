package com.dizitiveit.hrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Branch;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.LeaveRequest;

public interface LeaveRequestDao extends JpaRepository<LeaveRequest, Long>{

	
LeaveRequest findByLeaveRequestId(long leaveRequestId);
	
	List<LeaveRequest> findByEmployee(Employee emp);
   
	
	@Query(value = "select *  FROM dems.leave_request  where  employee_emplyoee_code=?1 order by leave_request_id desc", nativeQuery = true)
	List<LeaveRequest> findbyEmployee(Employee emp);
	
		 @Query(value = "SELECT * from leave_request  WHERE ?1 between start_date and end_date  and status=?2 and employee_emplyoee_code=?3", nativeQuery = true)
	 LeaveRequest getByStart(Date date,String status,long employeeCode);
	
	 @Query(value = "select * FROM dems.leave_request where status=?1", nativeQuery = true)
	 List<LeaveRequest> findBysetStatus(String status);
	 
	 @Query(value = "select * FROM  leave_request where  start_date>=?1 and end_date<=?2  and employee_emplyoee_code=?3", nativeQuery = true)
	 LeaveRequest getByStartAndEnd(Date startDate,Date endDate,long employeeCode);

	 @Query(value = "select * FROM dems.leave_request order by leave_request_id asc", nativeQuery = true)
	 List<LeaveRequest> findAll();


	@Query(value = "select *  FROM dems.leave_request  where status=?1 and employee_emplyoee_code=?2 ", nativeQuery = true)
	List<LeaveRequest> findBysetStatus(String status, long emplyoeeCode);

	
	boolean existsByLeaveRequestId(long leaveRequestId);
		
	LeaveRequest findByStatus(String status);
	
	@Query(value = "select *  FROM dems.leave_request where supervisor_emplyoee_code=?1 and status=?2", nativeQuery = true)
	List<LeaveRequest> findBysupervisorId(long employeeCode,String status);
		
	
	@Query(value = "select *  FROM leave_request where supervisor_emplyoee_code=?1", nativeQuery = true)
	List<LeaveRequest> findBysupervisorId(long employeeCode);
	
	
	@Query(value = "select *  FROM dems.leave_request  where employee_emplyoee_code=?1 and MONTH(start_date) = ?2 and YEAR(start_date) = ?3", nativeQuery = true)
	List<LeaveRequest> getLeaveView(long emplyoeeCode, long month, long year);
	
	@Query(value = "select *  FROM dems.leave_request  where employee_emplyoee_code=?1 and (MONTH(start_date) =?2 or MONTH(end_date) =?3 ) and YEAR(start_date)=?4 and YEAR(end_date)=?5 and status=?6", nativeQuery = true)
	List<LeaveRequest> getLeavecount(long emplyoeeCode, int startMonth,int endMonth, int startYear,int endYear,String status);
}