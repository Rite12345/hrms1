package com.dizitiveit.hrms.controller;

import java.text.DateFormat;
import java.time.ZoneId;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.LeaveEmpEntitlementDao;
import com.dizitiveit.hrms.dao.LeaveMasterDao;
import com.dizitiveit.hrms.dao.LeaveRequestDao;
import com.dizitiveit.hrms.dao.LeavesAuditDao;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.LeaveEmployeeEntitlement;
import com.dizitiveit.hrms.model.LeaveMaster;
import com.dizitiveit.hrms.model.LeaveRequest;
import com.dizitiveit.hrms.model.LeavesAudit;
import com.dizitiveit.hrms.pojo.LeaveRequestPojo;
import com.dizitiveit.hrms.pojo.LeaveRequestViewPojo;
import com.dizitiveit.hrms.pojo.Responses;
import com.dizitiveit.hrms.service.EmailServiceImpl;


@RequestMapping("/leave")
@RestController
public class LeaveRequestController 
{

	@Autowired
	private LeaveRequestDao leaveDao;
	
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	
	@Autowired 
	private LeaveEmpEntitlementDao empEntitlementDao;
	 
	
	@Autowired
	private LeavesAuditDao leaveAuditDao;
	
	
	@Autowired
	private LeaveMasterDao leaveMasterDao;
	 
	

