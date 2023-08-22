package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.LeaveMaster;

public interface LeaveMasterDao extends JpaRepository<LeaveMaster, Long> {

	 LeaveMaster findByLeaveId(long leaveId);
	 
	 Boolean existsByLeaveType(String LeaveType);


	 @Query(value = "select * FROM  dems.leave_master  order by  leave_id asc", nativeQuery = true)
	 List<LeaveMaster> findAll();

	 @Query(value = "select *  FROM dems.leave_master  where status=?1 ",nativeQuery = true)
	List<LeaveMaster> findBysetStatus(boolean status);

	 
	 @Query(value = "select leave_type FROM dems.leave_master where status=?1 ", nativeQuery = true)
		List<String> findByLeaveType(boolean status);
	 
	 
	 @Query(value = "select *  FROM dems.leave_master  where leave_type=?1 ",nativeQuery = true)
		LeaveMaster findByleaveType(String leaveType);
		  
	
	
	 
	 @Query(value = "select * FROM leave_master  where period=?1 and status=?2",nativeQuery = true ) 
		List<LeaveMaster> getByPeriod(String period,boolean status);
	
	

	
}
