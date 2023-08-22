package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.dizitiveit.hrms.model.Department;
import com.dizitiveit.hrms.model.OtpValidation;

public interface OtpValidationDao extends JpaRepository<OtpValidation, Long>{

	OtpValidation findById(long otpId);

	OtpValidation findBymobile(String mobile);
	
	@Query(value = "select * FROM dems.otp_validation order by otp_id asc", nativeQuery = true)
	List<OtpValidation> findAll();
	
}
