package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class LeaveManagement {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long leaveId;
	private String leaveName;
	private String shortForm;
	private int year;
	private String totalLeaves;
	private int casualLeaves;
	private int sickLeaves;
	private int LOP;
	private int totalDays;
	private boolean status;
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	//  @Temporal(TemporalType.DATE)
	//private Date fromDate;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	//  @Temporal(TemporalType.DATE)
//	private Date toDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date createdAt;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date lastModifiedDate;
	@ManyToOne
	private Employee employee;
	
}
