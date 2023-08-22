package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.LeaveMaster;
import com.dizitiveit.hrms.model.LeavesAudit;

public interface LeavesAuditDao extends JpaRepository<LeavesAudit, Long> {

	LeavesAudit findByLeaveAuditId(long leaveAuditId);
	

	@Query(value = "select * FROM  leaves_audit where employee_emplyoee_code=?1 and leave_master_leave_id=?2 and MONTH(last_credited) =?3 and YEAR(last_credited) =?4", nativeQuery = true)
	List<LeavesAudit> findByEmployee(long employeeCode,long leaveId,long month,long year);
		
	 
	@Query(value = "select * FROM  leaves_audit where employee_emplyoee_code=?1 and leave_master_leave_id=?2 and MONTH(last_credited) =?3 and YEAR(last_credited) =?4 and financial_year=?5", nativeQuery = true)
	LeavesAudit findByEmployee(long employeeCode,long leaveId,long month,long year, String financialYear);
	
	

	@Query(value = "select * FROM  leaves_audit where employee_emplyoee_code=?1 and financial_year=?2 and leave_master_leave_id=?3 order by leave_audit_id desc", nativeQuery = true)
	List<LeavesAudit> getByFinancialYear(long employeeCode,String financialYear,long leaveId);
	
	
	@Query(value = "select *  FROM leaves_audit where employee_emplyoee_code=?1", nativeQuery = true)
	List<LeavesAudit> findByEmployee(long employeeCode);
	
	
	
	@Query(value = "select *  FROM leaves_audit where employee_emplyoee_code=?1 and leave_master_leave_id=?2 order by leave_audit_id desc limit 1", nativeQuery = true)
	LeavesAudit findByQuarter(long employeeCode, long leaveId);
	
	

	@Query(value = "select *  FROM leaves_audit where employee_emplyoee_code=?1 and leave_master_leave_id=?2 order by leave_audit_id desc limit 1", nativeQuery = true)
	LeavesAudit findByemployee(long employeeCode, long leaveId);

	
	@Query(value = "select *  FROM leaves_audit where employee_emplyoee_code=?1 and leave_master_leave_id=?2", nativeQuery = true)
	LeavesAudit getByEmployee(long employeeCode, long leaveId);
	
	
	@Query(value = "select *  FROM leaves_audit where employee_emplyoee_code=?1 and leave_master_leave_id=?2", nativeQuery = true)
	List<LeavesAudit> findByEmployee(long employeeCode, long leaveId);


	
	@Query(value = "select *  FROM leaves_audit where employee_emplyoee_code=?1 and leave_master_leave_id=?2", nativeQuery = true)
	LeavesAudit findByquater(long employeeCode, long leaveId);
	
	@Query(value = "select *  FROM leaves_audit where employee_emplyoee_code=?1 and leave_master_leave_id=?2 order by leave_audit_id desc limit 1", nativeQuery = true)
	LeavesAudit getByleaveBalance(long employeeCode, long leaveId);
				
}
