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

import com.dizitiveit.hrms.dao.RolesDao;
import com.dizitiveit.hrms.model.Designation;
import com.dizitiveit.hrms.model.Roles;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/roles")
public class RolesController {

	@Autowired
	private RolesDao rolesDao;
	
	@PostMapping("/addRoles")
	public ResponseEntity<?> addRole(@RequestBody Roles roles)
	{
		if(rolesDao.existsByRoleName(roles.getRoleName()))
		{
			 return ResponseEntity.badRequest().body(Responses.builder(). message("Role Already Exists").build()); 
		}
		else {
     		rolesDao.save(roles);
		   //return new ResponseEntity<Roles>(roles,HttpStatus.OK);
     		//return ResponseEntity.ok("Saved Successfully.");
     		return ResponseEntity.ok(Responses.builder().message("Role Details Saved Successfully.").build());
		}
	}
	
	@PostMapping("/updateRoles/{roleName}")
	public ResponseEntity<?> updateRoles(@RequestBody Roles roles, @PathVariable String roleName)
	{
		Roles rolesUpdate=rolesDao.findByRoleName(roleName);
		if(rolesUpdate !=null)
		{
		rolesUpdate.setRoleName(roles.getRoleName());
		rolesUpdate.setDescription(roles.getDescription());
		//rolesUpdate.setStatus(roles.getStatus());
		rolesUpdate.setStatus(roles.isStatus());
		//rolesUpdate.setStatus(false);
		rolesDao.save(rolesUpdate);
		//return ResponseEntity.ok(rolesUpdate);
		//return ResponseEntity.ok("Updated Successfully.");
		return ResponseEntity.ok(Responses.builder().message("Role Details Updated Successfully.").build());
	    } 
		else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("Role Name already exist").build()); 
		}
	}	

	
	  @GetMapping("getRolesDetails")
	  public ResponseEntity<?> getroles()
	  {
		List<Roles> role=rolesDao.findAll();
		if(role.size()>0)
		{
			HashMap<String,List<Roles>> map=new HashMap<String, List<Roles>>();
			   map.put("Roles", role);
			   return ResponseEntity.ok(map);	
		}
		else
		{
			 return ResponseEntity.badRequest().body(Responses.builder(). message("Data not found").build()); 
    	}
      }
	 
	  
	  @GetMapping("/getRolesName/{roleName}") 
		 public ResponseEntity<?> getRolesId(@PathVariable String roleName)
	      { 
		   Roles roles=rolesDao.findByRoleName(roleName);
		   if(roles!=null) 
		 {
		   HashMap<String,Roles> map=new HashMap<String, Roles>();
		   map.put("Roles", roles);
		   return ResponseEntity.ok(map);	 
		 }
		 //return new ResponseEntity<Roles>(roles,HttpStatus.OK);
		   else 
			 { 
			   return ResponseEntity.badRequest().body(Responses.builder(). message("Data not found with Rolename " + roleName).build()); 
			  }
	   }
	  
	  @GetMapping("/getRoleNames")
	  public ResponseEntity<?> getBranchNames()
	  {
		  List<String> role= rolesDao.findByroleName();
		  HashMap<String,List<String>> map=new HashMap<String,List<String>>();
	      map.put("roleNames", role);
	      return ResponseEntity.ok(map);
		  
	  }
	  
	  @DeleteMapping("/deleteRoles/{roleId}")
		public ResponseEntity<?> deleteRoles(@PathVariable long roleId)
		{
			if(rolesDao.existsByRoleId(roleId))
			{
			 rolesDao.deleteById(roleId);
			 return ResponseEntity.ok(Responses.builder().message("Role Details Deleted Sucessfully"+ roleId).build());
			}
			else {
			   return ResponseEntity.badRequest().body(Responses.builder(). message(" Not found roleId " + roleId).build()); 
			}
			
		}			
	}


