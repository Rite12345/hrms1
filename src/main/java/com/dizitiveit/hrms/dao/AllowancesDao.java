package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.Allowances;
public interface AllowancesDao extends JpaRepository<Allowances,Long>{

	Boolean existsByAllowanceName(String allowanceName);
	Allowances findByAllowanceId(long allowanceId);
	
	List<Allowances> findByAllowanceType(String allowanceType);
	
	Allowances findByAllowanceName(String allowanceName);
	
	Allowances deleteById(long allowanceId);
	
	 @Query(value = "select allowance_name FROM allowances where status=true", nativeQuery = true)
	 List<String> findByallowanceName();
	 
	 
	 @Query(value = "select * FROM dems.allowances order by allowance_id asc", nativeQuery = true)
	 List<Allowances> findAll();
	 
	
		
}
