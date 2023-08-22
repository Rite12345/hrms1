package com.dizitiveit.hrms.pojo;

import lombok.Data;

@Data
public class EmployeeAllowancePojo {

	 private long empAllowanceId;
	 private int fromMonth;
	 private int fromYear;
	 private int toMonth;
	 private int toYear;
	 private Double value;
	 private String employeeId;
	 private String allowanceName;
}
