package com.dizitiveit.hrms.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.dizitiveit.hrms.model.AdditionalDetails;
import com.dizitiveit.hrms.model.Branch;
import com.dizitiveit.hrms.model.Department;
import com.dizitiveit.hrms.model.Designation;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.Roles;

import lombok.Data;

@Data
public class EmpPojo {

	private String employeeId;
	private String supervisorId;
	private String supervisorName;
	private Date dateOfJoining;
	private String firstName;
	private String lastName;
	private String qualification;
	private Date dateOfbirth;
	private String gender;
	private String permanentAddress;
	private String presentAddress;
	private String phoneNumber;
	private String emergencyContact;
	private String personalEmailId;
	private String officialEmailId;
	private boolean status;
	private String nationality;
	private String maritalStatus;
	private String bloodGroup;
	private String experiance;
	private String adharNumber;
	private String panCardNumber;
	private String location;
	private String deputeBranch;
	
	private Employee employee;
	
}
