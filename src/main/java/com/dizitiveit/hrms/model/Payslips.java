package com.dizitiveit.hrms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="pay_slips")
public class Payslips {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int payslipId;
	private String month;
	private int year;
	@Lob
	 @Column(name = "payslip")
	  private byte[] payslip;
	@ManyToOne
	private Employee employee;
	private String contentType;
}
