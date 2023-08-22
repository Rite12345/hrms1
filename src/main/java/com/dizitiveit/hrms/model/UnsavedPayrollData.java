package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class UnsavedPayrollData {

	@Id
	@GeneratedValue
	private long unsavedPayrollId;
	private String employeeId;
	private String employeeName;
	private double HRA;
	private double Conveyance;
	private double CityAllowances;
	private double pfEmployeerContributor;
	private double PT;
	private double basicSalary;
	private int sickLeaves;
	private int casualLeaves;
	private int LOP;
	private int dayspaid;
	private String bankName;
	private String accountNumbe;
	private String baseBranch;
	private String location;
	private String deputeBranch;
	private Date createdAt;
	private String errorReason;
}
