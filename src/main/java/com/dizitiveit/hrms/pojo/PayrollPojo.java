package com.dizitiveit.hrms.pojo;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PayrollPojo {

	private String employeeId;
	private String employeeName;
	private String HRA;
	private String Conveyance;
	private String CityAllowances;
	private String pfEmployeerContributor;
	private String PT;
	private String designation;
	private String grade;
	private String aadharNo;
	private String bankName;
	private String accountNo;
	private int totalDays;
	private int daysPaid;
	private String baseBranch;
	private String location;
	private String deputeBranch;
	private int sickLeaves;
	private int casualLeaves;
	private int LOP;
	private String totalEarnings;
	private String totalDeductions;
	private String lopDeduction;
	private String netPay;
	private String basicSalary;
	private String payRollMonth;
	private String payrollDate;
	private String panCardNumber;
	private String dateOfJoining;
	
	
}
