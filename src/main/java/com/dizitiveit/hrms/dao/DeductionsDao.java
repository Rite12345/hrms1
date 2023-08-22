package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Allowances;
import com.dizitiveit.hrms.model.Deductions;

public interface DeductionsDao extends JpaRepository<Deductions, Long> {


	Boolean existsByDeductionName(String deductionName);
	Deductions findByDeductionId(long deductionId);
	
	List<Deductions> findByDeductionType(String deductionType);	
	
	Deductions findByDeductionName(String deductionName);
	
	Deductions deleteById(long deductionId);
	
	 @Query(value = "select deduction_name FROM deductions where status=true", nativeQuery = true)
	 List<String> findBydeductionName();
	 
	 
	 @Query(value = "select * FROM dems.deductions order by deduction_id asc", nativeQuery = true)
	 List<Deductions> findAll();
		
	 
}
