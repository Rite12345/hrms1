package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.FamilyDetails;

@Repository
public interface FamilyDetailsDao extends JpaRepository<FamilyDetails, Integer>{



    Boolean existsByFamilyDetailsId(Integer familyDetailsId);
	
	FamilyDetails findByfamilyDetailsId(Integer familyDetailsId);
	
	Boolean existsByEmployee(Employee employee);
	
	FamilyDetails findByEmployee(Employee employee);
	
	
	@Query(value = "select *  FROM family_details where employee_emplyoee_code=?1 ", nativeQuery = true)
	List<FamilyDetails> findByemployee(Employee employee);
	
	@Query(value = "select * FROM dems.family_details  order by family_details_id asc", nativeQuery = true)
	 List<FamilyDetails> findAll();
}

