package com.dizitiveit.hrms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.FamilyDetailsDao;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.FamilyDetails;
import com.dizitiveit.hrms.pojo.FamilyDetailsPojo;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/familyDetails")
public class FamilyDetailsController {

	@Autowired
	private FamilyDetailsDao familyDetailsDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	
	@PostMapping("/addFamilyDetails/{employeeId}")
	public ResponseEntity<?> addFamilyDetails(@RequestBody FamilyDetails familyDetails,@PathVariable String employeeId)
	{
		Employee employee=employeeDao.findByEmployeeId(employeeId);
		//FamilyDetails familyDetailsExisting = familyDetailsDao.findByEmployee(employee);
		familyDetails.setEmployee(employee);
		familyDetailsDao.save(familyDetails);
		return ResponseEntity.ok(Responses.builder().message("Family Details Saved Successfully.").build());
		}
	
	@PostMapping("/updateFamilyDetails/{familyDetailsId}")
	public ResponseEntity<?> updateAdditionalDetails(@RequestBody FamilyDetails familyDetails , @PathVariable int familyDetailsId)
	{
		
		FamilyDetails familydetails=familyDetailsDao.findByfamilyDetailsId(familyDetailsId);
		if(familydetails !=null)
		{
		familydetails.setName(familyDetails.getName());
		familydetails.setRelation(familyDetails.getRelation());
		familydetails.setPhoneNumber(familyDetails.getPhoneNumber());
		familyDetailsDao.save(familydetails);
		//return ResponseEntity.ok(familydetails);	
		//return ResponseEntity.ok("Updated Successfully.");
		return ResponseEntity.ok(Responses.builder().message("Family Details Updated Successfully.").build());
	    }else 
	      {
		 return ResponseEntity.badRequest().body(Responses.builder().message("EmployeeID not found").build()); 
	    }
	  }
		
	@GetMapping("getFamilyDetails")
	 public ResponseEntity<?> FamilyDetails() {
	 List<FamilyDetails> familyDetails=familyDetailsDao.findAll();
	 if(familyDetails.size()>0) {
		 HashMap<String,List<FamilyDetails>> map=new HashMap<String, List<FamilyDetails>>();
		   map.put("FamilyDetails", familyDetails);
		   return ResponseEntity.ok(map);
	 } else 
	 { 
		 return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build()); 
	  } 
	 }
		
	 @GetMapping("/getFamilyDetailsId/{employeeId}") 
	 public ResponseEntity<?> getfamilyDetailsId(@PathVariable String employeeId)
      { 
		 Employee employee = employeeDao.findByEmployeeId(employeeId);
		 FamilyDetails familyDetails=familyDetailsDao.findByEmployee(employee);
	   if(familyDetails!=null) 
	 {
	   HashMap<String,FamilyDetails> map=new HashMap<String, FamilyDetails>();
	   map.put("FamilyDetails", familyDetails);
	   return ResponseEntity.ok(map);	 
	 }
	 //return new ResponseEntity<FamilyDetails>(familyDetails,HttpStatus.OK);
	   else 
		 { 
			 return ResponseEntity.badRequest().body(Responses.builder().message("EmployeeId not found").build()); 
		  }
   }
	
	 
	 
	 
	  @GetMapping("/getFamily/{employeeId}")
		 public ResponseEntity<?> getfamilyDetails(@PathVariable String employeeId)
	    {
			Employee famDe = employeeDao.findByEmployeeId(employeeId);
			
			 List<FamilyDetails> familyDetails=familyDetailsDao.findByemployee(famDe);
			 List<FamilyDetailsPojo> familyList = new ArrayList<>();
			 if(familyDetails.size()==0)
			 {
				 FamilyDetails fmaily = new FamilyDetails();
				 return ResponseEntity.ok(fmaily);	
			 }
			 else {
			 for(FamilyDetails fd:familyDetails)
			 {
				FamilyDetailsPojo famDetailsPojo1=new FamilyDetailsPojo();
				famDetailsPojo1.setName(fd.getName());
				famDetailsPojo1.setRelation(fd.getRelation());
				famDetailsPojo1.setPhoneNumber(fd.getPhoneNumber());
				famDetailsPojo1.setEmployeeId(fd.getEmployee().getEmployeeId());
				familyList.add(famDetailsPojo1);
			 }
		        HashMap<String, List<FamilyDetailsPojo>> map=new HashMap<String,  List<FamilyDetailsPojo>>();
		        map.put("FamilyDetails", familyList);
		        return ResponseEntity.ok(map);	
			 }
		       //return new ResponseEntity<FamilyDetails>(familyDetails,HttpStatus.OK);
			
	    }

	 
	 
	  
	@DeleteMapping("/deleteFamilyDetails/{familyDetailsId}")
	public ResponseEntity<?> deleteFamilyDetails(@PathVariable int familyDetailsId)
	{
		FamilyDetails familyDetails=familyDetailsDao.findByfamilyDetailsId(familyDetailsId);
		   if(familyDetails!=null) 
		 {
		familyDetailsDao.deleteById(familyDetailsId);
		return ResponseEntity.ok(Responses.builder().message("Family Details are Deleted Successfully.").build());
		 }
		   else 
			 { 
			   return ResponseEntity.badRequest().body(Responses.builder().message("Family Details Id not found").build()); 
			  }
	}

		
	
}
