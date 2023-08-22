package com.dizitiveit.hrms.pojo;

import lombok.Data;

@Data
public class AttendancePojo {

    private String employeeId;
    private int attendanceId;
    private String empName;
	private String attendanceDay;
  	private String totalHours;
  	private String inTime;
	private String outTime;
	
	
	
}
