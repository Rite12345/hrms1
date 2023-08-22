package com.dizitiveit.hrms.pojo;

import lombok.Data;

@Data

public class AuditPojo {

	private String employeeId;
	private long leaveId;
	private String leaveType;
	private int leaveBalance;
	private int leavesAwaitingApproval;
}
