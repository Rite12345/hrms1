package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Branch {

	@Id
	@GeneratedValue
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
	//private String CompanyId;
	
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhoneNumberOne() {
		return phoneNumberOne;
	}
	public void setPhoneNumberOne(String phoneNumberOne) {
		this.phoneNumberOne = phoneNumberOne;
	}
	public String getPhoneNumberTwo() {
		return phoneNumberTwo;
	}
	public void setPhoneNumberTwo(String phoneNumberTwo) {
		this.phoneNumberTwo = phoneNumberTwo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getBranchPremisesType() {
		return branchPremisesType;
	}
	public void setBranchPremisesType(String branchPremisesType) {
		this.branchPremisesType = branchPremisesType;
	}
	
	public String getBranchPremisesRent() {
		return branchPremisesRent;
	}
	public void setBranchPremisesRent(String branchPremisesRent) {
		this.branchPremisesRent = branchPremisesRent;
	}
	
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}
