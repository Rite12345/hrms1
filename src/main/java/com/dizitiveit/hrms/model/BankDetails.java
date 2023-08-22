package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class BankDetails {

	@Id
	@GeneratedValue
	private long bankId;
	private String accountHolderName;
	private String ifscCode;
	private String accountNumber;
	private String bankName;
	private String branchName;
	private String ownedBy;
	private String status;
	  @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date lastModifiedDate;
	
	

		@OneToOne
		private Employee employee;
}
