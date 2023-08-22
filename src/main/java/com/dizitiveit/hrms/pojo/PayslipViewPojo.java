package com.dizitiveit.hrms.pojo;

import java.util.List;

import com.dizitiveit.hrms.model.PaySlipLineItem;

import lombok.Data;
@Data
public class PayslipViewPojo {

	private String employeeId;
	private String employeeName;
	private String panNumber;
	
	
	private String dateOfJoining;
	
	private String location;
	private String deputeBranch;
	
	private String accountNumber;
	private String bankName;
	private String baseBranch;
	private String desigName;
	private String grade;
	private List<PayslipLineItemPojo> paySlipLineItemList; 
	private double earninnings;
	private double deductions;
	private double netSalary;
	private int sickLeaves;
	private int casualLeaves;
	private int lopDays;
	private double LOP;
	private int daysPaid;
}
