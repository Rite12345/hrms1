package com.dizitiveit.hrms.model;

import java.time.LocalTime;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
public class AttendanceSummary {

	@Id
	@GeneratedValue
	private int attendanceSummaryId;
	

	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime inTime;
	
	@JsonFormat(pattern = "HH:mm:ss")
	//@DateTimeFormat(pattern="yyyy.MM.dd HH:mm:ss")
	private LocalTime outTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	  @Temporal(TemporalType.DATE)
	private Date attendanceDay;
	
	@ManyToOne
	private Employee employee;
}
