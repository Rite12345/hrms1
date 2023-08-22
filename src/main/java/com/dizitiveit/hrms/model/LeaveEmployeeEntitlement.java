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
public class LeaveEmployeeEntitlement {

	@Id
	@GeneratedValue
	private long leaveEmployeeId;
	
	
	private String financialYear;
	  
	private int openingBalance;

	 private int leavesCredited;
	 private int leavesApproved;
	 private int leavesAwaitingApproval;
	 private int leaveBalance;
	
		
 
	 @ManyToOne
	 private LeavesAudit leavesAudit;
		 
	
	
}
