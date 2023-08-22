package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class PaySlipLineItem {

	@Id
	@GeneratedValue
	private int payItemId;
	private String currentMonth;
	private int currentYear;
	private String itemDetails;
	private double itemValue;
	private boolean itemType;
	private String updatedBy;
	private Date updatedDate;

	@ManyToOne
	private MonthlySalarySummary monthlySalarySummary;
}
