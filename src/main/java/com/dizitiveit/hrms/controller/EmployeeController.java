package com.dizitiveit.hrms.controller;

import java.util.Date;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.AdditionalDetailsDao;
import com.dizitiveit.hrms.dao.BankDetailsDao;
import com.dizitiveit.hrms.dao.BranchDao;
import com.dizitiveit.hrms.dao.DeptDao;
import com.dizitiveit.hrms.dao.DesigDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.FamilyDetailsDao;
import com.dizitiveit.hrms.dao.OtpValidationDao;
import com.dizitiveit.hrms.dao.RolesDao;
import com.dizitiveit.hrms.service.MyUserDetailsService;
import com.dizitiveit.hrms.service.OtpSenderService;
import com.dizitiveit.hrms.model.AdditionalDetails;
import com.dizitiveit.hrms.model.AuthenticationRequest;
import com.dizitiveit.hrms.model.AuthenticationResponse;
import com.dizitiveit.hrms.model.BankDetails;
import com.dizitiveit.hrms.model.Branch;
import com.dizitiveit.hrms.model.Department;
import com.dizitiveit.hrms.model.Designation;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.FamilyDetails;
import com.dizitiveit.hrms.model.OtpValidation;
import com.dizitiveit.hrms.model.Roles;
import com.dizitiveit.hrms.pojo.Responses;
import com.dizitiveit.hrms.pojo.RolesPojo;
import com.dizitiveit.hrms.pojo.additionalDetailsPojo;
import com.dizitiveit.hrms.util.JwtUtil;
import com.dizitiveit.hrms.pojo.EmployeePojo;
import com.dizitiveit.hrms.pojo.ProfilePojo;
import com.dizitiveit.hrms.pojo.BranchsPojo;
import com.dizitiveit.hrms.pojo.DeptPojo;
import com.dizitiveit.hrms.pojo.DesgPojo;
import com.dizitiveit.hrms.pojo.EmpListPojo;
import com.dizitiveit.hrms.pojo.EmpPojo;

@RequestMapping("/employee")
@RestController
public class EmployeeController {

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private RolesDao rolesDao;
	
	@Autowired
	private AdditionalDetailsDao additionalDetailsDao;
	
	@Autowired
	private FamilyDetailsDao familyDetailsDao;
	
	@Autowired
	private BankDetailsDao bankDetailsDao;
	
	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private OtpSenderService otpService;

	@Autowired
	private OtpValidationDao otpvalidationDao;

	@Autowired
	private BranchDao branchDao;

	@Autowired
	private DeptDao deptDao;

	@Autowired
	private DesigDao desigDao;
  

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		Authentication authentication = null;

