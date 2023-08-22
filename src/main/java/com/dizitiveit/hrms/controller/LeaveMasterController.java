package com.dizitiveit.hrms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.LeaveMasterDao;
import com.dizitiveit.hrms.model.LeaveMaster;
import com.dizitiveit.hrms.pojo.LeaveMasterPojo;
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("/leaveMaster")
public class LeaveMasterController {

	@Autowired
	private LeaveMasterDao leaveMasterDao;
	
	
	@PostMapping("/addLeaveMaster")
	public ResponseEntity<?> addLeaveMaster(@RequestBody LeaveMaster leaveMaster)
	{
		if(leaveMasterDao.existsByLeaveType(leaveMaster.getLeaveType()))
		{
			return ResponseEntity.badRequest().body(Responses.builder().message("LeaveType Already Exists").build());
		}
		else {
	
		   leaveMaster.setStatus(true);
		   leaveMasterDao.save(leaveMaster);
		   return ResponseEntity.ok(Responses.builder().message("LeaveMaster Saved  Sucessfully").build());
	}
	}
	
	
	@PostMapping("/updateLeaveMaster/{leaveId}")
	public ResponseEntity<?> updateLeaveMaster(@RequestBody LeaveMaster leaveMaster,@PathVariable long leaveId)
	{
		LeaveMaster leaveMasterUpdate=leaveMasterDao.findByLeaveId(leaveId);
		if(leaveMasterUpdate!=null)
		{
		leaveMasterUpdate.setLeaveType(leaveMaster.getLeaveType());
		leaveMasterUpdate.setCarryForward(leaveMaster.getCarryForward());
		leaveMasterUpdate.setPeriod(leaveMaster.getPeriod());
		leaveMasterUpdate.setCount(leaveMaster.getCount());
		leaveMasterUpdate.setStatus(true);
		leaveMasterDao.save(leaveMasterUpdate);
		return ResponseEntity.ok(Responses.builder().message("LeaveMaster Updated  Successfully").build());
		
	    }
		else
		{
			return ResponseEntity.badRequest().body(Responses.builder().message("Leave Id  Not Found").build());
		}
	
	}	
		
		@GetMapping("leaveMasterDetails")
		public ResponseEntity<?> leaveMasterDetails()
		{
			List<LeaveMaster> leaveMaster=leaveMasterDao.findAll();
			if(leaveMaster.size()>0)
			{
				 HashMap<String,List<LeaveMaster>> map=new HashMap<String, List<LeaveMaster>>();
				   map.put("LeaveMaster", leaveMaster);
				   return ResponseEntity.ok(map);
			}
			 else 
			 { 
				 return ResponseEntity.ok(Responses.builder().message("Not found").build());
			 } 
		 }
		
		
		

		 @GetMapping("/getLeaveTypes")
		  public ResponseEntity<?> getLeaveTypes()
		  {
			  List<String> leaveType= leaveMasterDao.findByLeaveType(true);
			  HashMap<String,List<String>> map=new HashMap<String,List<String>>();
		      map.put("LeaveTypes", leaveType);
		      return ResponseEntity.ok(map);
			  
		  }
		
		
		
		@GetMapping("leaveDetails/{leaveId}")
		public ResponseEntity<?> leaveDetails(@PathVariable long leaveId)
		{
			LeaveMaster leaveMaster=leaveMasterDao.findByLeaveId(leaveId);
			if(leaveMaster!=null)
			{
				HashMap<String,LeaveMaster> map=new HashMap<String, LeaveMaster>();
				   map.put("LeaveMaster", leaveMaster);
				   return ResponseEntity.ok(map);
			}
			
			else
			{
				return ResponseEntity.badRequest().body(Responses.builder().message("Leave Id Not Found").build());
			}
			
		}
	
		
		   @PostMapping("/deactiveStatus/{leaveId}")
			public ResponseEntity<?> deactiveStatus(@PathVariable long leaveId )
			{
			   LeaveMaster leaveMaster=leaveMasterDao.findByLeaveId(leaveId);
				if(leaveMaster != null)
				{
					leaveMaster.setStatus(false);
					leaveMasterDao.save(leaveMaster);
					return ResponseEntity.ok(Responses.builder().message("LeaveMaster Deactivated Successfully").build());
				}
				else {
					 return ResponseEntity.badRequest().body(Responses.builder().message(" Leave Id Not Found ").build());
				}
			}
		  
		    
		    @PostMapping("/activeStatus/{leaveId}")
			public ResponseEntity<?> activeStatus(@PathVariable long leaveId )
			{
		    	 LeaveMaster leaveMaster=leaveMasterDao.findByLeaveId(leaveId);
		    	 if(leaveMaster != null)
					{
						leaveMaster.setStatus(true);
						leaveMasterDao.save(leaveMaster);
						return ResponseEntity.ok(Responses.builder().message("LeaveMaster Activated Successfully").build());
					}
					else {
						 return ResponseEntity.badRequest().body(Responses.builder().message(" Leave Id Not Found ").build());
					}
			}
		    
		    
		    @GetMapping("ActiveListOfStatus")
			  public ResponseEntity<?> ActivelistOfStatus(){
				  List<LeaveMaster> leaveMaster=leaveMasterDao.findBysetStatus(true);
				  List<LeaveMasterPojo> leavePojoList = new ArrayList<LeaveMasterPojo>();
				  for(LeaveMaster leave : leaveMaster)
				  {
					  LeaveMasterPojo leavePojo = new LeaveMasterPojo();
					  leavePojo.setLeaveId(leave.getLeaveId());
					  leavePojo.setLeaveType(leave.getLeaveType());
					  leavePojo.setStatus(leave.isStatus());
					  leavePojoList.add(leavePojo);
				      
				  }
				    HashMap<String,List<LeaveMasterPojo>> map=new HashMap<String, List<LeaveMasterPojo>>();
				    map.put("LeaveMaster", leavePojoList); 
				    return ResponseEntity.ok(map);
				  }
				
		    
		    @GetMapping("DeactiveListOfStatus")
			  public ResponseEntity<?> DeactivelistOfStatus(){
				  List<LeaveMaster> leaveMaster=leaveMasterDao.findBysetStatus(false);
				  List<LeaveMasterPojo> leavePojoList = new ArrayList<LeaveMasterPojo>();
				  for(LeaveMaster leave : leaveMaster)
				  {
					  LeaveMasterPojo leavePojo = new LeaveMasterPojo();
					  leavePojo.setLeaveId(leave.getLeaveId());
					  leavePojo.setLeaveType(leave.getLeaveType());
					  leavePojo.setStatus(leave.isStatus());
					  leavePojoList.add(leavePojo);
				      
				  }
				    HashMap<String,List<LeaveMasterPojo>> map=new HashMap<String, List<LeaveMasterPojo>>();
				    map.put("LeaveMaster", leavePojoList); 
				    return ResponseEntity.ok(map);
				  }
		
	}
	
	
	
	
	
	

