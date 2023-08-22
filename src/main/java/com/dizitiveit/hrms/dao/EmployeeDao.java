package com.dizitiveit.hrms.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.Roles;

public interface EmployeeDao extends JpaRepository<Employee,Long>{
	
	//Optional<Employee> findByEmailIdOrEmpCode(String email,String empCode);
Optional<Employee> findByOfficialEmailIdOrPhoneNumber(String officialEmailId,String phoneNumber);
	
	Employee findByOfficialEmailId(String officialEmailId);
	Employee findByEmployeeId(String employeeId);
	Employee findByPhoneNumber(String phoneNumber);
	//Boolean existsByEmailId(String email);
	Boolean existsByPhoneNumber(String phoneNumber);
	Boolean existsByEmployeeId(String employeeId);
	Boolean existsByAdharNumber(String AdharNumber);
	Boolean existsByPanCardNumber(String panCardNumber);
	Boolean existsByStatus(boolean status);

	List<Employee> findByRoles(Roles role);

	Boolean existsByOfficialEmailId(String officialEmailId);
	
	
	@Query(value = "select supervisor_name FROM dems.employee ", nativeQuery = true)
	List<String> findBySupervisorName();

	@Query(value = "select *  FROM dems.employee  where status=?1 and  employee_id=?2 ", nativeQuery = true)
	Employee findBysetStatus(boolean status, String employeeId);
	
	@Query(value = "select *  FROM dems.employee  where status=?1",nativeQuery = true)
	List<Employee> findBysetStatus(boolean status);
	
	
	@Query(value = "select * FROM dems.employee  order by emplyoee_code", nativeQuery = true)
	 List<Employee> findAll();

	
	
	@Query(value = "select *  FROM dems.employee  where  supervisor_emplyoee_code=?1 ", nativeQuery = true)
	Employee findBySupervisorId(String supervisorId);
	


	@Query(value = "select *  FROM dems.employee  where supervisor_emplyoee_code=?1 ",nativeQuery = true)
	List<Employee> findByEmployee(long supervisorId);

	@Query(value = "select * FROM dems.employee where status=?1", nativeQuery = true)
	List<Employee> findAllEmployee(boolean Status);
}
