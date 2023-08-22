package com.dizitiveit.hrms.pojo;

import lombok.Data;

@Data
public class PaySlipListPojo {

	public String employeeId;
	public String currentMonth;
	public int currentYear;
	public String itemDetails;
	public double itemValue;
	public boolean itemType;
}