		try {
			System.out.println(authenticationRequest.getUsername());
			System.out.println(authenticationRequest.getPassword());

			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));

			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			Employee employee = employeeDao.findByOfficialEmailId(authenticationRequest.getUsername());
			if (employee == null) {

				employee = employeeDao.findByEmployeeId(authenticationRequest.getUsername());
			}
			if (employee != null) {
				final String jwt = jwtTokenUtil.generateToken(userDetails);
				return ResponseEntity.ok(
						new AuthenticationResponse(jwt, employee.getRoles().getRoleName(), employee.getEmployeeId()));
			} else {
				return ResponseEntity.ok("");
			}
		} catch (BadCredentialsException e) {
			System.out.println("error in checking password");
			//return ResponseEntity.ok("Email is already in use");
			 return ResponseEntity.ok(Responses.builder().message("Email is already in use!").build());

		}

	}

	@PostMapping("/employeeRegistration/{roleName}/{branchName}/{deptName}/{desigName}/{supervisorId}")
	public ResponseEntity<?> employeeRegistration(@RequestBody Employee employee, @PathVariable String roleName,
			@PathVariable String branchName, @PathVariable String deptName, @PathVariable String desigName,@PathVariable String supervisorId) {
		Roles roles = rolesDao.findByRoleName(roleName);
		Branch branch = branchDao.findBybranchName(branchName);
		Department department = deptDao.findByDeptName(deptName);
		Designation designation = desigDao.findByDesigName(desigName);
		Employee employeeSupervisor=employeeDao.findByEmployeeId(supervisorId);
		if (employeeDao.existsByOfficialEmailId(employee.getOfficialEmailId())) {

			//return ResponseEntity.ok("Employee Email Already Exists");
			 return ResponseEntity.badRequest().body(Responses.builder().message("Employee Email Already Exists").build());
		}
		if (employeeDao.existsByPhoneNumber(employee.getPhoneNumber())) {

			//return ResponseEntity.ok("Employee PhoneNumber Already Exists");
			 return ResponseEntity.badRequest().body(Responses.builder().message("Employee PhoneNumber Already Exists").build());
		}
		
		  if(employeeDao.existsByEmployeeId(employee.getEmployeeId())) {
		  
		  //return ResponseEntity.ok("Employee employeeId Already Exists"); 
		  return ResponseEntity.badRequest().body(Responses.builder(). message("Employee employeeId Already Exists").build()); 
		  
		  }
		 
		if (employeeDao.existsByAdharNumber(employee.getAdharNumber())) {
			//return ResponseEntity.ok("Employee Adhar Number Already Exists");
			 return ResponseEntity.badRequest().body(Responses.builder().message("Employee Adhar Number Already Exists").build());
		}
		if (employeeDao.existsByPanCardNumber(employee.getPanCardNumber())) {

			//return ResponseEntity.ok("Employee Pan Card Number Already Exists");
			 return ResponseEntity.badRequest().body(Responses.builder().message("Employee Pan Card Number Already Exists").build());
		}
		if (roles != null) {
			employee.setRoles(roles);
			employee.setCreatedAt(new Date());
			employee.setSupervisor(employeeSupervisor);
			employee.setStatus(true);
			employee.setBranch(branch);
			employee.setDepartment(department);
			employee.setDesg(designation);
			employeeDao.save(employee);
			
			
			FamilyDetails familyDetails=new FamilyDetails();
			familyDetails.setEmployee(employee);
			familyDetailsDao.save(familyDetails);
			
			
			AdditionalDetails additionalDetails=new AdditionalDetails();
			additionalDetails.setEmployee(employee);
			additionalDetailsDao.save(additionalDetails);
			
			
			BankDetails bankDetails=new BankDetails();
			bankDetails.setEmployee(employee);
			bankDetailsDao.save(bankDetails);
			
			return ResponseEntity.ok(Responses.builder().message("Employee saved sucessfully").build());

		} else {
			//return ResponseEntity.ok("Error: Role is not found.!");
			return ResponseEntity.badRequest().body(Responses.builder().message("Error: Role is not  found.!").build());
		}

	}

	@PostMapping(value = "/sendOtp/{phoneNumber}/{signature}")
	public ResponseEntity<?> sendOtp(@PathVariable String phoneNumber,@PathVariable String signature) {
		System.out.println("Inside method");
		String otpNumber = "-1";
		 String message = "Please use this OTP to proceed"+signature ;

		try {

			// check for mobile and return if found
			Employee employee = employeeDao.findByPhoneNumber(phoneNumber);
			// send OTP thru SMS
			 //otpNumber = otpService.sendOtp(phoneNumber, message);

			otpNumber = "9999";
			if (employee != null) {
				if (employee.isStatus() == true) {

					// send OTP thru SMS
					// otpNumber = otpService.sendOtp(phoneNumber, message);

					OtpValidation otpval = otpvalidationDao.findBymobile(phoneNumber);
					if (otpval != null) {
						otpval.setOtp(otpNumber);
						otpval.setMobile(phoneNumber);
						otpval.setStatus("Otp Sent");
						otpval.setSignature(signature);
						otpval = otpvalidationDao.save(otpval);
					} else {
						OtpValidation otpval1 = new OtpValidation();
						otpval1.setOtp(otpNumber);
						System.out.println("current otp is" + otpNumber);
						otpval1.setMobile(phoneNumber);
						System.out.println("mobile number is" + phoneNumber);
						otpval1.setStatus("Otp Sent");
						System.out.println("mobile number is" + phoneNumber);
						otpval1.setSignature(signature);
						otpval1 = otpvalidationDao.save(otpval1);
					}

					//return ResponseEntity.ok("Otp Send Sucessfully");
					 return ResponseEntity.ok(Responses.builder().message("Otp Send Sucessfully").build());

				} else {

					//return ResponseEntity.ok("Your Account was Locked you cant login");
					 return ResponseEntity.badRequest().body(Responses.builder().message("Your Account was Locked you cant login").build());
					// return ResponseEntity.status(200).body("Your Account was locked you cant
					// login");
				}
			} else {

				//return ResponseEntity.ok("Mobile Number Doesnt Exist");
				  return ResponseEntity.badRequest().body(Responses.builder().message("Mobile Number Doesnt Exist").build());
				// return ResponseEntity.badRequest().body("Mobile Number Doesnt Exist");
			}

		} catch (Exception E) {

			E.printStackTrace();

			//return ResponseEntity.badRequest().body("Internal Server Error");
			return ResponseEntity.badRequest().body(Responses.builder().message("Internal Server Error").build());
		}

	}

	@PostMapping("/validateOtp/{phoneNumber}/{otpNum}")
	public ResponseEntity<?> validateOtp(@PathVariable String phoneNumber, @PathVariable String otpNum) {
		Employee employee = employeeDao.findByPhoneNumber(phoneNumber);
		int serverOtp = 9999;
		//int serverOtp = otpService.getOtp(phoneNumber);
		System.out.println("Pathvariable otp is" + otpNum);
		System.out.println("Server otp is" + serverOtp);
		int intOtp = Integer.parseInt(otpNum);
		System.out.println("intotp is" + intOtp);
		if (serverOtp > 0) {
			if (intOtp == serverOtp) {
				if (employeeDao.existsByStatus(true)) {
					otpService.clearOTP(phoneNumber);
					final UserDetails userDetails = userDetailsService.loadUserByUsername(employee.getPhoneNumber());
					final String jwt = jwtTokenUtil.generateToken(userDetails);
					return ResponseEntity.ok(new AuthenticationResponse(jwt, employee.getRoles().getRoleName(),employee.getEmployeeId()));
				} else {

					//return ResponseEntity.ok("Employee is Inactive");
					 return ResponseEntity.ok(Responses.builder().message("Employee is Inactive").build());
				}
			} else {

				//return ResponseEntity.ok("Enter valid otp");
				return ResponseEntity.badRequest().body(Responses.builder().message("Enter valid  otp").build());
			}
		} else {
			//return ResponseEntity.ok("Otp Time Expired");
			// return ResponseEntity.ok(Responses.builder().message("Otp Time Expired").build());
			 return ResponseEntity.badRequest().body(Responses.builder().message("Otp Time Expired").build());
		}

	}

	
	@PostMapping("/updateEmployee/{employeeId}/{supervisorId}")

	public ResponseEntity<?> updateEmployee(@RequestBody Employee employee,@PathVariable String supervisorId, @PathVariable String employeeId,@RequestParam(name = "desigName", required = false) String desigName,@RequestParam(name = "roleName", required = false) String roleName,@RequestParam(name = "branchName", required = false) String branchName,@RequestParam(name = "deptName", required = false) String deptName) 
	{

		Employee emp = employeeDao.findByEmployeeId(employeeId);
		Employee supervisor = employeeDao.findByEmployeeId(supervisorId);
		if(emp !=null)
		{
		
	    emp.setSupervisor(supervisor);
		emp.setDateOfJoining(employee.getDateOfJoining());
		emp.setFirstName(employee.getFirstName());
		emp.setLastName(employee.getLastName());
		emp.setGender(employee.getGender());
		emp.setDateOfbirth(employee.getDateOfbirth());
		emp.setPresentAddress(employee.getPresentAddress());
		emp.setPhoneNumber(employee.getPhoneNumber());
		emp.setOfficialEmailId(employee.getOfficialEmailId());
		emp.setPanCardNumber(employee.getPanCardNumber());
		emp.setAdharNumber(employee.getAdharNumber());
		emp.setExperiance(employee.getExperiance());
		emp.setLastModifiedDate(new Date());
		emp.setDeputeBranch(employee.getDeputeBranch());
		emp.setLocation(employee.getLocation());

		Designation desig= desigDao.findByDesigName(desigName);
		if(desig!=null)
		{
		emp.setDesg(desig);
		}
		else
		{
			 return ResponseEntity.badRequest().body(Responses.builder().message("Designation not found").build()); 
		}
		Roles role= rolesDao.findByroleName(roleName);
		if(role!=null)
		{
		emp.setRoles(role);
		}
		else
		{
			 return ResponseEntity.badRequest().body(Responses.builder().message(" Roles not found").build()); 
		}
		Branch branch= branchDao.findBybranchName(branchName);
		if(branch!=null)
		{
		emp.setBranch(branch);
		}
		else
		{
			 return ResponseEntity.badRequest().body(Responses.builder().message(" Branch not found").build()); 
		}
		Department dept= deptDao.findByDeptName(deptName);
		if(dept!=null)
		{
		emp.setDepartment(dept);
		}
		else
		{
			 return ResponseEntity.badRequest().body(Responses.builder().message("Department not found").build()); 
		}
		employeeDao.save(emp);
		// return ResponseEntity.ok(emp);
		//return ResponseEntity.ok("Updated Successfully.");
		return ResponseEntity.ok(Responses.builder().message("Employee Details Updated Successfully.").build());
		}else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("Employee ID not found").build()); 
		}
		

	}
	
	
	
	 @PostMapping("/updateEmp/{employeeId}/{supervisorId}")
	  public ResponseEntity<?> updateEmp(@RequestBody Employee employee, @PathVariable String employeeId,@PathVariable String supervisorId)
	  {
	  Employee emp = employeeDao.findByEmployeeId(employeeId);
	  Employee supervisor = employeeDao.findByEmployeeId(supervisorId);
		if(emp !=null)
		{
		
	    emp.setSupervisor(supervisor);
		emp.setDateOfJoining(employee.getDateOfJoining());
		emp.setFirstName(employee.getFirstName());
		emp.setLastName(employee.getLastName());
		emp.setQualification(employee.getQualification());
		emp.setDateOfbirth(employee.getDateOfbirth());
		emp.setGender(employee.getGender());
		emp.setPermanentAddress(employee.getPermanentAddress());
		emp.setPresentAddress(employee.getPresentAddress());
		emp.setPhoneNumber(employee.getPhoneNumber());
		emp.setEmergencyContact(employee.getEmergencyContact());
		emp.setPersonalEmailId(employee.getPersonalEmailId());
		emp.setOfficialEmailId(employee.getOfficialEmailId());
		emp.setStatus(employee.isStatus());
		emp.setNationality(employee.getNationality());
		emp.setMaritalStatus(employee.getMaritalStatus());
		emp.setBloodGroup(employee.getBloodGroup());
		emp.setExperiance(employee.getExperiance());
		emp.setPanCardNumber(employee.getPanCardNumber());
		emp.setAdharNumber(employee.getAdharNumber());
		emp.setDeputeBranch(employee.getDeputeBranch());
		emp.setLocation(employee.getLocation());
		employeeDao.save(emp);
		return ResponseEntity.ok(Responses.builder().message("Employee Details Updated Successfully.").build());
		}else {
			 return ResponseEntity.badRequest().body(Responses.builder().message("Employee ID not found").build()); 
		}
}
	
	
	
	@GetMapping("getEmployeeDetails")
	 public ResponseEntity<?> Employee() {
	 List<Employee> employee=employeeDao.findAll();
	 if(employee.size()>0) {
		 HashMap<String,List<Employee>> map=new HashMap<String, List<Employee>>();
		   map.put("Employee", employee);
		   return ResponseEntity.ok(map);
	 
	 } else 
	 { 
		 //return ResponseEntity.ok("Data not found."); 
		  return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build());
	  } 
	 }
	
	
	 @GetMapping("listOfEmployees")
	  public ResponseEntity<?> listOfEmployees(){
		  List<Employee> employee=employeeDao.findBysetStatus(true);
		  List<EmployeePojo> empPojo = new ArrayList<EmployeePojo>();
		  for(Employee emp : employee)
		  {
			  EmployeePojo employeePojo = new EmployeePojo();
			  employeePojo.setEmployeeId(emp.getEmployeeId());
			  String empName=emp.getFirstName() + " " + emp.getLastName();
			  employeePojo.setFirstName(empName);
			  employeePojo.setStatus(emp.isStatus());
			  empPojo.add(employeePojo);
		      
			  

		  }
		    HashMap<String,List<EmployeePojo>> map=new HashMap<String, List<EmployeePojo>>();
		    map.put("Employee", empPojo); 
		    return ResponseEntity.ok(map);
		  }
	
	 
	  @GetMapping("/getEmployeeManagerDetails")
		 public ResponseEntity<?>  getEmployeeManagerDetails()
		 {
			Roles role = rolesDao.findByRoleName("Manager");
			List<Employee> employeeList = employeeDao.findByRoles(role);
			//System.out.println(role.getRoleName());
			List<EmployeePojo> employeePojoList = new ArrayList<EmployeePojo>();
			for(Employee manager : employeeList)
			{
				EmployeePojo empPojo = new EmployeePojo();
				empPojo.setEmployeeId(manager.getEmployeeId());
				 String empName=manager.getFirstName() + " " + manager.getLastName();
				 empPojo.setFirstName(empName);
				employeePojoList.add(empPojo);
			}
			 HashMap<String,List<EmployeePojo>> map=new HashMap<String, List<EmployeePojo>>();
			   map.put("Managers", employeePojoList);
			   return ResponseEntity.ok(map);	
	 		
		 }
	 
	 
	
	 @GetMapping("/getEmployeeId/{employeeId}") 
	 public ResponseEntity<?> getEmployeeId(@PathVariable String employeeId)
    { 
		 //Employee employee=employeeDao.findByEmployeeId(employeeId);
		 Employee employee=employeeDao.findBysetStatus(true,employeeId);
		 
	   if(employee!=null) 
	 {
	   HashMap<String,Employee> map=new HashMap<String, Employee>();
	   map.put("Employee", employee);
	   return ResponseEntity.ok(map);	 
	 }
	 //return new ResponseEntity<Employee>(employee,HttpStatus.OK);
	   //return ResponseEntity.ok("Data not found.");
	   else 
		 { 
		   return ResponseEntity.badRequest().body(Responses.builder().message("ID not found").build());
		  } 
   }
	 
	 
	 @PostMapping("/deactiveEmployee/{employeeId}")
		public ResponseEntity<?> deactiveEmployee(@PathVariable String employeeId )
		{
			Employee emp = employeeDao.findByEmployeeId(employeeId);
			if(emp != null)
			{
				emp.setStatus(false);
				employeeDao.save(emp);
				return ResponseEntity.ok(Responses.builder().message("Employee Details saved Sucessfully").build());
			}
			else {
				 return ResponseEntity.badRequest().body(Responses.builder().message("Employee Id "+ " " +emp+ " "+ " Not Found ").build());
			}
		}
	 
	 
	 @PostMapping("/activeEmployee/{employeeId}")
		public ResponseEntity<?> activeEmployee(@PathVariable String employeeId )
		{
			Employee emp = employeeDao.findByEmployeeId(employeeId);
			if(emp != null)
			{
				emp.setStatus(true);
				employeeDao.save(emp);
				return ResponseEntity.ok(Responses.builder().message("Employee Details Activated Sucessfully").build());
			}
			else {
				 return ResponseEntity.badRequest().body(Responses.builder().message("Employee Id "+ " " +emp+ " "+ " Not Found ").build());
			}
		}
	 
	 

	   @GetMapping("/getProfile/{employeeId}")
	   public ResponseEntity<?> getProfile(@PathVariable String employeeId)
	   {
		   Employee emp= employeeDao.findBysetStatus(true, employeeId);
		   EmpPojo empPojo = new EmpPojo();
		   empPojo.setSupervisorId(emp.getSupervisor().getEmployeeId());
		   empPojo.setSupervisorName(emp.getSupervisor().getFirstName());
		   empPojo.setEmployeeId(emp.getEmployeeId());
		   empPojo.setFirstName(emp.getFirstName());
		   empPojo.setLastName(emp.getLastName());
		   empPojo.setPhoneNumber(emp.getPhoneNumber());
		   empPojo.setEmergencyContact(emp.getEmergencyContact());
		   empPojo.setPersonalEmailId(emp.getPersonalEmailId());
		   empPojo.setOfficialEmailId(emp.getOfficialEmailId());
		   empPojo.setGender(emp.getGender());
		   empPojo.setDateOfbirth(emp.getDateOfbirth());
		   empPojo.setStatus(emp.isStatus());
		   empPojo.setQualification(emp.getQualification());
		   empPojo.setExperiance(emp.getExperiance());
		   empPojo.setBloodGroup(emp.getBloodGroup());
		   empPojo.setNationality(emp.getNationality());
		   empPojo.setMaritalStatus(emp.getMaritalStatus());
		   empPojo.setAdharNumber(emp.getAdharNumber());
		   empPojo.setPanCardNumber(emp.getPanCardNumber());
		   empPojo.setDateOfJoining(emp.getDateOfJoining());
		   empPojo.setPermanentAddress(emp.getPermanentAddress());
		   empPojo.setPresentAddress(emp.getPresentAddress()); 
		   empPojo.setDeputeBranch(emp.getDeputeBranch());
		   empPojo.setLocation(emp.getLocation());   
		   
		   Roles roles = rolesDao.findByroleName(emp.getRoles().getRoleName());
		   RolesPojo rolePojo=new RolesPojo();
		   rolePojo.setRoleId(roles.getRoleId());
		   rolePojo.setRoleName(roles.getRoleName());
		   rolePojo.setDescription(roles.getDescription());
		   rolePojo.setStatus(roles.isStatus());
		   
		   
		   Branch branch=branchDao.findBybranchName(emp.getBranch().getBranchName());
		   BranchsPojo branchPojo=new BranchsPojo();
		   branchPojo.setBranchId(branch.getBranchId());
		   branchPojo.setBranchName(branch.getBranchName());
		   branchPojo.setCity(branch.getCity());
		   branchPojo.setState(branch.getCity());
		   branchPojo.setCountry(branch.getCountry());
		   branchPojo.setPhoneNumberOne(branch.getPhoneNumberOne());
		   branchPojo.setPhoneNumberTwo(branch.getPhoneNumberTwo());
		   branchPojo.setEmailId(branch.getEmailId());
		   branchPojo.setBranchPremisesRent(branch.getBranchPremisesRent());
		   branchPojo.setBranchPremisesType(branch.getBranchPremisesType());
		   branchPojo.setLastModifiedDate(branch.getLastModifiedDate());
		   branchPojo.setStatus(branch.getStatus());
		  
		   
		   Designation desig= desigDao.findByDesigName(emp.getDesg().getDesigName());
		   DesgPojo desgPojo=new DesgPojo();
		   desgPojo.setDesigId(desig.getDesigId());
		   desgPojo.setDesigName(desig.getDesigName());
		   desgPojo.setGrade(desig.getGrade());
		   desgPojo.setLevel(desig.getLevel());
		   
		   
		   Department dept = deptDao.findByDeptName(emp.getDepartment().getDeptName());
		   DeptPojo deptPojo=new DeptPojo();
		   deptPojo.setDeptId(dept.getDeptId());
		   deptPojo.setDeptName(dept.getDeptName());
		   
		   
		 
		   additionalDetailsPojo addDetailsPojo=new additionalDetailsPojo();
		   AdditionalDetails additionalDetails = additionalDetailsDao.findByEmployee(emp);
		   if(additionalDetails!=null)
		   {
		   
		   addDetailsPojo.setAdditionalDetailsId(additionalDetails.getAdditionalDetailsId());
		   addDetailsPojo.setAlternateNumber(additionalDetails.getAlternateNumber());
		   addDetailsPojo.setWeddingDay(additionalDetails.getWeddingDay());
		   addDetailsPojo.setPassportNumber(additionalDetails.getPassportNumber());
		   addDetailsPojo.setPhoto(additionalDetails.getPhoto());
		   addDetailsPojo.setPhotoDisplay(additionalDetails.getPhotoDisplay());
		   }
		   
		   
		   ProfilePojo profilePojo=new ProfilePojo();
		   profilePojo.setEmployee(empPojo);
		   profilePojo.setRoles(rolePojo);
		   profilePojo.setBranchs(branchPojo);
		   profilePojo.setDesg(desgPojo);
		   profilePojo.setDept(deptPojo);
		   profilePojo.setAdditionalDetails(addDetailsPojo);
		   
		   
		   HashMap<String,ProfilePojo> map=new HashMap<String, ProfilePojo>();
		   map.put("Profile", profilePojo);
		   return ResponseEntity.ok(map);
		   
		   }
		  
	   
	   
	   
	   @GetMapping("/getSupervisorNames")
		  public ResponseEntity<?> getSupervisorNames()
		  {
			  List<String> emp= employeeDao.findBySupervisorName();
			  HashMap<String,List<String>> map=new HashMap<String,List<String>>();
		      map.put("SupervisorNames", emp);
		      return ResponseEntity.ok(map);
			  
		  }
	   
	   
	   
	   
	   @GetMapping("/getEmployeeList/{supervisorId}")
	    public ResponseEntity<?> getEmployeeList(@PathVariable String supervisorId)
	    {
	    	 Employee employee=employeeDao.findByEmployeeId(supervisorId);
	    	 List<Employee> empDetails=employeeDao.findByEmployee(employee.getEmplyoeeCode());
	    	 List<EmpListPojo> empList = new ArrayList<EmpListPojo>();
	    	 for (Employee emp : empDetails)
	    	 {
	    		 EmpListPojo listPojo=new EmpListPojo();
	    		 listPojo.setEmployeeId(emp.getEmployeeId());
	    		 String empName=emp.getFirstName() + " " + emp.getLastName();
	    		 listPojo.setEmpName(empName);
	    		 empList.add(listPojo);
	    	 }
	    	
	    	 HashMap<String, List<EmpListPojo>> map = new HashMap<String,List<EmpListPojo>>(); 
	    	 map.put("EmployeeList",empList); 
	    	 return ResponseEntity.ok(map);
	    			 
	    }
	  
} 
	  
	   

