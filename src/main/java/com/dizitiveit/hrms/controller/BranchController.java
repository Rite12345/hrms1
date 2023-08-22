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

import com.dizitiveit.hrms.dao.BranchDao;
import com.dizitiveit.hrms.model.Branch;
import com.dizitiveit.hrms.pojo.BranchPojo;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/branch")
public class BranchController {

	@Autowired
	private BranchDao branchDao;
	
	@PostMapping("/addBranch")
	public ResponseEntity<?> addBranch(@RequestBody Branch branch)
	{
		if(branchDao.existsByBranchName(branch.getBranchName()))
		{
			return ResponseEntity.badRequest().body(Responses.builder().message("Branch Already Exists").build());
		}
		else {
		branchDao.save(branch);
		//return new ResponseEntity<Branch>(branch,HttpStatus.OK);	
		//return ResponseEntity.ok("Saved Successfully.");
		return ResponseEntity.ok(Responses.builder().message("Branch Details Saved Successfully.").build());
		}
		
	}
	
	@PostMapping("/updateBranch/{branchName}")
	public ResponseEntity<?> updateDept(@RequestBody Branch branch , @PathVariable String branchName)
	{
		Branch branchUpdate=branchDao.findBybranchName(branchName);
		if(branchUpdate!=null)
		{
		branchUpdate.setBranchName(branch.getBranchName());
		branchUpdate.setCity(branch.getCity());
		branchUpdate.setState(branch.getState());
		branchUpdate.setCountry(branch.getCountry());
		branchUpdate.setPhoneNumberOne(branch.getPhoneNumberOne());
		branchUpdate.setPhoneNumberTwo(branch.getPhoneNumberTwo());
		branchUpdate.setEmailId(branch.getEmailId());
		branchUpdate.setBranchPremisesType(branch.getBranchPremisesType());
		branchUpdate.setBranchPremisesRent(branch.getBranchPremisesRent());
		branchUpdate.setLastModifiedDate(branch.getLastModifiedDate());
		branchUpdate.setStatus(branch.getStatus());
		branchDao.save(branchUpdate);
		//return ResponseEntity.ok(branchUpdate);
		//return ResponseEntity.ok("Updated Successfully.");
		return ResponseEntity.ok(Responses.builder().message("Branch Details are Updated Successfully.").build());
		}
		else {
			return ResponseEntity.badRequest().body(Responses.builder().message("Branch Name already exists").build());
		}
	}
	
	 @GetMapping("getBranchDetails")
	 public ResponseEntity<?> Branch() {
		 List<Branch> branch=branchDao.findAll();
		 if(branch.size()>0) {
			 HashMap<String,List<Branch>> map=new HashMap<String, List<Branch>>();
		      map.put("Branch", branch);
		      return ResponseEntity.ok(map);
		 } else 
		 { 
			 return ResponseEntity.ok("Data not found."); 
		  } 
		}
	 
	 @GetMapping("/getBranchName/{branchName}") 
	 public ResponseEntity<?> getBranchId(@PathVariable String branchName)
      { 
	   Branch branch=branchDao.findBybranchName(branchName);
	   if(branch!=null) 
	    {
	      HashMap<String,Branch> map=new HashMap<String, Branch>();
	      map.put("Branch", branch);
	      return ResponseEntity.ok(map);	 
	    }
	 //return new ResponseEntity<Branch>(branch,HttpStatus.OK);
	   else 
		 { 
		   return ResponseEntity.badRequest().body(Responses.builder().message("Data Not Found with branchName").build());
		  }
   }
	
	 @GetMapping("/getBranchNames")
	  public ResponseEntity<?> getBranchNames()
	  {
		  List<String> branch= branchDao.findBybranchName();
		  HashMap<String,List<String>> map=new HashMap<String,List<String>>();
	      map.put("branchNames", branch);
	      return ResponseEntity.ok(map);
		  
	  }

		@DeleteMapping("/deleteBranch/{branchId}")
		public ResponseEntity<?> deleteBranch(@PathVariable int branchId)
		{
			if(branchDao.existsByBranchId(branchId))
			{
			branchDao.deleteById(branchId);
			return ResponseEntity.ok(Responses.builder().message("Branch Details are Deleted Successfully.").build());
			}
			else {
				 return ResponseEntity.badRequest().body(Responses.builder(). message(" Not found branchId " + branchId).build());
			}
			
		}
		
		

		 @PostMapping("/deactiveBranch/{branchId}")
			public ResponseEntity<?> deactiveBranch(@PathVariable int branchId )
			{
				Branch branch= branchDao.findBybranchId(branchId);
				if(branch != null)
				{
					branch.setStatus(false);
					branchDao.save(branch);
					return ResponseEntity.ok(Responses.builder().message("Branch Details deactivated Sucessfully").build());
				}
				else {
					 return ResponseEntity.badRequest().body(Responses.builder().message("Branch Id "+ " " +branch+ " "+ " Not Found ").build());
				}
			}
		 
		 
		 @GetMapping("ActiveListBranchNames")
		  public ResponseEntity<?> ActiveListBranchNames(){
			  List<Branch> branch=branchDao.findBysetStatus(true);
			  List<BranchPojo> branPojo = new ArrayList<BranchPojo>();
			  for(Branch bran : branch)
			  {
				  BranchPojo branchPojo = new BranchPojo();
				  branchPojo.setBranchId(bran.getBranchId());
				  branchPojo.setBranchName(bran.getBranchName());
				  branchPojo.setCity(bran.getCity());
				  branchPojo.setStatus(bran.getStatus());
				  branPojo.add(branchPojo);
			  }
				
			    HashMap<String,List<BranchPojo>> map=new HashMap<String, List<BranchPojo>>();
			    map.put("Branch", branPojo); 
			    return ResponseEntity.ok(map);
			  }
		
		
		
		
		
		
		
}
