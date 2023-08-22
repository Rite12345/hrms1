package com.dizitiveit.hrms.pojo;

import java.util.List;

import lombok.Data;

@Data
public class EmployeeAllowancesPojo {

	 private long empAllowanceId;
	 private int fromMonth;
	 private int fromYear;
	 private int toMonth;
	 private int toYear;
	 private List<Double> value;
	 private String employeeId;
	 private List<String> allowanceName;
}
