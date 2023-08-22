package com.dizitiveit.hrms.model;

import java.io.Serializable;

public class OtpResponse implements Serializable {

	private static final long serialVersionUID = 1L; private final String otp;
	  
	  public OtpResponse(String otp) { this.otp = otp; }
	  
	  public String getOtp() { return otp; }
}
