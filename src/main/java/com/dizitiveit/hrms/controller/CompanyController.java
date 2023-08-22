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

import com.dizitiveit.hrms.dao.CompanyDao;
import com.dizitiveit.hrms.model.Company;
import com.dizitiveit.hrms.pojo.CompanyPojo;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyDao companyDao;
	
	@PostMapping("/addCompany")
	public ResponseEntity<?> addCompany(@RequestBody Company company)
	{

		if(companyDao.existsByCompanyName(company.getCompanyName()))
		{
			return ResponseEntity.badRequest().body(Responses.builder().message("Company Already Exists").build());
		}
		else {
	
		companyDao.save(company);
		//return new ResponseEntity<Company>(company,HttpStatus.OK);
		//return ResponseEntity.ok("Saved Successfully.");
		return ResponseEntity.ok(Responses.builder().message("Company Details Saved Successfully.").build());
		}
	}
	
	@PostMapping("/updateCompany/{companyId}")
	public ResponseEntity<?> updateCompany(@RequestBody Company company, @PathVariable int companyId)
	{
		Company companyUpdate=companyDao.findBycompanyId(companyId);
		if(companyUpdate!=null)
		{
		companyUpdate.setCompanyName(company.getCompanyName());
		companyUpdate.setRegistrationNumber(company.getRegistrationNumber());
		companyUpdate.setGstNumber(company.getGstNumber());
		companyUpdate.setPanNumber(company.getPanNumber());
		companyUpdate.setEsiNumber(company.getEsiNumber());
		companyUpdate.setPfNumber(company.getPfNumber());
		companyUpdate.setTinNumber(company.getTinNumber());
		companyUpdate.setNumberOfBranches(company.getNumberOfBranches());
		companyUpdate.setWorkingHours(company.getWorkingHours());
		companyUpdate.setWorkingDays(company.getWorkingDays());
		companyUpdate.setStatus(company.getStatus());
		companyUpdate.setLastModifiedDate(company.getLastModifiedDate());
		companyDao.save(companyUpdate);
		//return ResponseEntity.ok("Updated Successfully.");	
		return ResponseEntity.ok(Responses.builder().message("Company Details are Updated Successfully.").build());
		}
		else {
			return ResponseEntity.badRequest().body(Responses.builder().message("Company Id doesnt exists").build());
		}
		
	}
	
	 @GetMapping("getCompanyDetails")
	 public ResponseEntity<?> Company() {
		 List<Company> company=companyDao.findAll();
		 if(company.size()>0) {
			 HashMap<String,List<Company>> map=new HashMap<String,List<Company>>();
			   map.put("Company", company);
			   return ResponseEntity.ok(map); 
		 } else 
		 { 
			return ResponseEntity.badRequest().body(Responses.builder().message("Data not Found").build());
		  } 
		}
	 
	 @GetMapping("/getCompanyId/{companyId}") 
	 public ResponseEntity<?> getCompanyId(@PathVariable int companyId)
      { 
	  Company company=companyDao.findBycompanyId(companyId);
	   if(company!=null) 
	 {
	   HashMap<String,Company> map=new HashMap<String, Company>();
	   map.put("Company", company);
	   return ResponseEntity.ok(map);	 
	 }
	 //return new ResponseEntity<Company>(company,HttpStatus.OK);	
	   else 
		 { 
			return ResponseEntity.badRequest().body(Responses.builder().message("Company Id doesnt exists").build());
		  }
    }
	
	 
	 @DeleteMapping("/deleteCompany/{companyId}")
		public ResponseEntity<?> deleteCompany(@PathVariable int companyId)
		{
		 if(companyDao.existsByCompanyId(companyId))
		 {
			companyDao.deleteById(companyId);
			return ResponseEntity.ok(Responses.builder().message("Company Details are deleted Successfully.").build());
		 }
		 else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("Company Id doesn't Exists").build());
		 }
		} 
	 
	 
	 @PostMapping("/deactive/{companyId}")
		public ResponseEntity<?> deactiveAllowances(@PathVariable int companyId )
		{
			Company company = companyDao.findBycompanyId(companyId);
			if(company != null)
			{
				company.setStatus(false);
				companyDao.save(company);
				return ResponseEntity.ok(Responses.builder().message("Company Details deactivated Sucessfully").build());
			}
			else {
				 return ResponseEntity.badRequest().body(Responses.builder().message("Company Id "+ " " +company+ " "+ " Not Found ").build());
			}
		}
	 
	 
	 @GetMapping("ActiveListNames")
	  public ResponseEntity<?> ActiveListNames(){
		  List<Company> company=companyDao.findBysetStatus(true);
		  List<CompanyPojo> comPojo = new ArrayList<CompanyPojo>();
		  for(Company com : company)
		  {
			  CompanyPojo companyPojo = new CompanyPojo();
			  companyPojo.setCompanyId(com.getCompanyId());
			  companyPojo.setCompanyName(com.getCompanyName());
			  companyPojo.setStatus(com.getStatus());
			  comPojo.add(companyPojo);
		  }
		    HashMap<String,List<CompanyPojo>> map=new HashMap<String, List<CompanyPojo>>();
		    map.put("Company", comPojo); 
		    return ResponseEntity.ok(map);
		    
		  }
  }
