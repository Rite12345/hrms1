
package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.LeaveEmployeeEntitlement;

public interface LeaveEmpEntitlementDao extends JpaRepository<LeaveEmployeeEntitlement, Long> {

	
	
	@Query(value = "select *  FROM leave_employee_entitlement where employee_emplyoee_code=?1 and leave_master_leave_id=?2", nativeQuery = true)
	LeaveEmployeeEntitlement findByemployee(long employeeCode, long leaveId);

	
	
	@Query(value = "select *  FROM leave_employee_entitlement where employee_emplyoee_code=?1", nativeQuery = true)
	List<LeaveEmployeeEntitlement> findByEmployee(long employeeCode);
	
	
	
	@Query(value = "select *  FROM leave_employee_entitlement where leaves_audit_leave_audit_id=?1", nativeQuery = true)
	LeaveEmployeeEntitlement findByLeaveAudit(long leaveAuditId);

	
	
	
	
	@Query(value = "select * FROM  leave_employee_entitlement where employee_emplyoee_code=?1 and financial_year=?2", nativeQuery = true)
	LeaveEmployeeEntitlement getByfinancialYear(long employeeCode,String financialYear);


	
	@Query(value = "select * FROM  leave_employee_entitlement where employee_emplyoee_code=?1 and leave_master_leave_id=?2", nativeQuery = true)
	LeaveEmployeeEntitlement findByquater(long emplyoeeCode, long leaveId);



	

	
	

	
	
}
