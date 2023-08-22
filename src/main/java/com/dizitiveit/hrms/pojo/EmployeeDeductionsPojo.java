package com.dizitiveit.hrms.pojo;

import java.util.List;

import lombok.Data;

@Data
public class EmployeeDeductionsPojo {

	private List<String> deductionName;
	private int fromMonth;
	private int fromYear;
	private int toMonth;
	private int toYear;
	private List<Double> value;
	private String employeeId;

}
