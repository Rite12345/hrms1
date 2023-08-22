package com.dizitiveit.hrms.controller;

import java.util.HashMap;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
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
import com.dizitiveit.hrms.dao.DeptDao;
import com.dizitiveit.hrms.model.Branch;
import com.dizitiveit.hrms.model.Department;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	private DeptDao deptDao;
	
	@Autowired
	private BranchDao branchDao;
	
	
	@PostMapping("/addDept/{branchName}")
	public ResponseEntity<?> addDept(@RequestBody Department department, @PathVariable String branchName)
	{
		if(deptDao.existsByDeptName(department.getDeptName()))
		{
			return ResponseEntity.badRequest().body(Responses.builder().message("Department Already Exists").build());
		}
		else
		{
		Branch branch1=branchDao.findBybranchName(branchName);
		 department.setBranch(branch1);	
		 System.out.println(department.toString());
		 deptDao.save(department);
		//return new ResponseEntity<Department>(department,HttpStatus.OK);	
		//return ResponseEntity.ok("Saved Successfully.");
		return ResponseEntity.ok(Responses.builder().message("Department Details are Saved Successfully.").build());
		}
		
	}
	
	@PostMapping("/updateDept/{deptId}")
	public ResponseEntity<?> updateDept(@RequestBody Department department ,@PathVariable int deptId)
	{
		
		Department deptUpdate=deptDao.findBydeptId(deptId);
		if(deptUpdate!=null)
		{
		deptUpdate.setDeptName(department.getDeptName());
		deptDao.save(deptUpdate);
		//return ResponseEntity.ok(deptUpdate);
		//return ResponseEntity.ok("Updated Successfully.");
		return ResponseEntity.badRequest().body(Responses.builder().message("Department Details are Updated Sucessfully").build());
		}
		else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("DepartmentId not Found").build());
		}
		
		
	}
	
	 @GetMapping("getDeptDetails")
	 public ResponseEntity<?> Department() {
		 List<Department> department=deptDao.findAll();
		 if(department.size()>0) {
			 HashMap<String, List<Department>> map=new HashMap<String,  List<Department>>();
		      map.put("Department", department);
		      return ResponseEntity.ok(map);
		 } else 
		 { 
			 return ResponseEntity.badRequest().body(Responses.builder().message("Data not Found").build());
		  } 
		 }
	
	  @GetMapping("/getDeptId/{deptId}")
	  public ResponseEntity<?> getDeptId(@PathVariable int deptId)
      { 
	     Department dept=deptDao.findBydeptId(deptId);
	     if(dept!=null) 
	     {
	      HashMap<String,Department> map=new HashMap<String, Department>();
	      map.put("Department", dept);
	      return ResponseEntity.ok(map);	 
	     }
	     //return new ResponseEntity<Department>(dept,HttpStatus.OK);
	     else 
		 { 
	    	 return ResponseEntity.badRequest().body(Responses.builder().message("DeptId not Found").build());
		  }
       }
	 
	 
	 @GetMapping("/getDeptNames")
	  public ResponseEntity<?> getDeptNames()
	  {
		  List<String> dept= deptDao.findBydeptName();
		  HashMap<String,List<String>> map=new HashMap<String,List<String>>();
	      map.put("departmentNames", dept);
	      return ResponseEntity.ok(map);
		  
	  }
	 
		@DeleteMapping("/deleteDept/{deptId}")
		public ResponseEntity<?> deleteDept(@PathVariable int deptId)
		{
			Department dept=deptDao.findBydeptId(deptId);
		     if(dept!=null) 
		     {
			deptDao.deleteById(deptId);
			return ResponseEntity.ok(Responses.builder().message("Department Details are deleted Sucessfully").build());
		     }
		     else 
			 { 
		    	 return ResponseEntity.badRequest().body(Responses.builder().message("DepartmentId not Found").build());
			  }
		}
	
}