	 @PostMapping("/addLeaveDetails/{employeeId}/{leaveId}")
     public ResponseEntity<?> addLeaveDetails(@RequestBody LeaveRequest leave, @PathVariable String employeeId,@PathVariable long leaveId)
	{
		Employee employee=employeeDao.findByEmployeeId(employeeId);
		LeaveMaster leaveMaster=leaveMasterDao.findByLeaveId(leaveId);
		LeavesAudit leaveAudit=leaveAuditDao.findByemployee(employee.getEmplyoeeCode(),leaveMaster.getLeaveId());
		
		LocalDate startLocalDate = leave.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		  
		  LocalDate endLocalDate = leave.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		  
		  long days =ChronoUnit.DAYS.between(startLocalDate,endLocalDate);
		 
		 if(leaveAudit.getLeaveBalance()== 0 || leaveAudit.getLeaveBalance()<=days)
		  {
			
			return ResponseEntity.badRequest().body(Responses.builder().message("Your leave balance is insufficient balance to apply leave").build());
			
		  }
		 else
		 {
			 Instant instStartDate = leave.getStartDate().toInstant();
	 		 LocalDate localDateStart = instStartDate.atZone(ZoneId.systemDefault()).toLocalDate();
			  Instant dayInstStart = localDateStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
			   Date dayStart = Date.from(dayInstStart);
			   System.out.println(dayStart);
	 		
			   Instant insteNDDate = leave.getEndDate().toInstant();
		 		 LocalDate localDateEnd = insteNDDate.atZone(ZoneId.systemDefault()).toLocalDate();
				  Instant dayInstEnd = localDateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant();
				   Date dayEnd = Date.from(dayInstEnd);
				   System.out.println(dayEnd);
			      
	 		// LeaveRequest leaveRequestStart=leaveDao.getByStartAndEnd(dayStart,dayEnd,employee.getEmplyoeeCode());
	 		 LeaveRequest leaveRequestStart=leaveDao.getByStart(dayStart,"PENDING",employee.getEmplyoeeCode());
			  LeaveRequest leaveRequestEnd=leaveDao.getByStart(dayEnd,"PENDING",employee.getEmplyoeeCode());
			  LeaveRequest leaveRequestStartAprov=leaveDao.getByStart(dayStart,"APPROVED",employee.getEmplyoeeCode());
			  LeaveRequest leaveRequestEndAprov=leaveDao.getByStart(dayEnd,"APPROVED",employee.getEmplyoeeCode());
	 		System.out.println(leaveRequestStart);
	 		System.out.println(leaveRequestEnd);
	 		 if(leaveRequestStart==null && leaveRequestEnd==null && leaveRequestStartAprov==null && leaveRequestEndAprov==null )
	 		 {
		
		System.out.println(leaveMaster.getLeaveId());
		leave.setEmployee(employee);
		leave.setSupervisor(employee.getSupervisor());
		leave.setCreatedAt(new Date());
		leave.setStatus("PENDING");
		
		SimpleDateFormat dateformaterStart = new SimpleDateFormat("dd-MM-yyyy");
		String startDate = dateformaterStart.format(leave.getStartDate());
		
		SimpleDateFormat dateformaterEnd = new SimpleDateFormat("dd-MM-yyyy");   
		String endDate = dateformaterEnd.format(leave.getEndDate());
		
		leaveDao.save(leave);
		
		if(leaveAudit!=null) 
		{
			int leaveEmp= leaveAudit.getLeavesAwaitingApproval();
			 int leaveBalance=leaveAudit.getLeaveBalance();
			 System.out.println("leave audit leave balance"+leaveBalance);
			 if(days==0)
			  {
				 long leaveBalanceForSingle=leaveBalance-1;
				 System.out.println(leaveBalanceForSingle); 
	 			 int leaveBal=(int)leaveBalanceForSingle;
	 			 leaveAudit.setLeaveBalance(leaveBal);	
	 			  
	 			 int leaveEmpTotal= leaveEmp+1;
	 			 System.out.println(leaveEmpTotal);
	 			 leaveAudit.setLeavesAwaitingApproval(leaveEmpTotal);
	 			 leaveAuditDao.save(leaveAudit);
			  }
			 
				 
				 else
				 {
					 long totalDays= days+1;
					 System.out.println("total days"+totalDays);
	                 long leaveBalanceTotal=leaveBalance-totalDays;
	                 System.out.println("leave balance"+leaveBalanceTotal);
	                 int leaveBal=(int)leaveBalanceTotal;
				     leaveAudit.setLeaveBalance(leaveBal);
				  
				    long leaveEmpTotal= leaveEmp+totalDays;
				  //System.out.println(leaveEmpTotal);
				    int leaveAwaiting=(int)leaveEmpTotal;
				    System.out.println("leave awaiting"+leaveAwaiting);
				    leaveAudit.setLeavesAwaitingApproval(leaveAwaiting);
				    leaveAuditDao.save(leaveAudit);
				 }
			
					LeaveEmployeeEntitlement leaveEmpEntitlement=empEntitlementDao.findByLeaveAudit(leaveAudit.getLeaveAuditId());
					if(leaveEmpEntitlement!=null) 
					{
				
						  leaveEmpEntitlement.setLeavesAwaitingApproval(leaveAudit.getLeavesAwaitingApproval());
			
						  leaveEmpEntitlement.setLeaveBalance(leaveAudit.getLeaveBalance());	
						  
						  empEntitlementDao.save(leaveEmpEntitlement);
					  }	   
			    }
			 			try {
			 				emailServiceImpl.sendSimpleMessage(employee.getSupervisor().getOfficialEmailId(), " Leave Request Mail " , " Hi " + employee.getSupervisor().getFirstName() + " , I am " + employee.getFirstName() + " " + employee.getLastName() + "(" + " empId is: " + employee.getEmployeeId() + ")" + " I am Requesting leave is " + leave.getLeaveName() + " from " + startDate + " to "
									 + endDate + " due to " + leave.getShortForm() + " . " + " Please grant a permission. " + " Thank you. ");
			 			}
			 			catch (Exception e) {
							System.out.println("FAIL TO SEND EMAIL: "+e.getMessage());
						}
			 			
			 
				  return ResponseEntity.ok(Responses.builder().message("Leave Applied Successfully.").build());
				}
		 		 
		 		else 
		 		{
		 			return ResponseEntity.badRequest().body(Responses.builder().message("You have already applied leave for this dates").build());
		 		 }
			  }	  
	}
		 
		 
	 
	  
     @PostMapping("/addLeaveManager/{employeeId}/{leaveId}")
     public ResponseEntity<?> addLeaveManager(@RequestBody LeaveRequest leaves, @PathVariable String employeeId,@PathVariable long leaveId)
 	{
 		Employee employee=employeeDao.findByEmployeeId(employeeId);
 		LeaveMaster leaveMaster=leaveMasterDao.findByLeaveId(leaveId);
		LeavesAudit leaveAudit=leaveAuditDao.findByemployee(employee.getEmplyoeeCode(),leaveMaster.getLeaveId());
		LocalDate startLocalDate = leaves.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		  
		  LocalDate endLocalDate = leaves.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		  
		  long days =ChronoUnit.DAYS.between(startLocalDate,endLocalDate);
		 
		  System.out.println("no. of days"+days);
		 if(leaveAudit.getLeaveBalance()== 0 || leaveAudit.getLeaveBalance()<=days)
		  {
			
			return ResponseEntity.badRequest().body(Responses.builder().message("Your leave balance is insufficient balance to apply leave").build());
			
		  }
		 else
		 {
			 Instant instStartDate = leaves.getStartDate().toInstant();
	 		 LocalDate localDateStart = instStartDate.atZone(ZoneId.systemDefault()).toLocalDate();
			 Instant dayInstStart = localDateStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
			 Date dayStart = Date.from(dayInstStart);
			 System.out.println(dayStart);
	 		
			   
			  Instant insteNDDate = leaves.getEndDate().toInstant();
		 	  LocalDate localDateEnd = insteNDDate.atZone(ZoneId.systemDefault()).toLocalDate();
			  Instant dayInstEnd = localDateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant();
			  Date dayEnd = Date.from(dayInstEnd);
			  System.out.println(dayEnd);
			   
	 		 
			   LeaveRequest leaveRequestStart=leaveDao.getByStart(dayStart,"PENDING",employee.getEmplyoeeCode());
			   LeaveRequest leaveRequestEnd=leaveDao.getByStart(dayEnd,"PENDING",employee.getEmplyoeeCode());
			   LeaveRequest leaveRequestStartAprov=leaveDao.getByStart(dayStart,"APPROVED",employee.getEmplyoeeCode());
			   LeaveRequest leaveRequestEndAprov=leaveDao.getByStart(dayEnd,"APPROVED",employee.getEmplyoeeCode());
				 
			   if(leaveRequestStart==null && leaveRequestEnd==null && leaveRequestStartAprov==null && leaveRequestEndAprov==null )
	 		    
			   {
			 
		               System.out.println(leaveMaster.getLeaveId());
		               leaves.setEmployee(employee);
 		               leaves.setSupervisor(employee.getSupervisor());
 		               leaves.setCreatedAt(new Date());
 		               leaves.setStatus("APPROVED");
 		               leaveDao.save(leaves);
 		
 		       if(leaveAudit!=null) 
 		        {

 		           int leaveBalance=leaveAudit.getLeaveBalance();
			       int leavesApproved=leaveAudit.getLeavesApproved();
 			   if(days==0)
			     {
 			 	
 				  long leaveBalanceForSingle=leaveBalance-1;
				  System.out.println(leaveBalanceForSingle); 
	 			  int leaveBal=(int)leaveBalanceForSingle;
	 			  leaveAudit.setLeaveBalance(leaveBal);	
	 			  
	 			  int leaveApproveTotal=leavesApproved+1;
				  System.out.println(leaveApproveTotal);
				  leaveAudit.setLeavesApproved(leaveApproveTotal);
				  leaveAuditDao.save(leaveAudit);
				  
			  }
 		 
		  else
		  {
			     long totalDays= days+1;
				 System.out.println("total days"+totalDays);
                 long leaveBalanceTotal=leaveBalance-totalDays;
                 System.out.println("leave balance"+leaveBalanceTotal);
                 int leaveBal=(int)leaveBalanceTotal;
			     leaveAudit.setLeaveBalance(leaveBal);
			  
			     long leaveEmpTotal= leavesApproved+totalDays;
			     //System.out.println(leaveEmpTotal);
			    int leaveApproved=(int)leaveEmpTotal;
			    System.out.println("leave approved"+leaveApproved);
			    leaveAudit.setLeavesApproved(leaveApproved);
		        leaveAuditDao.save(leaveAudit);
		        
			 }
		  
		  }
 		       
 		LeaveEmployeeEntitlement leaveEmpEntitlement=empEntitlementDao.findByLeaveAudit(leaveAudit.getLeaveAuditId());
		if(leaveEmpEntitlement!=null) 
		{
	
			  leaveEmpEntitlement.setLeavesApproved(leaveAudit.getLeavesApproved());

			  leaveEmpEntitlement.setLeaveBalance(leaveAudit.getLeaveBalance());	
			  
			  empEntitlementDao.save(leaveEmpEntitlement);
		
		  }
			  emailServiceImpl.sendSimpleMessage(employee.getOfficialEmailId(), " Leave Request Mail "," Hi " + employee.getFirstName() + " " + employee.getLastName() + " , Your " + leaves.getLeaveName() + " has been " + leaves.getStatus() + " Successfully. ");
		 		 
 			  return ResponseEntity.ok(Responses.builder().message("Leave Request Sent Successfully.").build());	
 			  
		  }
		 
	 		 
	 		else {
	 			return ResponseEntity.badRequest().body(Responses.builder().message("You have already approved leave for this dates").build());
	 		 }
		 }
		 
 	}
 
	      
	@PostMapping("/updateLeaveDetails/{leaveRequestId}")
	public ResponseEntity<?> updateLeaveDetails(@RequestBody LeaveRequest leave, @PathVariable long leaveRequestId)
	{
		
		LeaveRequest leaveUpdate=leaveDao.findByLeaveRequestId(leaveRequestId);
		if(leaveUpdate!=null)
		{
		leaveUpdate.setLeaveName(leave.getLeaveName());
		leaveUpdate.setStartDate(leave.getStartDate());
		leaveUpdate.setEndDate(leave.getEndDate());
		leaveUpdate.setShortForm(leave.getShortForm());
		leaveUpdate.setLastModifyDate(new Date());
		leaveUpdate.setStatus(leave.getStatus());
		  System.out.println(leaveUpdate.getStartDate());
		leaveDao.save(leaveUpdate);
		
		return ResponseEntity.ok(Responses.builder().message("Leave Details updated successfully").build());
		}
		else {
			return ResponseEntity.badRequest().body(Responses.builder().message("LeaveRequest Id not found").build());
		}
	}
	
	
	@GetMapping("/getLeaveDetails")
	public ResponseEntity<?> Leave()
	{
		List<LeaveRequest> leaveDetails=leaveDao.findAll();
		if(leaveDetails.size()>0)
		{
			HashMap<String, List<LeaveRequest>> map=new HashMap<String,List<LeaveRequest>>();
			map.put("Leave", leaveDetails);
			return ResponseEntity.ok(map);
		}
		else
		{
		return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build());
		}
	}
	
	
	@GetMapping("/getLeave/{employeeId}")
	public ResponseEntity<?> getLeave(@PathVariable String employeeId)
	{
		Employee emp=employeeDao.findByEmployeeId(employeeId);
		List<LeaveRequest> leaveList=leaveDao.findbyEmployee(emp);
		List<LeaveRequestPojo> leavePojoList=new ArrayList<LeaveRequestPojo>();
		for(LeaveRequest leave:leaveList)
		{
			LeaveRequestPojo leavePojo=new LeaveRequestPojo();
			leavePojo.setEmployeeId(leave.getEmployee().getEmployeeId());
			leavePojo.setEmpName(leave.getEmployee().getFirstName());
			leavePojo.setSupervisorId(leave.getSupervisor().getEmployeeId());
			leavePojo.setSupervisorName(leave.getSupervisor().getFirstName());
			leavePojo.setComment(leave.getComment());
			leavePojo.setLeaveRequestId(leave.getLeaveRequestId());
			leavePojo.setLeaveName(leave.getLeaveName());
			
			DateFormat dfStartDate = new SimpleDateFormat("dd-MM-yyyy");
			if(leave.getStartDate()!=null)
			{
				leavePojo.setStartDate(dfStartDate.format(leave.getStartDate()));
			}
			
			DateFormat dfEndDate = new SimpleDateFormat("dd-MM-yyyy");
			if(leave.getEndDate()!=null)
			{
				leavePojo.setEndDate(dfEndDate.format(leave.getEndDate()));
			}
			
            leavePojo.setShortForm(leave.getShortForm());
            leavePojo.setStatus(leave.getStatus());
            leavePojoList.add(leavePojo);
			
		}
		
		HashMap<String, List<LeaveRequestPojo>> map=new HashMap<String,List<LeaveRequestPojo>>();
		map.put("Leave", leavePojoList);
		return ResponseEntity.ok(map);
		
	}
	
	
	@DeleteMapping("/deleteLeave/{leaveRequestId}")
	public ResponseEntity<?> deleteLeave(@PathVariable long leaveRequestId) {
		LeaveRequest leaveDetails = leaveDao.findByLeaveRequestId(leaveRequestId);
		if (leaveDetails != null) 
		{
			leaveDao.deleteById(leaveRequestId);
			return ResponseEntity.ok(Responses.builder().message("Leave Details are Deleted Successfully.").build());
		} 
		else 
		{
			return ResponseEntity.badRequest().body(Responses.builder().message("Id not found").build());
		}
	}
	
	 

	
	@PostMapping("/updateLeaveStatus/{leaveRequestId}/{status}/{employeeId}/{leaveId}")
	public ResponseEntity<?> ApproveLeave(@PathVariable long leaveRequestId,@PathVariable String status,@PathVariable String employeeId,@PathVariable long leaveId )
	{
	    LeaveRequest leaveDetails = leaveDao.findByLeaveRequestId(leaveRequestId);
		Employee employee=employeeDao.findByEmployeeId(employeeId);
		
		DateFormat dfStartDate = new SimpleDateFormat("yyyy-MM-dd");
 		String  startDate = dfStartDate.format(leaveDetails.getStartDate());
 		 DateFormat dfEndDate = new SimpleDateFormat("yyyy-MM-dd");
 		String endDate =dfEndDate.format(leaveDetails.getEndDate());
		System.out.println(startDate);
		  LocalDate date1 = LocalDate.parse(startDate);
		    LocalDate date2 = LocalDate.parse(endDate);
		    long days =  ChronoUnit.DAYS.between(date1, date2);
	 		System.out.println("no. of days"+days);
	 if (leaveDetails != null)
		{
		 if(status.equalsIgnoreCase("APPROVED"))
		 {
		 leaveDetails.setStatus(status);
		 leaveDao.save(leaveDetails);
		 
		 LeaveMaster leaveMaster=leaveMasterDao.findByLeaveId(leaveId);
					 
		 LeavesAudit leaveAudit=leaveAuditDao.findByemployee(employee.getEmplyoeeCode(),leaveMaster.getLeaveId());
			
		 if(leaveAudit!=null)
		  {
		  
			  int leaveAwaiting= leaveAudit.getLeavesAwaitingApproval();
			  int leavesApproved=leaveAudit.getLeavesApproved();
			  if(days==0)
			  {
				  if(leaveAwaiting!=0)
				  {
				 long leaveAwaitingForSingle=leaveAwaiting-1;
				 System.out.println(leaveAwaitingForSingle); 
	 			 int leaveAwaitingOne=(int)leaveAwaitingForSingle;
	 			  leaveAudit.setLeavesAwaitingApproval(leaveAwaitingOne);
	 			  
	 			 int leaveApproveTotal=leavesApproved+1;
				  System.out.println(leaveApproveTotal);
				  leaveAudit.setLeavesApproved(leaveApproveTotal);
				  leaveAuditDao.save(leaveAudit);
				  }
	 		 	  
			  }
			  else {
				  if(leaveAwaiting!=0)
				  {
					  long totalDays= days+1;
						 System.out.println(totalDays);
			  long leaveAwaitingTotal=leaveAwaiting-totalDays;
              System.out.println(leaveAwaitingTotal);
              int leaveAwaitingBal=(int)leaveAwaitingTotal;
			  leaveAudit.setLeavesAwaitingApproval(leaveAwaitingBal);
			  
			  long leaveApproval=leavesApproved+totalDays;
			  System.out.println(leaveApproval);
			  int leaveApprovalTotal=(int)leaveApproval;
			  leaveAudit.setLeavesApproved(leaveApprovalTotal);
			  leaveAuditDao.save(leaveAudit);
				  }
			  
			
			  }
			  
			 
			  LeaveEmployeeEntitlement leaveEmpEntitlement=empEntitlementDao.findByLeaveAudit(leaveAudit.getLeaveAuditId());
			  if(leaveEmpEntitlement!=null)
			  {
			  
				  leaveEmpEntitlement.setLeavesAwaitingApproval(leaveAudit.getLeavesAwaitingApproval());
				  leaveEmpEntitlement.setLeavesApproved(leaveAudit.getLeavesApproved());
				  empEntitlementDao.save(leaveEmpEntitlement);
				  
			  
			  }
				  
		  }
		 emailServiceImpl.sendSimpleMessage(employee.getOfficialEmailId(), " Leave Request Mail "," Hi " + employee.getFirstName() + " , Your " + leaveDetails.getLeaveName() + " has been " + leaveDetails.getStatus() + " . ");
		 
		  return ResponseEntity.ok(Responses.builder().message("LeaveRequest Approved Successfully").build());
		 }
		 
		 else if (leaveDetails != null)
			{
			 if(status.equalsIgnoreCase("REJECTED"))
			 {
			 leaveDetails.setStatus(status);
			 leaveDao.save(leaveDetails); 
			 
			 LeaveMaster leavemaster=leaveMasterDao.findByLeaveId(leaveId);
				
			 LeavesAudit leaveAudits=leaveAuditDao.findByemployee(employee.getEmplyoeeCode(),leavemaster.getLeaveId());
				
			 
			 if(leaveAudits!=null)
			  {
			    
				  int leaveBalance=leaveAudits.getLeaveBalance();
				  int leaveAwaiting= leaveAudits.getLeavesAwaitingApproval();
				  if(days==0)
				  {
					  
					  
					  if(leaveAwaiting!=0)
					  {
					  int leaveEmpTotal= leaveAwaiting-1;
					  System.out.println(leaveEmpTotal);
					  leaveAudits.setLeavesAwaitingApproval(leaveEmpTotal);
					  
					  
					  
					  int leaveBalanceTotal=leaveBalance+1;
					  System.out.println(leaveBalanceTotal); 
					  leaveAudits.setLeaveBalance(leaveBalanceTotal);	
					  leaveAuditDao.save(leaveAudits);
					  }
				  }
				  else {
				  
				  
				  if(leaveAwaiting!=0)
				  {
					  long totalDays= days+1;
						 System.out.println(totalDays);
				  long leaveAwait=leaveAwaiting-totalDays;
	              System.out.println(leaveAwait);
	              int leaveAwaitingT=(int)leaveAwait;
				  leaveAudits.setLeavesAwaitingApproval(leaveAwaitingT);
				  
				  
				  long leaveBalanceTotal=leaveBalance +totalDays;
	              System.out.println("rejected leave balance"+leaveBalanceTotal);
	              int Balanace=(int)leaveBalanceTotal;
				  leaveAudits.setLeaveBalance(Balanace);
				  leaveAuditDao.save(leaveAudits);
				  }
				
				  
				  }
				  
				 LeaveEmployeeEntitlement leaveEmpEntitlement=empEntitlementDao.findByLeaveAudit(leaveAudits.getLeaveAuditId());
				 if(leaveEmpEntitlement!=null)
				  {
				    
					  leaveEmpEntitlement.setLeaveBalance(leaveAudits.getLeaveBalance());	
					  leaveEmpEntitlement.setLeavesAwaitingApproval(leaveAudits.getLeavesAwaitingApproval());
					  empEntitlementDao.save(leaveEmpEntitlement);
				  
				  }
				  
				  }
				  
					  
			  
			 emailServiceImpl.sendSimpleMessage(employee.getOfficialEmailId(), " Leave Request Mail "," Hi " + employee.getFirstName() + " , Your " + leaveDetails.getLeaveName() + " has been " + leaveDetails.getStatus() + " . ");
			 
			  return ResponseEntity.ok(Responses.builder().message("LeaveRequest Rejected Successfully").build());
			 }
			 
			 else if (leaveDetails != null)
				{
				 if(status.equalsIgnoreCase("CANCELLED"))
					 
				 {
				 leaveDetails.setStatus(status);
				 leaveDao.save(leaveDetails); 
				 
				 LeaveMaster leavesMaster=leaveMasterDao.findByLeaveId(leaveId);
					
				 LeavesAudit leaveAudited=leaveAuditDao.findByemployee(employee.getEmplyoeeCode(),leavesMaster.getLeaveId());
				 
				 
				 if(leaveAudited!=null)
				  {
				    
					 
					  int leaveBalance=leaveAudited.getLeaveBalance();
					  int leaveEmp= leaveAudited.getLeavesAwaitingApproval();
					  if(days==0)
					  {
					  int leaveBalanceTotal=leaveBalance+1;
					  System.out.println(leaveBalanceTotal); 
					  leaveAudited.setLeaveBalance(leaveBalanceTotal);	
					  
					  
					 
					  int leaveEmpTotal= leaveEmp-1;
					  System.out.println(leaveEmpTotal);
					  leaveAudited.setLeavesAwaitingApproval(leaveEmpTotal);
					  leaveAuditDao.save(leaveAudited);
					  }
					  else {
						  long totalDays= days+1;
							 System.out.println(totalDays);
						  long leaveAwaitCancel=leaveEmp-totalDays;
			              System.out.println(leaveAwaitCancel);
			              int leaveAwaitingCancelled=(int)leaveAwaitCancel;
			              leaveAudited.setLeavesAwaitingApproval(leaveAwaitingCancelled);
						  
						  
						  long leaveBalanceCancel=leaveBalance +totalDays;
			              System.out.println("cancelled leave balance"+leaveBalanceCancel);
			              int BalanaceCancel=(int)leaveBalanceCancel;
			              leaveAudited.setLeaveBalance(BalanaceCancel);
			              leaveAuditDao.save(leaveAudited);
					  }
					  
					  
					  LeaveEmployeeEntitlement leaveEmpEntitlement=empEntitlementDao.findByLeaveAudit(leaveAudited.getLeaveAuditId());
					  
					  if(leaveEmpEntitlement!=null)
					  {
					   
						  leaveEmpEntitlement.setLeaveBalance(leaveAudited.getLeaveBalance());	
						  
					
						  leaveEmpEntitlement.setLeavesAwaitingApproval(leaveAudited.getLeavesAwaitingApproval());
						  empEntitlementDao.save(leaveEmpEntitlement);
						  
					  }
		
					   	  
				  }
				 emailServiceImpl.sendSimpleMessage(employee.getOfficialEmailId(), " Leave Request Mail "," Hi " + employee.getFirstName() + " , Your " + leaveDetails.getLeaveName() + " has been " + leaveDetails.getStatus() + " . ");
				 
				  return ResponseEntity.ok(Responses.builder().message("LeaveRequest Cancelled Successfully").build());
				 }
				 
			}	 
			 
			}
		 }
			 
		  
	 return ResponseEntity.badRequest().body(Responses.builder().message("LeaveRequest Id "+ " " +leaveDetails+ " "+ " Not Found ").build());
				 
}
	
	
	 
		@PostMapping("/cancelLeaveRequest/{leaveRequestId}/{status}/{employeeId}/{leaveId}")
		public ResponseEntity<?> cancelLeaveRequest(@PathVariable long leaveRequestId,@PathVariable String status,@PathVariable String employeeId, @PathVariable long leaveId )
		{
			
			
			 LeaveRequest leaveDetails = leaveDao.findByLeaveRequestId(leaveRequestId);
		     Employee employee=employeeDao.findByEmployeeId(employeeId);
				
				DateFormat dfStartDate = new SimpleDateFormat("yyyy-MM-dd");
		 		String  startDate = dfStartDate.format(leaveDetails.getStartDate());
		 		 DateFormat dfEndDate = new SimpleDateFormat("yyyy-MM-dd");
		 		String endDate =dfEndDate.format(leaveDetails.getEndDate());
				System.out.println(startDate);
				  LocalDate date1 = LocalDate.parse(startDate);
				    LocalDate date2 = LocalDate.parse(endDate);
				    long days =  ChronoUnit.DAYS.between(date1, date2);
			 		System.out.println("no. of days"+days);
			 		
			  if (leaveDetails != null)
				{
				  
			   if(status.equalsIgnoreCase("CANCELLED"))
				 {
				 
				 if(leaveDetails.getStatus().equalsIgnoreCase("PENDING"))
				 {
				 leaveDetails.setStatus(status);
				 leaveDao.save(leaveDetails); 
				 
				 LeaveMaster leavesMaster=leaveMasterDao.findByLeaveId(leaveId);
					
				 LeavesAudit leaveAudited=leaveAuditDao.findByemployee(employee.getEmplyoeeCode(),leavesMaster.getLeaveId());
				  
				 if(leaveAudited!=null)
				  {
				     
					  int leaveBalance=leaveAudited.getLeaveBalance();
					  System.out.println("LeaveBalance:" +leaveBalance);
					  
					  int leaveEmp= leaveAudited.getLeavesAwaitingApproval();
					  if(days==0)
					  {
					  int leaveBalanceTotal=leaveBalance+1;
					  System.out.println(leaveBalanceTotal); 
					  leaveAudited.setLeaveBalance(leaveBalanceTotal);	
					  
					  
					 
					  int leaveEmpTotal= leaveEmp-1;
					  System.out.println(leaveEmpTotal);
					  leaveAudited.setLeavesAwaitingApproval(leaveEmpTotal);
					  leaveAuditDao.save(leaveAudited);
					  }
					  else {
						  long totalDays= days+1;
							 System.out.println(totalDays);
						  long leaveAwaitCancel=leaveEmp-totalDays;
			              System.out.println(leaveAwaitCancel);
			              int leaveAwaitingCancelled=(int)leaveAwaitCancel;
			              leaveAudited.setLeavesAwaitingApproval(leaveAwaitingCancelled);
						  
						  
						  long leaveBalanceCancel=leaveBalance +totalDays;
			              System.out.println("cancelled leave balance " +leaveBalanceCancel);
			              int BalanaceCancel=(int)leaveBalanceCancel;
			              leaveAudited.setLeaveBalance(BalanaceCancel);
			              leaveAuditDao.save(leaveAudited);
					  }
				  } 
				  
				 LeaveEmployeeEntitlement leaveEmpEntitlement=empEntitlementDao.findByLeaveAudit(leaveAudited.getLeaveAuditId());
				  
				  if(leaveEmpEntitlement!=null)
				  {
				   
					  leaveEmpEntitlement.setLeaveBalance(leaveAudited.getLeaveBalance());	
					  
				
					  leaveEmpEntitlement.setLeavesAwaitingApproval(leaveAudited.getLeavesAwaitingApproval());
					  empEntitlementDao.save(leaveEmpEntitlement);
				  
				  }
				  emailServiceImpl.sendSimpleMessage(employee.getOfficialEmailId(), " Leave Request Mail "," Hi " + employee.getFirstName() + " , Your " + leaveDetails.getLeaveName() + " has been " + leaveDetails.getStatus() + " . ");
					 
				  return ResponseEntity.ok(Responses.builder().message("LeaveRequest Cancelled Successfully").build());
				 }
				 
					 
				 else if(leaveDetails.getStatus().equalsIgnoreCase("APPROVED"))
					    {
					    	System.out.println("status" +status);
					 leaveDetails.setStatus(status);
					 leaveDao.save(leaveDetails); 
					 
					 LeaveMaster leavesmaster=leaveMasterDao.findByLeaveId(leaveId);
						
					 LeavesAudit leaveAudit=leaveAuditDao.findByemployee(employee.getEmplyoeeCode(),leavesmaster.getLeaveId());
					  
					 if(leaveAudit!=null)
					  {
					     
						  int leaveBalance=leaveAudit.getLeaveBalance();
						  int leaveEmp= leaveAudit.getLeavesApproved();
						  if(days==0)
						  {
						  int leaveBalanceTotal=leaveBalance+1;
						  System.out.println(leaveBalanceTotal); 
						  leaveAudit.setLeaveBalance(leaveBalanceTotal);	
						  
						  
						 
						  int leaveEmpTotal= leaveEmp-1;
						  System.out.println(leaveEmpTotal);
						  leaveAudit.setLeavesApproved(leaveEmpTotal);
						  leaveAuditDao.save(leaveAudit);
						  }
						  else {
							  long totalDays= days+1;
								 System.out.println(totalDays);
							  long leaveAwaitCancel=leaveEmp-totalDays;
				              System.out.println(leaveAwaitCancel);
				              int leaveAwaitingCancelled=(int)leaveAwaitCancel;
				              leaveAudit.setLeavesApproved(leaveAwaitingCancelled);
							  
							  
							  long leaveBalanceCancel=leaveBalance +totalDays;
				              System.out.println("Cancelled leave balance" +leaveBalanceCancel);
				              int BalanaceCancel=(int)leaveBalanceCancel;
				              leaveAudit.setLeaveBalance(BalanaceCancel);
				              leaveAuditDao.save(leaveAudit);
						  }
					   
					  }
					  
					  LeaveEmployeeEntitlement leaveEmpEntitle=empEntitlementDao.findByLeaveAudit(leaveAudit.getLeaveAuditId());
					  
					  if(leaveEmpEntitle!=null)
					  {
					   
						  leaveEmpEntitle.setLeaveBalance(leaveAudit.getLeaveBalance());	
						  
					
						  leaveEmpEntitle.setLeavesApproved(leaveAudit.getLeavesApproved());
						  empEntitlementDao.save(leaveEmpEntitle);
					  
				  }
				 }
				}  
				}
				 emailServiceImpl.sendSimpleMessage(employee.getOfficialEmailId(), " Leave Request Mail "," Hi " + employee.getFirstName() + " , Your " + leaveDetails.getLeaveName() + " has been " + leaveDetails.getStatus() + " . ");
				 
				  return ResponseEntity.ok(Responses.builder().message("LeaveRequest Cancelled Successfully").build());
				 
			
		      }
		



	
	    @PostMapping("/updateLeaveComment/{leaveRequestId}/{status}")
		public ResponseEntity<?> updateLeaveComment(@PathVariable long leaveRequestId,@PathVariable String status,@RequestParam(name = "comment", required = false) String comment )
		{
		 LeaveRequest leaveDetails = leaveDao.findByLeaveRequestId(leaveRequestId);
		 
		 if (leaveDetails != null)
		 {
				/*
				 * LeaveRequest leaveDetail=leaveDao.findByStatus(status);
				 * 
				 * if(leaveDetail==null) { return
				 * ResponseEntity.badRequest().body(Responses.builder().
				 * message("LeaveRequest Approved already exists").build()); }
				 */
				 
			 leaveDetails.setStatus(status);
			 leaveDetails.setComment(comment);
			 
			 leaveDao.save(leaveDetails);
			 return ResponseEntity.ok(Responses.builder().message("LeaveRequest sent Sucessfully").build());
		}
		
		 else
		 { 
			 //return ResponseEntity.badRequest().body(Responses.builder().message("LeaveRequest Id "+ " " +leaveDetails+ " "+ "not found").build());
			 return ResponseEntity.badRequest().body(Responses.builder().message("LeaveRequest Id not found").build());
			  
		 }
			 
		}
	    
	    
	    @GetMapping("LeavesByStatus/{status}")
		  public ResponseEntity<?> ApproveLeaves(@PathVariable String status){
			  List<LeaveRequest> leave=leaveDao.findBysetStatus(status);
			  List<LeaveRequestPojo> leaveReqPojoList = new ArrayList<LeaveRequestPojo>();
			  if(status.equalsIgnoreCase("APPROVED"))
			  {
			  for(LeaveRequest leaveReq : leave)
			  {
				  LeaveRequestPojo leaveReqPojo = new LeaveRequestPojo();
				  leaveReqPojo.setEmployeeId(leaveReq.getEmployee().getEmployeeId());
				  //leaveReqPojo.setSupervisorId(leaveReq.getEmployee().getSupervisorId());
				  //System.out.println(leaveReq.getSupervisor());
				  leaveReqPojo.setSupervisorId(leaveReq.getSupervisor().getEmployeeId());
				  leaveReqPojo.setSupervisorName(leaveReq.getSupervisor().getFirstName());
				  leaveReqPojo.setComment(leaveReq.getComment());
				  leaveReqPojo.setLeaveRequestId(leaveReq.getLeaveRequestId());
				  leaveReqPojo.setLeaveName(leaveReq.getLeaveName());
				  
				  DateFormat dfStartDate = new SimpleDateFormat("dd-MM-yyyy");
					if(leaveReq.getStartDate()!=null)
					{
						leaveReqPojo.setStartDate(dfStartDate.format(leaveReq.getStartDate()));
					}
					
					DateFormat dfEndDate = new SimpleDateFormat("dd-MM-yyyy");
					if(leaveReq.getEndDate()!=null)
					{
						leaveReqPojo.setEndDate(dfEndDate.format(leaveReq.getEndDate()));
					}
					
					leaveReqPojo.setShortForm(leaveReq.getShortForm());
					leaveReqPojo.setStatus(leaveReq.getStatus());
					leaveReqPojoList.add(leaveReqPojo);
			   }
			  HashMap<String,List<LeaveRequestPojo>> map=new HashMap<String, List<LeaveRequestPojo>>();
			    map.put("LeaveRequest", leaveReqPojoList); 
			    return ResponseEntity.ok(map);
			  }
			  else if(status.equalsIgnoreCase("PENDING"))
			  {
				  
				  for(LeaveRequest leaveReq : leave)
				  {
					  LeaveRequestPojo leaveReqPojo = new LeaveRequestPojo();
					  leaveReqPojo.setEmployeeId(leaveReq.getEmployee().getEmployeeId());
					  leaveReqPojo.setSupervisorId(leaveReq.getSupervisor().getEmployeeId());
					  leaveReqPojo.setSupervisorName(leaveReq.getSupervisor().getFirstName());
					  leaveReqPojo.setComment(leaveReq.getComment());
					  leaveReqPojo.setLeaveRequestId(leaveReq.getLeaveRequestId());
					  leaveReqPojo.setLeaveName(leaveReq.getLeaveName());
					  
					  DateFormat dfStartDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveReq.getStartDate()!=null)
						{
							leaveReqPojo.setStartDate(dfStartDate.format(leaveReq.getStartDate()));
						}
						
						DateFormat dfEndDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveReq.getEndDate()!=null)
						{
							leaveReqPojo.setEndDate(dfEndDate.format(leaveReq.getEndDate()));
						}
						
						leaveReqPojo.setShortForm(leaveReq.getShortForm());
						leaveReqPojo.setStatus(leaveReq.getStatus());
						leaveReqPojoList.add(leaveReqPojo);
				  }
				  HashMap<String,List<LeaveRequestPojo>> map=new HashMap<String, List<LeaveRequestPojo>>();
				    map.put("LeaveRequest", leaveReqPojoList); 
				    return ResponseEntity.ok(map);
				 }
			  else if(status.equalsIgnoreCase("REJECTED"))
			  {
				  
				  for(LeaveRequest leaveReq : leave)
				  {
					  LeaveRequestPojo leaveReqPojo = new LeaveRequestPojo();
					  leaveReqPojo.setEmployeeId(leaveReq.getEmployee().getEmployeeId());
					  leaveReqPojo.setSupervisorId(leaveReq.getSupervisor().getEmployeeId());
					  leaveReqPojo.setSupervisorName(leaveReq.getSupervisor().getFirstName());
					  leaveReqPojo.setComment(leaveReq.getComment());
					  leaveReqPojo.setLeaveRequestId(leaveReq.getLeaveRequestId());
					  leaveReqPojo.setLeaveName(leaveReq.getLeaveName());
					  
					  DateFormat dfStartDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveReq.getStartDate()!=null)
						{
							leaveReqPojo.setStartDate(dfStartDate.format(leaveReq.getStartDate()));
						}
						
						DateFormat dfEndDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveReq.getEndDate()!=null)
						{
							leaveReqPojo.setEndDate(dfEndDate.format(leaveReq.getEndDate()));
						}
						
						leaveReqPojo.setShortForm(leaveReq.getShortForm());
						leaveReqPojo.setStatus(leaveReq.getStatus());
						leaveReqPojoList.add(leaveReqPojo);
				  }
			  }
				  HashMap<String,List<LeaveRequestPojo>> map=new HashMap<String, List<LeaveRequestPojo>>();
				    map.put("LeaveRequest", leaveReqPojoList); 
				    return ResponseEntity.ok(map);
			
			 }
			 
	 
	 
		  @PostMapping("/PendingLeaves/{leaveRequestId}/{status}")
			public ResponseEntity<?> ApproveLeaves(@PathVariable long leaveRequestId,@PathVariable String status )
			{
			 LeaveRequest leaveDetails = leaveDao.findByLeaveRequestId(leaveRequestId);
			 if (leaveDetails != null)
				{
				 leaveDetails.setStatus(status);
				 leaveDao.save(leaveDetails);
				return ResponseEntity.ok(Responses.builder().message("LeaveRequest Pending").build());
				}
				else {
					 return ResponseEntity.badRequest().body(Responses.builder().message("LeaveRequest Id "+ " " +leaveDetails+ " "+ " Not Found ").build());
				}
			}
		 
		  

		  @PostMapping("/RejectedLeaves/{leaveRequestId}/{status}")
			public ResponseEntity<?> RejectedLeaves(@PathVariable long leaveRequestId,@PathVariable String status )
			{
			 LeaveRequest leaveDetails = leaveDao.findByLeaveRequestId(leaveRequestId);
			 if (leaveDetails != null)
				{
				 leaveDetails.setStatus(status);
				 leaveDao.save(leaveDetails);
				return ResponseEntity.ok(Responses.builder().message("LeaveRequest Rejected").build());
				}
				else {
					 return ResponseEntity.badRequest().body(Responses.builder().message("LeaveRequest Id "+ " " +leaveDetails+ " "+ " Not Found ").build());
				}
			}
		 
		 
		  @GetMapping("LeavesByStatusById/{status}/{employeeId}")
		  public ResponseEntity<?> LeavesByStatusById(@PathVariable String status,@PathVariable String employeeId){
			  Employee employee=employeeDao.findByEmployeeId(employeeId);
			  List<LeaveRequest> leave=leaveDao.findBysetStatus(status,employee.getEmplyoeeCode());
			  List<LeaveRequestPojo> leaveReqPojoList = new ArrayList<LeaveRequestPojo>();
			  if(status.equalsIgnoreCase("APPROVED"))
			  {
			  for(LeaveRequest leaveReq : leave)
			  {
				  LeaveRequestPojo leaveReqPojo = new LeaveRequestPojo();
				  leaveReqPojo.setEmployeeId(leaveReq.getEmployee().getEmployeeId());
				  //leaveReqPojo.setSupervisorId(leaveReq.getEmployee().getSupervisorId());
				  leaveReqPojo.setSupervisorId(leaveReq.getSupervisor().getEmployeeId());
				  leaveReqPojo.setSupervisorName(leaveReq.getSupervisor().getFirstName());
				  leaveReqPojo.setComment(leaveReq.getComment());
				  leaveReqPojo.setLeaveRequestId(leaveReq.getLeaveRequestId());
				  leaveReqPojo.setLeaveName(leaveReq.getLeaveName());
				  
				  DateFormat dfStartDate = new SimpleDateFormat("dd-MM-yyyy");
					if(leaveReq.getStartDate()!=null)
					{
						leaveReqPojo.setStartDate(dfStartDate.format(leaveReq.getStartDate()));
					}
					
					DateFormat dfEndDate = new SimpleDateFormat("dd-MM-yyyy");
					if(leaveReq.getEndDate()!=null)
					{
						leaveReqPojo.setEndDate(dfEndDate.format(leaveReq.getEndDate()));
					}
					
					leaveReqPojo.setShortForm(leaveReq.getShortForm());
					leaveReqPojo.setStatus(leaveReq.getStatus());
					leaveReqPojoList.add(leaveReqPojo);
			   }
			  
			    HashMap<String,List<LeaveRequestPojo>> map=new HashMap<String, List<LeaveRequestPojo>>();
			    map.put("LeaveRequest", leaveReqPojoList); 
			    return ResponseEntity.ok(map);
			  }
			  else if(status.equalsIgnoreCase("PENDING"))
			  {
				  
				  for(LeaveRequest leaveReq : leave)
				  {
					  LeaveRequestPojo leaveReqPojo = new LeaveRequestPojo();
					  leaveReqPojo.setEmployeeId(leaveReq.getEmployee().getEmployeeId());
					 // leaveReqPojo.setSupervisorId(leaveReq.getEmployee().getSupervisorId());
					  leaveReqPojo.setSupervisorId(leaveReq.getSupervisor().getEmployeeId());
					  leaveReqPojo.setSupervisorName(leaveReq.getSupervisor().getFirstName());
					  leaveReqPojo.setComment(leaveReq.getComment());
					  leaveReqPojo.setLeaveRequestId(leaveReq.getLeaveRequestId());
					  leaveReqPojo.setLeaveName(leaveReq.getLeaveName());
					  
					  DateFormat dfStartDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveReq.getStartDate()!=null)
						{
							leaveReqPojo.setStartDate(dfStartDate.format(leaveReq.getStartDate()));
						}
						
						DateFormat dfEndDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveReq.getEndDate()!=null)
						{
							leaveReqPojo.setEndDate(dfEndDate.format(leaveReq.getEndDate()));
						}
						
						leaveReqPojo.setShortForm(leaveReq.getShortForm());
						leaveReqPojo.setStatus(leaveReq.getStatus());
						leaveReqPojoList.add(leaveReqPojo);
				  }
				  HashMap<String,List<LeaveRequestPojo>> map=new HashMap<String, List<LeaveRequestPojo>>();
				    map.put("LeaveRequest", leaveReqPojoList); 
				    return ResponseEntity.ok(map);
				 }
			  else if(status.equalsIgnoreCase("REJECTED"))
			  {
				  
				  for(LeaveRequest leaveReq : leave)
				  {
					  LeaveRequestPojo leaveReqPojo = new LeaveRequestPojo();
					  leaveReqPojo.setEmployeeId(leaveReq.getEmployee().getEmployeeId());
					  //leaveReqPojo.setSupervisorId(leaveReq.getEmployee().getSupervisorId());
					  leaveReqPojo.setSupervisorId(leaveReq.getSupervisor().getEmployeeId());
					  leaveReqPojo.setSupervisorName(leaveReq.getSupervisor().getFirstName());
					  leaveReqPojo.setComment(leaveReq.getComment());
					  leaveReqPojo.setLeaveRequestId(leaveReq.getLeaveRequestId());
					  leaveReqPojo.setLeaveName(leaveReq.getLeaveName());
					  
					  DateFormat dfStartDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveReq.getStartDate()!=null)
						{
							leaveReqPojo.setStartDate(dfStartDate.format(leaveReq.getStartDate()));
						}
						
						DateFormat dfEndDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveReq.getEndDate()!=null)
						{
							leaveReqPojo.setEndDate(dfEndDate.format(leaveReq.getEndDate()));
						}
						
						leaveReqPojo.setShortForm(leaveReq.getShortForm());
						leaveReqPojo.setStatus(leaveReq.getStatus());
						leaveReqPojoList.add(leaveReqPojo);
				  }
			  }
				  HashMap<String,List<LeaveRequestPojo>> map=new HashMap<String, List<LeaveRequestPojo>>();
				    map.put("LeaveRequest", leaveReqPojoList); 
				    return ResponseEntity.ok(map);
			
			 }
			  
		  @GetMapping("leaveDetails/{supervisorId}/{status}")
	      public ResponseEntity<?> leaveDetails(@PathVariable String supervisorId,@PathVariable String status)
	
	      {
	    	  Employee employee = employeeDao.findByEmployeeId(supervisorId);
	    	  List<LeaveRequest> leave=leaveDao.findBysupervisorId(employee.getEmplyoeeCode(),status);
			  List<LeaveRequestPojo> leaveReqPojoList = new ArrayList<LeaveRequestPojo>();
			  for(LeaveRequest leaveRequ:leave)
		      { 
				  LeaveRequestPojo leaveReqPojo=new LeaveRequestPojo();
				  leaveReqPojo.setEmployeeId(leaveRequ.getEmployee().getEmployeeId());
				  //System.out.println(leaveRequ.getSupervisor().getEmployeeId());
				  leaveReqPojo.setEmpName(leaveRequ.getEmployee().getFirstName());
				  leaveReqPojo.setLeaveRequestId(leaveRequ.getLeaveRequestId());
				  leaveReqPojo.setSupervisorId(leaveRequ.getSupervisor().getEmployeeId());
				  leaveReqPojo.setSupervisorName(leaveRequ.getSupervisor().getFirstName());
				  leaveReqPojo.setLeaveName(leaveRequ.getLeaveName());
				  leaveReqPojo.setCreatedAt(leaveRequ.getCreatedAt());
				  
				  DateFormat dfStartDate = new SimpleDateFormat("dd-MM-yyyy");
					if(leaveRequ.getStartDate()!=null)
					{
						leaveReqPojo.setStartDate(dfStartDate.format(leaveRequ.getStartDate()));
					}
					
					DateFormat dfEndDate = new SimpleDateFormat("dd-MM-yyyy");
					if(leaveRequ.getEndDate()!=null)
					{
						leaveReqPojo.setEndDate(dfEndDate.format(leaveRequ.getEndDate()));
					}
			  
					leaveReqPojo.setShortForm(leaveRequ.getShortForm());
                    leaveReqPojo.setComment(leaveRequ.getComment());
                    leaveReqPojo.setStatus(leaveRequ.getStatus());
                    //leaveReqPojo.setCreatedAt(new Date());
                    leaveReqPojoList.add(leaveReqPojo);
                    //System.out.println(leaveReqPojoList.size());
                   
		         }
			  
			          HashMap<String, List<LeaveRequestPojo>> map=new HashMap<String,List<LeaveRequestPojo>>(); 
			          map.put("LeaveDetails", leaveReqPojoList); 
					  return ResponseEntity.ok(map);			  
	    	  
	           }
	      
		      
		      @GetMapping("leaveDetails/{supervisorId}")
		      public ResponseEntity<?> leaveDetails(@PathVariable String supervisorId)
		
		      {
		    	  Employee employee = employeeDao.findByEmployeeId(supervisorId);
		    	  List<LeaveRequest> leave=leaveDao.findBysupervisorId(employee.getEmplyoeeCode());
				  List<LeaveRequestPojo> leaveReqPojoList = new ArrayList<LeaveRequestPojo>();
				  for(LeaveRequest leaveRequ:leave)
			      { 
					  LeaveRequestPojo leaveReqPojo=new LeaveRequestPojo();
					  leaveReqPojo.setEmployeeId(leaveRequ.getEmployee().getEmployeeId());
					  //System.out.println(leaveRequ.getSupervisor().getEmployeeId());
					  leaveReqPojo.setEmpName(leaveRequ.getEmployee().getFirstName());
					  leaveReqPojo.setLeaveRequestId(leaveRequ.getLeaveRequestId());
					  leaveReqPojo.setSupervisorId(leaveRequ.getSupervisor().getEmployeeId());
					  leaveReqPojo.setSupervisorName(leaveRequ.getSupervisor().getFirstName());
					  leaveReqPojo.setLeaveName(leaveRequ.getLeaveName());
					  leaveReqPojo.setCreatedAt(leaveRequ.getCreatedAt());
					  
					  DateFormat dfStartDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveRequ.getStartDate()!=null)
						{
							leaveReqPojo.setStartDate(dfStartDate.format(leaveRequ.getStartDate()));
						}
						
						DateFormat dfEndDate = new SimpleDateFormat("dd-MM-yyyy");
						if(leaveRequ.getEndDate()!=null)
						{
							leaveReqPojo.setEndDate(dfEndDate.format(leaveRequ.getEndDate()));
						}
				  
						leaveReqPojo.setShortForm(leaveRequ.getShortForm());
                        leaveReqPojo.setComment(leaveRequ.getComment());
                        leaveReqPojo.setStatus(leaveRequ.getStatus());     
                        leaveReqPojoList.add(leaveReqPojo);
                      
                       
			         }
				  
				          HashMap<String, List<LeaveRequestPojo>> map=new HashMap<String,List<LeaveRequestPojo>>(); 
				          map.put("LeaveDetails", leaveReqPojoList); 
						  return ResponseEntity.ok(map);			  
		    	  
		           }
		      
		      
		      @GetMapping("/leaveRequestView/{employeeId}/{month}/{year}")
		  	public ResponseEntity<?> leaveRequestView(@PathVariable String employeeId, @PathVariable long month, @PathVariable long year)
		  	{
		  		
		  		Employee emp=employeeDao.findByEmployeeId(employeeId);
		  		if(emp!=null)
		  		{
		  		List<LeaveRequest> leaveDetails = leaveDao.getLeaveView(emp.getEmplyoeeCode(), month, year);
		  		List<LeaveRequestViewPojo> leaveList = new ArrayList<>();
		  		for(LeaveRequest leave:leaveDetails)
		  		{
		  			LeaveRequestViewPojo leaveReqViewPojo=new LeaveRequestViewPojo();
		  			
		  			String empName = leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName();
		  			leaveReqViewPojo.setEmpName(empName);
		  			
		  			leaveReqViewPojo.setEmpName(empName);
		  			leaveReqViewPojo.setLeaveName(leave.getLeaveName());
		  			leaveReqViewPojo.setStatus(leave.getStatus());
		  			leaveReqViewPojo.setSupervisorName(leave.getSupervisor().getFirstName());
		  			leaveList.add(leaveReqViewPojo);
		  			
		  		}
		  		
		  		HashMap<String, List<LeaveRequestViewPojo>> map = new HashMap<String, List<LeaveRequestViewPojo>>();
		  		map.put("LeaveReqView", leaveList);
		  		return ResponseEntity.ok(map);
		  		}
		  		else
				{
					return ResponseEntity.badRequest().body(Responses.builder().message("No Data for Employee").build());
				}
		  		
		  	}
		  	
		      
		      
		      
		      
		      
		      
		      
		      
		     
}      

