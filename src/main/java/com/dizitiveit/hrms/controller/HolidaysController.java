package com.dizitiveit.hrms.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.HolidaysDao;
import com.dizitiveit.hrms.model.Holidays;
import com.dizitiveit.hrms.pojo.Responses;

@RequestMapping("/holidays")
@RestController
public class HolidaysController 
{

	@Autowired
	private HolidaysDao holidaysDao;
	
	
	@PostMapping("/addHolidays")
	public ResponseEntity<?> addHolidays(@RequestBody Holidays holidays) {

		holidays.setLastModifiedDate(new Date());
		holidaysDao.save(holidays);
		return ResponseEntity.ok(Responses.builder().message("Holidays Saved Successfully.").build());
	}
	
	
	
	@PostMapping("/updateHolidays/{holidaysId}")
	public ResponseEntity<?> updateHolidays(@RequestBody Holidays holidays, @PathVariable int holidaysId) {
		
		Holidays empHolidaysUpdate = holidaysDao.findByHolidaysId(holidaysId);
		if (empHolidaysUpdate != null) {
			
			empHolidaysUpdate.setHolidayName(holidays.getHolidayName());
			empHolidaysUpdate.setHolidayDate(holidays.getHolidayDate());
			empHolidaysUpdate.setHolidayType(holidays.getHolidayType());
			holidaysDao.save(empHolidaysUpdate);	
		} 
		return ResponseEntity.ok(Responses.builder().message("Holidays details Updated Successfully.").build());
	}
	
	
	@GetMapping("getHolidays")
	public ResponseEntity<?> getEmpHolidays() {
		List<Holidays> holidays = holidaysDao.findAll();
		if (holidays.size() > 0) {
			HashMap<String, List<Holidays>> map = new HashMap<String, List<Holidays>>();
			map.put("Holidays", holidays);
			return ResponseEntity.ok(map);
		} else {
			return ResponseEntity.ok("Data not found.");
		}
	}

	
	@GetMapping("getHolidaysMonth/{month}/{year}")
	public ResponseEntity<?> getHolidaysMonth(@PathVariable int month, @PathVariable int year) {
		List<Holidays> holidays = holidaysDao.getbyholidaysMonth(month,year);
		if (holidays!=null) 
		{
			HashMap<String, List<Holidays>> map = new HashMap<String, List<Holidays>>();
			map.put("Holidays", holidays);
			return ResponseEntity.ok(map);
		} else {
			return ResponseEntity.ok("Data not found.");
		}
	}

	
	
	@DeleteMapping("/deleteHolidays/{holidaysId}")
	public ResponseEntity<?> deleteHolidays(@PathVariable int holidaysId) {
		if (holidaysDao.existsByHolidaysId(holidaysId)) 
		{
			holidaysDao.deleteById(holidaysId);
			
			return ResponseEntity.ok(Responses.builder().message("Holidays Details Deleted Successfully.").build());
			
		} 
		else 
		{
			return ResponseEntity.badRequest().body(Responses.builder().message(" Not found empHolidaysId " + holidaysId).build());
			
		}
	}
	

}
