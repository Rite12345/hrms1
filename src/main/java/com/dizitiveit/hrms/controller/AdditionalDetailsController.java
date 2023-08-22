package com.dizitiveit.hrms.controller;

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

import com.dizitiveit.hrms.dao.AdditionalDetailsDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.model.AdditionalDetails;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.pojo.Responses;


@RequestMapping("/additionalDetails")
@RestController
public class AdditionalDetailsController {

	@Autowired
	private AdditionalDetailsDao additionalDetailsDao;
	
	
	@Autowired
	private EmployeeDao employeeDao;
	@PostMapping("/addAdditionalDetails/{employeeId}")
	public ResponseEntity<?> addAdditionalDetails(@RequestBody AdditionalDetails additionalDetails,@PathVariable String employeeId)
	{
		
		Employee employee=employeeDao.findByEmployeeId(employeeId);
		AdditionalDetails addiDetailsUpdate= additionalDetailsDao.findByEmployee(employee);
		if(addiDetailsUpdate !=null)
			{
				
			addiDetailsUpdate.setAlternateNumber(additionalDetails.getAlternateNumber());
			addiDetailsUpdate.setWeddingDay(additionalDetails.getWeddingDay());
			addiDetailsUpdate.setPassportNumber(additionalDetails.getPassportNumber());
			addiDetailsUpdate.setPhoto(additionalDetails.getPhoto());
			addiDetailsUpdate.setPhotoDisplay(additionalDetails.getPhotoDisplay());
			additionalDetailsDao.save(addiDetailsUpdate);
			return ResponseEntity.ok(Responses.builder().message("Additional Details Updated Successfully.").build());
			}
			else 
			 { 
	            additionalDetails.setEmployee(employee);
		        additionalDetailsDao.save(additionalDetails);
		        return ResponseEntity.ok(Responses.builder().message("Additional Details are saved sucessfully").build());
			 }
		 
	}
	
	
	
	@PostMapping("/updateAdditionalDetails/{employeeId}")
	public ResponseEntity<?> updateAdditionalDetails( @RequestBody AdditionalDetails additionalDetails , @PathVariable String employeeId)
	{
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		AdditionalDetails addiDetailsUpdate= additionalDetailsDao.findByEmployee(employee);
		if(addiDetailsUpdate !=null)
		{
		
		addiDetailsUpdate.setAlternateNumber(additionalDetails.getAlternateNumber());
		addiDetailsUpdate.setWeddingDay(additionalDetails.getWeddingDay());
		addiDetailsUpdate.setPassportNumber(additionalDetails.getPassportNumber());
		addiDetailsUpdate.setPhoto(additionalDetails.getPhoto());
		addiDetailsUpdate.setPhotoDisplay(additionalDetails.getPhotoDisplay());
		additionalDetailsDao.save(addiDetailsUpdate);
		return ResponseEntity.ok(Responses.builder().message("Additional Details Updated Successfully.").build());
		}
		else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("EmployeeID not found").build()); 
		}
		
	}
	
	@GetMapping("getAdditionalDetails")
	 public ResponseEntity<?> AdditionalDetails() {
	 List<AdditionalDetails> additionalDetails=additionalDetailsDao.findAll();
	 if(additionalDetails.size()>0) {
		 HashMap<String,List<AdditionalDetails>> map=new HashMap<String, List<AdditionalDetails>>();
		   map.put("AdditionalDetails", additionalDetails);
		   return ResponseEntity.ok(map);
	 } else 
	 { 
		return ResponseEntity.badRequest().body(Responses.builder().message("Data not Found").build());
	 } 
	 
	 
	}
	
	
	 @GetMapping("/getadditionalDetailsId/{employeeId}") 
	 public ResponseEntity<?> getadditionalDetailsId(@PathVariable String employeeId)
      { 
		 Employee employee = employeeDao.findByEmployeeId(employeeId);
		 AdditionalDetails additionalDetails=additionalDetailsDao.findByEmployee(employee);
	   if(additionalDetails!=null) 
	    {
	   HashMap<String,AdditionalDetails> map=new HashMap<String, AdditionalDetails>();
	   map.put("AdditionalDetails", additionalDetails);
	   return ResponseEntity.ok(map);	 
	    }
	 //return new ResponseEntity<AdditionalDetails>(additionalDetails,HttpStatus.OK);
	   else
		 {
		   //return ResponseEntity.badRequest().body(Responses.builder().message("EmployeeID not found").build());
		   AdditionalDetails additionalDetailsNew = new AdditionalDetails();
		   HashMap<String,AdditionalDetails> map=new HashMap<String, AdditionalDetails>();
		   map.put("AdditionalDetails", additionalDetailsNew);
		   return ResponseEntity.ok(map);	
		  
		  }  
   }
	
	  
	 @DeleteMapping("/deleteAdditionalDetails/{additionalDetailsId}")
		public ResponseEntity<?> deleteAdditionalDetails(@PathVariable int additionalDetailsId)
		{
			AdditionalDetails additionalDetails=additionalDetailsDao.findByAdditionalDetailsId(additionalDetailsId);
			if(additionalDetails!=null)
			{
			additionalDetailsDao.deleteById(additionalDetailsId);
			return ResponseEntity.ok(Responses.builder().message("Additional Details are Deleted successfully").build());
			}
			else {
				 return ResponseEntity.badRequest().body(Responses.builder().message("AdditionalDetails ID not found").build()); 
			}
			
		}

	
}
