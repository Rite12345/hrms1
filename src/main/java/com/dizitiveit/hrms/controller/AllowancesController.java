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

import com.dizitiveit.hrms.dao.AllowancesDao;
import com.dizitiveit.hrms.model.Allowances;
import com.dizitiveit.hrms.model.Roles;
import com.dizitiveit.hrms.pojo.Responses;


@RestController
@RequestMapping("/allowances")
public class AllowancesController {

	
	@Autowired
	private AllowancesDao allowancesDao;

	@PostMapping("/saveAllowances")
	 public ResponseEntity<?> saveAllowances(@RequestBody Allowances allowances)
	 {
		if(allowancesDao.existsByAllowanceName(allowances.getAllowanceName()))
		{
			Allowances allowancesUpdate = allowancesDao.findByAllowanceName(allowances.getAllowanceName());
			allowancesUpdate.setStatus(allowances.isStatus());
			allowancesUpdate.setAllowanceType(allowances.getAllowanceType());
			allowancesUpdate.setStartDate(allowances.getStartDate());
			allowancesUpdate.setEndDate(allowances.getEndDate());
			allowancesUpdate.setValue(allowances.getValue());
			allowancesUpdate.setLastModifiedDate(new Date());
			allowancesDao.save(allowancesUpdate);
			return ResponseEntity.ok(Responses.builder().message
					("Allowances Details Updated  Sucessfully").build());
		}
		else {
			allowancesDao.save(allowances);
			return ResponseEntity.ok(Responses.builder().message("Allowances Details Saved Sucessfully").build());
		}
	 }
	
	
	@GetMapping("/getAllowances/{allowanceName}")
	 public ResponseEntity<?> getAllowances (@PathVariable String allowanceName ){
		Allowances allowances = allowancesDao.findByAllowanceName(allowanceName);
		if(allowances!=null)
		{
		HashMap<String,Allowances> map=new HashMap<String, Allowances>();
		   map.put("Allowances", allowances);
		   return ResponseEntity.ok(map);	
		}
		else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("Allowance Name"+""+allowanceName+""+"Not Found").build());
		}
		   
	}
	
	@GetMapping("listOfAllowances")
	 public ResponseEntity<?> listOfAllowances()
	 {
		List<Allowances> allowances = allowancesDao.findAll();
		HashMap<String,List<Allowances>> map=new HashMap<String, List<Allowances>>();
		   map.put("Allowances", allowances);
		   return ResponseEntity.ok(map);
		
	 }
	
	@GetMapping("/getAllAllowanceNames")
	 public ResponseEntity<?> getAllAllowanceNames()
	 {
		List<String> allowances = allowancesDao.findByallowanceName();
		HashMap<String,List<String>> map=new HashMap<String,List<String>>();
		   map.put("Allowances", allowances);
		   return ResponseEntity.ok(map);	
	 }
	
	@DeleteMapping("/deleteAllowances/{allowanceId}")
	 public ResponseEntity<?> deleteAllowances(@PathVariable long allowanceId)
	 {
		Allowances allowances = allowancesDao.deleteById(allowanceId);
		return ResponseEntity.ok(Responses.builder().message("Allowances Details Deleted Sucessfully").build());
		   		
	 }
	
	@PostMapping("/deactiveAllowances/{allowanceName}")
	public ResponseEntity<?> deactiveAllowances(@PathVariable String allowanceName )
	{
		Allowances allowances = allowancesDao.findByAllowanceName(allowanceName);
		if(allowances != null)
		{
			allowances.setStatus(false);
			allowancesDao.save(allowances);
			return ResponseEntity.ok(Responses.builder().message("Allowances Details saved Sucessfully").build());
		}
		else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("Allowance Name"+""+allowanceName+""+"Not Found").build());
		}
	}
	
	
	
	@PostMapping("/saveAllowance")
	public ResponseEntity<?> saveAllowance(@RequestBody Allowances allowances)
	{
     if (allowancesDao.existsByAllowanceName(allowances.getAllowanceName())) {
    	
     
    	 Allowances allowancesUpdate = allowancesDao.findByAllowanceName(allowances.getAllowanceName());
			allowancesUpdate.setStatus(allowances.isStatus());
			allowancesUpdate.setAllowanceType(allowances.getAllowanceType());
			allowancesUpdate.setStatus(true);
			allowancesUpdate.setLastModifiedDate(new Date());
			allowancesDao.save(allowancesUpdate);
			return ResponseEntity.ok(Responses.builder().message("Allowances Details Updated  Sucessfully").build());	
		} 
		else 
		{
			allowances.setStatus(true);
		   	allowancesDao.save(allowances);
			return ResponseEntity.ok(Responses.builder().message("Allowances Details Saved Sucessfully").build());
		}
	}
	
	 @GetMapping("getAllowance")
	 public ResponseEntity<?> getAllowance() {
		List<Allowances> allowances = allowancesDao.findAll();
		HashMap<String, List<Allowances>> map = new HashMap<String, List<Allowances>>();
		map.put("Allowances", allowances);
		return ResponseEntity.ok(map);

	}
	
	
}
