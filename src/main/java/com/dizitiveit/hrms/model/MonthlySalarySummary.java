package com.dizitiveit.hrms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class MonthlySalarySummary {

	@Id
	@GeneratedValue
	private int monthlySalaryID;
	private String currentMonth;
	private int currentYear;
	private double totalEarnings;
	private double totalDeductions;
	private double netSalary;
	private int basicSalary;
	private int daysPaid;

	@ManyToOne
	private Employee employee;

}
