package com.dizitiveit.hrms.model;

import java.util.Date;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
public class AdditionalDetails {

	@Id
	@GeneratedValue
	private int additionalDetailsId;
	private String alternateNumber;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date weddingDay;
	
	private String passportNumber;
	private String photoDisplay;
	private String photo;
	
	
	@OneToOne
	private Employee employee;
	
	
}
