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

import com.dizitiveit.hrms.dao.DeductionsDao;
import com.dizitiveit.hrms.model.Allowances;
import com.dizitiveit.hrms.model.Deductions;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/deductions")
public class DeductionsController {

	@Autowired
	private DeductionsDao deductionsDao;
	
	@PostMapping("/saveDeductions")
	 public ResponseEntity<?> saveAllowances(@RequestBody Deductions deductions)
	 {
		if(deductionsDao.existsByDeductionName(deductions.getDeductionName()))
		{
			Deductions deductionsUpdate = deductionsDao.findByDeductionName(deductions.getDeductionName());
			deductionsUpdate.setDeductionType(deductions.getDeductionType());
			deductionsUpdate.setStatus(deductions.isStatus());
			deductionsUpdate.setDeductionName(null);
			deductionsUpdate.setStatus(true);
			deductionsDao.save(deductionsUpdate);
			return ResponseEntity.ok(Responses.builder().message("Deduction Details Updated  Sucessfully").build());
		}
		else {
			deductionsDao.save(deductions);
			return ResponseEntity.ok(Responses.builder().message("Deduction Details Saved Sucessfully").build());
		}
	 }
	
	@GetMapping("/getDeductions/{deductionName}")
	 public ResponseEntity<?> getDeductions (@PathVariable String deductionName ){
		Deductions deductions = deductionsDao.findByDeductionName(deductionName);
		if(deductions!=null)
		{
		HashMap<String,Deductions> map=new HashMap<String, Deductions>();
		   map.put("Deductions", deductions);
		   return ResponseEntity.ok(map);	
		}
		else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("Deduction Name"+""+deductionName+""+"Not Found").build());
		}
		   
	}
	
	@GetMapping("/getAllDeductionNames")
	 public ResponseEntity<?> getAllDeductionNames()
	 {
		List<String> deductions = deductionsDao.findBydeductionName();
		HashMap<String,List<String>> map=new HashMap<String,List<String>>();
		   map.put("Deductions", deductions);
		   return ResponseEntity.ok(map);	
	 }
	
	@GetMapping("listOfDeductions")
	 public ResponseEntity<?> listOfDeductions()
	 {
		List<Deductions> deductions = deductionsDao.findAll();
		HashMap<String,List<Deductions>> map=new HashMap<String, List<Deductions>>();
		   map.put("Deductions", deductions);
		   return ResponseEntity.ok(map);
		
	 }
	
	@DeleteMapping("/deleteDeductions/{deductionId}")
	 public ResponseEntity<?> deleteAllowances(@PathVariable long deductionId)
	 {
		Deductions deductions = deductionsDao.deleteById(deductionId);
		return ResponseEntity.ok(Responses.builder().message("Deduction Details Deleted Sucessfully").build());
		   		
	 }
	
	@PostMapping("/deactiveDeductions/{deductionName}")
	public ResponseEntity<?> deactiveAllowances(@PathVariable String deductionName )
	{
		Deductions deductions = deductionsDao.findByDeductionName(deductionName);
		if(deductions != null)
		{
			deductions.setStatus(false);
			deductionsDao.save(deductions);
			return ResponseEntity.ok(Responses.builder().message("Deduction Details saved Sucessfully").build());
		}
		else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("Deduction Name"+""+deductionName+""+"Not Found").build());
		}
	}
	
	
	@PostMapping("/saveDeduction")
	public ResponseEntity<?> saveDeduction(@RequestBody Deductions deduction) 
	{
		if (deductionsDao.existsByDeductionName(deduction.getDeductionName())) {
			
			
			Deductions deductionsUpdate = deductionsDao.findByDeductionName(deduction.getDeductionName());
			deductionsUpdate.setDeductionType(deduction.getDeductionType());
			deductionsUpdate.setDeductionName(deduction.getDeductionName());
			deductionsUpdate.setStatus(deduction.isStatus());
			deduction.setLastModifiedDate(new Date());
			deductionsUpdate.setStatus(true);
			deductionsDao.save(deductionsUpdate);
			return ResponseEntity.ok(Responses.builder().message("Deduction Details Updated  Sucessfully").build());
		} 
		
		else {
			
			deduction.setStatus(true);
			deductionsDao.save(deduction);
			return ResponseEntity.ok(Responses.builder().message("Deduction Details Saved Sucessfully").build());
		}
		
		
	}
	
	
	@GetMapping("getDeduction")
	public ResponseEntity<?> getDeduction() {
		List<Deductions> deduction = deductionsDao.findAll();
		HashMap<String, List<Deductions>> map = new HashMap<String, List<Deductions>>();
		map.put("Deductions", deduction);
		return ResponseEntity.ok(map);

	}
	
	
}
