package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.dizitiveit.hrms.model.Designation;

public interface DesigDao extends JpaRepository<Designation, Integer> {

	Designation findBydesigId(Integer desigId);
	Boolean existsByDesigName(String desigName);
	Designation findByDesigName(String desigName);
	Boolean existsByDesigId(int desigId);
	
	 @Query(value = "select desig_name FROM designation", nativeQuery = true)
	 List<String> findBydesigName();
	 
	 
	 @Query(value = "select * FROM dems.designation order by desig_id asc", nativeQuery = true)
	 List<Designation> findAll();
	 
}
