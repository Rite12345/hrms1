package com.dizitiveit.hrms.pojo;

import lombok.Data;

@Data

public class LeaveEmpEntitlementPojo {

	private String employeeId;
	private String leaveType;
	private String financialYear;  
	private int openingBalance;
	private String lastCredited;
	 private int leavesCredited;
	 private int leavesApproved;
	 private int leavesAwaitingApproval;
	 private int leaveBalance;
}
