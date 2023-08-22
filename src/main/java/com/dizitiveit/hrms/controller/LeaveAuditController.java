package com.dizitiveit.hrms.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.LeaveEmpEntitlementDao;
import com.dizitiveit.hrms.dao.LeaveMasterDao;
import com.dizitiveit.hrms.dao.LeavesAuditDao;
import com.dizitiveit.hrms.model.Attendance;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.LeaveEmployeeEntitlement;
import com.dizitiveit.hrms.model.LeaveMaster;
import com.dizitiveit.hrms.model.LeavesAudit;
import com.dizitiveit.hrms.pojo.AuditPojo;
import com.dizitiveit.hrms.pojo.LeaveAuditPojo;
import com.dizitiveit.hrms.pojo.Responses;

@RequestMapping("/leaveAudit")
@RestController
public class LeaveAuditController {

	
	@Autowired
	private LeavesAuditDao leavesAuditDao;
	
	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private LeaveMasterDao leaveMasterDao;
	
	
	
	@PostMapping("/addLeaveAudit/{employeeId}/{leaveId}")
	public ResponseEntity<?> addLeaveAudit(@PathVariable String employeeId, @RequestBody LeavesAudit leavesAudit,@PathVariable long leaveId)
	{


		Employee employee=employeeDao.findByEmployeeId(employeeId);
		leavesAudit.setEmployee(employee);
		
		LeaveMaster leaveMaster = leaveMasterDao.findByLeaveId(leaveId);
		leavesAudit.setLeaveMaster(leaveMaster);
	
		
		leavesAudit.setLastCredited(new Date());
		leavesAuditDao.save(leavesAudit);
		return ResponseEntity.ok(Responses.builder().message("LeavesAudit Details Saved Successfully.").build());
		}
	
	
	
	
	@GetMapping("getCurrentLeaveDashboard/{employeeId}")
	  public ResponseEntity<?> getCurrentLeaveDashboard(@PathVariable String employeeId)
	  {
		
		  Employee emp=employeeDao.findByEmployeeId(employeeId);
		  List<LeaveMaster> leaveMaster=leaveMasterDao.findAll();
		  System.out.println(leaveMaster.size());
		  List<AuditPojo> auditList = new ArrayList<AuditPojo>();
		  for(LeaveMaster leavemaster : leaveMaster)
		  {
	    LeavesAudit auditDetails=leavesAuditDao.findByQuarter(emp.getEmplyoeeCode(),leavemaster.getLeaveId());
	    if(auditDetails!=null)
	    {
	           System.out.println("audit details leave master is"+auditDetails.getLeaveMaster().getLeaveId());
			   AuditPojo auditPojo=new AuditPojo();			 
			   //System.out.println("EmployeeCode:" +auditDetails.getEmployee().getEmployeeId());
			   auditPojo.setEmployeeId(auditDetails.getEmployee().getEmployeeId());
               auditPojo.setLeaveId(auditDetails.getLeaveMaster().getLeaveId());
			   auditPojo.setLeaveType(auditDetails.getLeaveMaster().getLeaveType());
			   auditPojo.setLeaveBalance(auditDetails.getLeaveBalance());
			   auditPojo.setLeavesAwaitingApproval(auditDetails.getLeavesAwaitingApproval());
		       auditList.add(auditPojo);
	    }
			  }
			  		  
		  HashMap<String, List<AuditPojo>> map=new HashMap<String,List<AuditPojo>>();
		  map.put("LeaveAudit", auditList);
		  return ResponseEntity.ok(map);
	  }
		 	  

		  //only retrieve the Array of objects based on employeeId 
		  @GetMapping("getLeavesAudit/{employeeId}") 
		  public ResponseEntity<?> getEmpEntitlement(@PathVariable String employeeId) 
		  {
		  Employee employee=employeeDao.findByEmployeeId(employeeId);
	      List<LeavesAudit> leaveEmpEntitlementDetails =leavesAuditDao.findByEmployee(employee.getEmplyoeeCode());
		  
		  List<LeaveAuditPojo> leaveEmpEntitlementList = new ArrayList<LeaveAuditPojo>();
		  
		  for (LeavesAudit leavesAudit :leaveEmpEntitlementDetails) {
		  
			  LeaveAuditPojo leaveAuditPojo = new LeaveAuditPojo();
			  leaveAuditPojo.setEmployeeId(leavesAudit.getEmployee().getEmployeeId());
			  leaveAuditPojo.setLeaveType(leavesAudit.getLeaveMaster().getLeaveType());
			  leaveAuditPojo.setFinancialYear(leavesAudit.getFinancialYear()); 
			  leaveAuditPojo.setOpeningBalance(leavesAudit.getOpeningBalance());
		  DateFormat dateOfJoiningFormat = new SimpleDateFormat("dd-MM-yyyy");
		  leaveAuditPojo.setLastCredited(dateOfJoiningFormat.format(leavesAudit.getLastCredited()));
			
		  leaveAuditPojo.setLeavesCredited(leavesAudit.getLeavesCredited());
		  leaveAuditPojo.setLeavesApproved(leavesAudit.getLeavesApproved());
		  leaveAuditPojo.setLeavesAwaitingApproval(leavesAudit.getLeavesAwaitingApproval());
		  leaveAuditPojo.setLeaveBalance(leavesAudit.getLeaveBalance()); 
		  //leaveEmpEntitlementPojo.setEmployee(leaveEmpEntitlement.getEmployee());
		  leaveEmpEntitlementList.add(leaveAuditPojo); 
		  }
		  
		  HashMap<String, List<LeaveAuditPojo>> map = new HashMap<String,List<LeaveAuditPojo>>(); 
		  map.put("LeavesAudit",leaveEmpEntitlementList); 
		  return ResponseEntity.ok(map);
		  
		  }
		  
		    
		  
     }

		
		  
	
	
	
	
	
	
	

