package com.dizitiveit.hrms.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.dizitiveit.hrms.pojo.Responses;

@RestController
@RequestMapping("leaveEmpEntitlement")
public class LeaveEmpEntitlementController {

	@Autowired
	private LeaveEmpEntitlementDao empEntitlementDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private LeaveMasterDao leaveMasterDao;

	@Autowired
	private LeavesAuditDao leavesAuditDao;
	
	@Autowired
	private LeaveRequestDao leaveRequestDao;

	@PostMapping("/addLeaveEmpEntitlement/{leaveId}/{leaveAuditId}")
	public ResponseEntity<?> addLeaveEmpEntitlement(@RequestBody LeaveEmployeeEntitlement leaveEmpEntitlement,@PathVariable long leaveAuditId)
			  {
	
		LeavesAudit leaveAudit = leavesAuditDao.findByLeaveAuditId(leaveAuditId);
		leaveEmpEntitlement.setLeavesAudit(leaveAudit);

		empEntitlementDao.save(leaveEmpEntitlement);
		return ResponseEntity.ok(Responses.builder().message("Leave Employee Entitlement Saved  Sucessfully").build());

	}
	

	 // @Scheduled(cron="0 0 * * * *") //Every one hour
	  
	  @Scheduled(cron="0 0 0 1 */3 *") //Every Quarter(Schedular run every 3 months)
	public void leaveEntitlement() {
		List<Employee> employeeList = employeeDao.findAll();
		
		int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
		int CurrentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
		String financiyalYearFrom = "";
		String financiyalYearTo = "";
		if (CurrentMonth < 4) {

			financiyalYearFrom = String.valueOf(CurrentYear - 1);
			financiyalYearTo = String.valueOf(CurrentYear);
		} else {
			financiyalYearFrom = String.valueOf(CurrentYear);
			financiyalYearTo = String.valueOf(CurrentYear + 1);
		}

		String financialYear = financiyalYearFrom + "-" + financiyalYearTo;
		System.out.println(financialYear);
		Date date = new Date();
		for (Employee employee : employeeList) {

				Employee employeeNew = employeeDao.findByEmployeeId(employee.getEmployeeId());
				List<LeaveMaster> leavemasterList = leaveMasterDao.getByPeriod("Quarterly",true);
				//System.out.println(leavemasterList.size());
			     for(LeaveMaster leaveMaster : leavemasterList)
			     {
			    	 System.out.println("leave master size"+leavemasterList.size());
			    	 //LeavesAudit leaveAudits=leavesAuditDao.findByEmployee(employee.getEmplyoeeCode(),leaveMaster.getLeaveId(),CurrentMonth,CurrentYear,financialYear);
			    	 List<LeavesAudit> leavesAuditList=leavesAuditDao.getByFinancialYear(employee.getEmplyoeeCode(),financialYear,leaveMaster.getLeaveId());
			    	 //if(leaveAudits==null)
			    	 //{
			    	 System.out.println("leave audit size"+leavesAuditList.size());
			    	 if(leavesAuditList.size()==0)
			    	 {
			    	   LeavesAudit leavesAudit = new LeavesAudit();
			    	   leavesAudit.setOpeningBalance(leaveMaster.getCount());
			    	   leavesAudit.setLeaveBalance(leaveMaster.getCount());
			     	   leavesAudit.setFinancialYear(financialYear);
			    	   leavesAudit.setLastCredited(new Date());
						 //System.out.println(employeeNew.getEmployeeId());
						 leavesAudit.setLeavesCredited(leaveMaster.getCount());
						 System.out.println("leaves credit"+leaveMaster.getCount());
			    		 leavesAudit.setEmployee(employeeNew);
			    		 leavesAudit.setLeaveMaster(leaveMaster);
			    		 leavesAuditDao.save(leavesAudit);
							
							  LeavesAudit leaveAuditExisting=leavesAuditDao.findByLeaveAuditId(leavesAudit.getLeaveAuditId()); 
							  LeaveEmployeeEntitlement leaveEntitlement = new LeaveEmployeeEntitlement(); 
							  leaveEntitlement.setFinancialYear(financialYear);
							  leaveEntitlement.setLeavesAudit(leaveAuditExisting);
							  leaveEntitlement.setLeaveBalance(leaveAuditExisting.getLeaveBalance());
							  leaveEntitlement.setLeavesCredited(leaveAuditExisting.getLeavesCredited());
							  leaveEntitlement.setOpeningBalance(leaveAuditExisting.getOpeningBalance());
							  empEntitlementDao.save(leaveEntitlement);
							 
			    	 }
			    	 else {
			    		      LeavesAudit leaveAudit = new LeavesAudit();
							  LeavesAudit leaves = leavesAuditList.get(0);
							  leaveAudit.setOpeningBalance(leaves.getLeaveBalance());		
							  leaveAudit.setLeavesCredited(leaveMaster.getCount());
							  leaveAudit.setLeavesApproved( leaves.getLeavesApproved());
							  leaveAudit.setLeavesAwaitingApproval(leaves.getLeavesAwaitingApproval());
							  
						  int leaveBalance=leaveAudit.getOpeningBalance()+leaveAudit.getLeavesCredited();
						  leaveAudit.setLeaveBalance(leaveBalance);
						  
						  leaveAudit.setFinancialYear(financialYear);
						  leaveAudit.setLastCredited(new Date());
						  leaveAudit.setEmployee(employeeNew);
						  leaveAudit.setLeaveMaster(leaveMaster);
						  System.out.println(employeeNew.getEmployeeId());
						  leavesAuditDao.save(leaveAudit);
						  
							  LeavesAudit leaveaudit=leavesAuditDao.findByLeaveAuditId(leaveAudit.getLeaveAuditId());
							  LeaveEmployeeEntitlement leaveEntitlement = new LeaveEmployeeEntitlement();
							  leaveEntitlement.setFinancialYear(financialYear);
							  leaveEntitlement.setLeavesAudit(leaveaudit);
							  leaveEntitlement.setLeaveBalance(leaveaudit.getLeaveBalance());
							  System.out.println("leave credited"+leaveaudit.getLeavesCredited());
							  leaveEntitlement.setLeavesCredited(leaveaudit.getLeavesCredited());
							  leaveEntitlement.setLeavesApproved(leaveaudit.getLeavesApproved());
							  leaveEntitlement.setLeavesAwaitingApproval(leaveaudit.getLeavesAwaitingApproval());
							  leaveEntitlement.setOpeningBalance(leaveaudit.getOpeningBalance());
							  empEntitlementDao.save(leaveEntitlement);
							 
						  }
			    	 
			    	 
			    	 }
			     }
		         }      

	
			 }
      
	
   

	 
	
	
