package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class LeaveBalance {

	@Id
	@GeneratedValue
	private int leaveId;
	private String leaveName;
	private String leaveShortForm;
	private int entitledLeaves;
	private int usedLeaves;
	private int year;
	private String status;
	private Date updatedDate;
	
	@ManyToOne
	private Employee employee;
}
