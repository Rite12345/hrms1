package com.dizitiveit.hrms.pojo;

import lombok.Data;

@Data
public class PayslipLineItemPojo {

	public String currentMonth;
	public int currentYear;
	public String itemDetails;
	public double itemValue;
	public boolean itemType;
}
