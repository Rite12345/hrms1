package com.dizitiveit.hrms.pojo;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class LeaveRequestPojo {

	private String employeeId;
	private String empName;
	private long leaveRequestId;
	private String supervisorId;
	private String supervisorName;
	private String leaveName;
	private String startDate;
	
	
	private String endDate;
	
	private String shortForm;
	private String status;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date createdAt;
	
	private String comment;
	
	
}
