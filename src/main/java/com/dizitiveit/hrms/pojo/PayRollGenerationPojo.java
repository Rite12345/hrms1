package com.dizitiveit.hrms.pojo;

import java.util.Date;
import java.util.List;

import com.dizitiveit.hrms.model.MonthlySalarySummary;
import com.dizitiveit.hrms.model.PaySlipLineItem;
import lombok.Data;
@Data
public class PayRollGenerationPojo {

	private String  employeeId;
	private String employeeName;
	private String Designation;
	private String grade;
	private String location;
	private String baseBranch;
	private String deputeBranch;
	private String panCardNumber;
	private String bankName;
	private String accountNumber;	
	private String monthAndYear;
	private int daysPaid;
	private String generatedDate;
	private Date dateOfJoining;
	private int sickLeave;
	private int casualLeave;
	private double lop;
	private int lopDays;
	private MonthlySalarySummary monthlySalarySummary;
	private List<PaySlipLineItem> allowances;
	private List<PaySlipLineItem> deductions;
	
}
