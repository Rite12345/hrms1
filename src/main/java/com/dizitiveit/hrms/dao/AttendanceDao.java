package com.dizitiveit.hrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.AdditionalDetails;
import com.dizitiveit.hrms.model.Attendance;
import com.dizitiveit.hrms.model.Employee;

public interface AttendanceDao extends JpaRepository<Attendance, Integer> {

	List<Attendance> findByEmployee(Employee employee);

	Attendance findByattendanceId(int attendanceId);
	
	@Query(value = "select * FROM dems.attendance where attendance_id=?1 and MONTH(attendance_day) =?2 and YEAR(attendance_day)=?3", nativeQuery = true)
	Attendance findByattendanceId(int attendanceId,long month,long year);
	
	 @Query(value = "select * FROM dems.attendance  order by  attendance_id asc", nativeQuery = true)
	 List<Attendance> findAll();
	 
	 @Query(value = "select * FROM attendance where employee_emplyoee_code=?1 and attendance_day=?2 ", nativeQuery = true)
	 Attendance findByattendanceDay(long emplyoeeCode,Date attendanceay);
	 
	 @Query(value="SELECT * FROM attendance WHERE MONTH(attendance_day) =?1  and YEAR(attendance_day) =?2 and employee_emplyoee_code=?3 ", nativeQuery = true)
	 List<Attendance> findByemployee(long month,long year,long employeeCode);
	 
	 @Query(value = "select * FROM attendance where employee_emplyoee_code=?1 and  DAY(attendance_day) =?2 and MONTH(attendance_day) =?3  and YEAR(attendance_day)=?4 and in_time is not null", nativeQuery = true)
	 Attendance getByAttendanceDay(long emplyoeeCode,long day,long month,long year);
	
	 @Query(value = "select * FROM attendance where employee_emplyoee_code=?1 and  DAY(attendance_day) =?2 and MONTH(attendance_day) =?3  and YEAR(attendance_day)=?4 and in_time is not null and out_time is null", nativeQuery = true)
	 Attendance getByAttendanceDayByOut(long emplyoeeCode,long day,long month,long year);
	 
	  @Query(value ="select * FROM attendance where employee_emplyoee_code=?1 and  DAY(attendance_day) =?2 and MONTH(attendance_day) =?3  and YEAR(attendance_day)=?4 ", nativeQuery = true) 
	  Attendance getAttendanceToday(long emplyoeeCode,long day,long month,long year);
	 
	  @Query(value = "select * from attendance where MONTH(attendance_day) =?1  and YEAR(attendance_day)=?2 and employee_emplyoee_code=?3 ", nativeQuery = true)
			List<Attendance> getByLop(int month, long year,long employeeCode);
	  
	  @Query(value="SELECT * FROM attendance WHERE MONTH(attendance_day) =?1  and YEAR(attendance_day) =?2 and employee_emplyoee_code=?3 ", nativeQuery = true)
		 List<Attendance> findByAbsentDays(int month,int year,long employeeCode);
		 
	 
}

