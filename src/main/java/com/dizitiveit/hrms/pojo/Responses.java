package com.dizitiveit.hrms.pojo;

import lombok.Builder;

//import lombok.Builder;
//import lombok.Data;

@Builder
public class Responses {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}
