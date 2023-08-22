package com.dizitiveit.hrms.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.BankDetails;

@Repository
public interface BankDetailsDao extends JpaRepository<BankDetails, Long>

{

    BankDetails findBybankId(long bankId);
	
	Boolean existsBybankId(long bankId);
	
	Boolean existsByEmployee(Employee emp);

	BankDetails findByEmployee(Employee employee);	
	
	
	@Query(value = "select * FROM dems.bank_details order by bank_id asc", nativeQuery = true)
	List<BankDetails> findAll();
}
