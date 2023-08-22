package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
@Data
@Entity
public class EmployeeAllowances {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long empAllowanceId;
	private int fromMonth;
	private int fromYear;
	private int toMonth;
	private int toYear;
	private boolean status;
	
	private double value;

	 @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date  effectFromDate;
	 @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	 private Date lastModifiedDate;
	 @DateTimeFormat(pattern="dd-MM-yyyy")
	 private Date createdAt;
	 
	 
	 @OneToOne
	 private Employee employee;
	 @ManyToOne
	 private Allowances allowances;
}
