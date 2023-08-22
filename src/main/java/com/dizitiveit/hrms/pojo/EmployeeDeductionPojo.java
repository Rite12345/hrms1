package com.dizitiveit.hrms.pojo;

import lombok.Data;

@Data
public class EmployeeDeductionPojo {

	private String deductionName;
	private int fromMonth;
	private int fromYear;
	private int toMonth;
	private int toYear;
	private double value;
	private String employeeId;
}
