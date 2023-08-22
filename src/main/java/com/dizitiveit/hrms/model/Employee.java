package com.dizitiveit.hrms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;


@Entity
@Table(name="employee")
@Data
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long emplyoeeCode;
	
	private String employeeId;
	private String supervisorName;
	  @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date dateOfJoining;
	private String firstName;
	private String lastName;
	private String qualification;
	  @DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date dateOfbirth;
	private String gender;
	private String permanentAddress;
	//private String addressTwo;
	private String presentAddress;
	private String phoneNumber;
	private String emergencyContact;
	private String personalEmailId;
	private String officialEmailId;
	private boolean status;
	private Date lastModifiedDate;
	private String nationality;
	private String maritalStatus;
	private String bloodGroup;
	//private String technicalQualification;
	private String experiance;
	private String adharNumber;
	private String panCardNumber;
	private Date createdAt;
	private String location;
	private String deputeBranch;
	
	
	@ManyToOne
	private Roles roles;
	
	@OneToOne
	private Branch branch;
	
	@OneToOne
	private Department department;
	
	@OneToOne
	private Designation desg;	
	
	@ManyToOne
	private Employee supervisor;
}
