package com.dizitiveit.hrms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import lombok.Data;

@Entity
@Table(name = "otp_validation")
//@Data
public class OtpValidation {

      @Id	
	  @GeneratedValue(strategy=GenerationType.AUTO)
	    @Column(name = "otp_id")
	    private Long otpId;
		
		
		  @Column(name = "otp")
		    private String otp;
		  
		  @Column(name="mobile")
			private String mobile;
		  
		  @Column(name="status")
		  private String status;
		  
		  @Column(name="signature")
		  private String signature;

		public Long getOtpId() {
			return otpId;
		}

		public void setOtpId(Long otpId) {
			this.otpId = otpId;
		}

		public String getOtp() {
			return otp;
		}

		public void setOtp(String otp) {
			this.otp = otp;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}
		  
		  
}
