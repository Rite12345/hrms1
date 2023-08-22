package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
@Entity
@Data
public class Allowances {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long allowanceId;
	private String allowanceName;
	private String allowanceType;
	private double value;
	private boolean status;
	private boolean active;
	private String lastModfiedBy;
	 @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	 private Date lastModifiedDate;
	 
	 
	
	 @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date startDate;
	 @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date endDate;
	
}
