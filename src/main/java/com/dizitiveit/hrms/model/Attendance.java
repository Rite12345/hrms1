package com.dizitiveit.hrms.model;

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


import lombok.Data;

@Entity
@Data
public class Attendance {

	@Id
	@GeneratedValue
	private int attendanceId;
		
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime inTime;
	
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime outTime;


	@DateTimeFormat(pattern="yyyy-MM-dd")
	 @Temporal(TemporalType.DATE)
	private Date attendanceDay;
	 
	@DateTimeFormat(pattern="yyyy-MM-dd")
	 @Temporal(TemporalType.DATE)
	private Date createdAt;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date lastModifiedDate;
	
	private String totalHours;
	private int permissionHours;
	
	
	@ManyToOne
	private Employee employee;
	
}
