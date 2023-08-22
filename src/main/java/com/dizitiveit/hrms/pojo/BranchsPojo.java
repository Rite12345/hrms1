package com.dizitiveit.hrms.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BranchsPojo {

	private Integer branchId;
	private String branchName;
	private String city;
	private String state;
	private String country;
	private String phoneNumberOne;
	private String phoneNumberTwo;
	private String emailId;
	private String branchPremisesType;
	private String branchPremisesRent;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date lastModifiedDate;
	private boolean status;
}
