package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class LeavesAudit {

	@Id
	@GeneratedValue
	private long leaveAuditId;
	private String financialYear;
	private int openingBalance;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date lastCredited;
	
	 private int leavesCredited;
	 private int leavesApproved;
	 private int leavesAwaitingApproval;
	 private int leaveBalance;
	 
	 @ManyToOne
	 private Employee employee;
	 
	 
	 @ManyToOne
	 private LeaveMaster leaveMaster;
	 
	 
	
}
