package com.dizitiveit.hrms.controller;

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

import com.dizitiveit.hrms.dao.BankDetailsDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.BankDetails;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/empBankDetails")
public class BankDetailsController {

	@Autowired
	private BankDetailsDao bankDetailsDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@PostMapping("/addEmpBankDetails/{employeeId}")
	public ResponseEntity<?> addEmpBankDetails(@RequestBody BankDetails bankDetails, @PathVariable String employeeId)
	{
		Employee emp= employeeDao.findByEmployeeId(employeeId);
		 if(bankDetailsDao.existsByEmployee(emp))
		 {
			 return ResponseEntity.badRequest().body(Responses.builder().message("Employee Bank Details Already Exists").build()); 
		 }
		 else 
			{ 
			 bankDetails.setEmployee(emp);
			 bankDetailsDao.save(bankDetails);
			return ResponseEntity.ok(Responses.builder().message("Employee Bank Details are saved sucessfully").build());
			}
		}		
	
	@PostMapping("/updateEmpBankDetails/{employeeId}")
	public ResponseEntity<?> updateEmpBankDetails(@RequestBody BankDetails bankDetails, @PathVariable String employeeId)
	{
		Employee emp= employeeDao.findByEmployeeId(employeeId);
		BankDetails empDetailsUpdate= bankDetailsDao.findByEmployee(emp);
		if(emp!=null)
		{
			empDetailsUpdate.setAccountHolderName(bankDetails.getAccountHolderName());
			empDetailsUpdate.setIfscCode(bankDetails.getIfscCode());
			empDetailsUpdate.setAccountNumber(bankDetails.getAccountNumber());
			empDetailsUpdate.setBankName(bankDetails.getBankName());
			empDetailsUpdate.setBranchName(bankDetails.getBranchName());
			empDetailsUpdate.setOwnedBy(bankDetails.getOwnedBy());
			empDetailsUpdate.setStatus(bankDetails.getStatus());
			empDetailsUpdate.setLastModifiedDate(bankDetails.getLastModifiedDate());
			bankDetailsDao.save(empDetailsUpdate);
			return ResponseEntity.ok(Responses.builder().message("Bank Details Updated Successfully.").build());
	    }
		else 
	      {
		 return ResponseEntity.badRequest().body(Responses.builder().message("Employee Id not found").build()); 
	      }		
	  }
	
	
	@GetMapping("getEmpBankDetails")
	 public ResponseEntity<?> EmployeeBankDetails() {
	 List<BankDetails> empBankDetails=bankDetailsDao.findAll();
	 if(empBankDetails.size()>0) {
		 HashMap<String,List<BankDetails>> map=new HashMap<String, List<BankDetails>>();
		   map.put("EmpBankDetails", empBankDetails);
		   return ResponseEntity.ok(map);
	 } else 
	 { 
		 //return ResponseEntity.ok("Data not found."); 
		 return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build()); 
	  } 
	 }
	
	
	 @GetMapping("/getEmpBankDetailsId/{employeeId}") 
	 public ResponseEntity<?> getEmpBankDetailsId(@PathVariable String employeeId)
      { 
		 Employee employee = employeeDao.findByEmployeeId(employeeId);
		 BankDetails empBankDetails=bankDetailsDao.findByEmployee(employee);
	   if(empBankDetails!=null) 
	 {
	   HashMap<String,BankDetails> map=new HashMap<String, BankDetails>();
	   map.put("EmployeeBankDetails", empBankDetails);
	   return ResponseEntity.ok(map);	 
	 }
	 //return new ResponseEntity<AdditionalDetails>(additionalDetails,HttpStatus.OK);
	   else 
		 { 
		   return ResponseEntity.badRequest().body(Responses.builder().message("ID not found").build());
		  } 
   }
	
	 
	 @DeleteMapping("/deleteEmpBankDetails/{bankId}")
		public ResponseEntity<?> deleteAdditionalDetails(@PathVariable long bankId)
		{
			if(bankDetailsDao.existsBybankId(bankId))
			{
				bankDetailsDao.deleteById(bankId);
			//return ResponseEntity.ok("Additional Details Deleted successfully.");
			return ResponseEntity.ok(Responses.builder().message("Bank Details Deleted Successfully.").build());
			}
			else
			{
			return ResponseEntity.badRequest().body(Responses.builder().message("ID not found").build());
			}	
	}
}
