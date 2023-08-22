package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;


import lombok.Data;

@Entity
@Data
public class LeaveRequest {

	@Id
	@GeneratedValue
	private long leaveRequestId;
	private String leaveName;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date startDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date endDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date lastModifyDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date createdAt;
	
	private String shortForm;
	private String status;
	private String comment;
	
	
	@ManyToOne
	private Employee employee;
	
	@ManyToOne
	private Employee supervisor;
	
}
