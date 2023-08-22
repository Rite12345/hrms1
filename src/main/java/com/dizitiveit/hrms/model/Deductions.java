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
public class Deductions {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long deductionId;
	private String deductionName;
	private String deductionType;
	private boolean status;
	 @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date lastModifiedDate;
}
