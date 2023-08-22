package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.LeaveManagement;
import com.dizitiveit.hrms.model.UnsavedPayrollData;
public interface UnsavedPayrollDataDao extends JpaRepository<UnsavedPayrollData,Long> {

	@Query(value = "SELECT * FROM unsaved_payroll_data WHERE MONTH(created_at) =?1  and YEAR(created_at) =?2  ", nativeQuery = true)
	 List<UnsavedPayrollData> findBymonthAndyear(int month,int year);
}
