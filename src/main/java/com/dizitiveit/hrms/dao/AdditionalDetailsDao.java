package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.AdditionalDetails;
import com.dizitiveit.hrms.model.Company;
import com.dizitiveit.hrms.model.Employee;

@Repository
public interface AdditionalDetailsDao extends JpaRepository<AdditionalDetails, Integer>{

	AdditionalDetails findByAdditionalDetailsId(Integer additionalDetailsId);	
	Boolean existsByEmployee(Employee employee);
	AdditionalDetails findByEmployee(Employee employee);
	
	
	
	 @Query(value = "select * FROM dems.additional_details order by additional_details_id asc", nativeQuery = true)
	 List<AdditionalDetails> findAll();
	
}
