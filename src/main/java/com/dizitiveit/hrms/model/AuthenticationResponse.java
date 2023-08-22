package com.dizitiveit.hrms.model;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable{

	 private static final long serialVersionUID = 1L;
	  private final String jwt;
	  private final String roleName;
	  private String employeeId;
	  
	  public String getJwt() { 
		  return jwt; 
		  }
	  


	public String getRoleName() {
		return roleName;
	}


	public String getEmployeeId() {
		return employeeId;
	}



	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}



	public AuthenticationResponse(String jwt,String roleName,String employeeId) {
		super();
		this.jwt = jwt;
		this.roleName = roleName;
		this.employeeId = employeeId;
	} 	
}
