package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.dizitiveit.hrms.model.Company;

@Repository
public interface CompanyDao extends JpaRepository<Company, Integer>{

	Company findBycompanyId(Integer companyId);
	Boolean existsByCompanyName(String companyName);
	Boolean existsByCompanyId(int companyId);
	
	 @Query(value = "select *  FROM dems.company  where status=?1 ",nativeQuery = true)
		List<Company> findBysetStatus(boolean status);
	
	
	@Query(value = "select * FROM dems.company order by company_id asc", nativeQuery = true)
	 List<Company> findAll();
	
	
}
