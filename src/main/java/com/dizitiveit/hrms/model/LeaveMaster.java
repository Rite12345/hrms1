package com.dizitiveit.hrms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class LeaveMaster {

	@Id
	@GeneratedValue
	private long leaveId;
	private String leaveType;
	private String carryForward;
	private boolean status;
	private String period;
	private int count;
	
}
