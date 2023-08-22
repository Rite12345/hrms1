 package com.dizitiveit.hrms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;


@Entity
@Data
public class FamilyDetails {

	@Id
	@GeneratedValue
	private int familyDetailsId;
	private String relation;
	private String name;
	private String phoneNumber;
	
    @ManyToOne
    private Employee employee;
    
	
}
