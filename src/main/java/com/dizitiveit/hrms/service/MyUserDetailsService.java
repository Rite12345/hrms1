package com.dizitiveit.hrms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.MyUserDetails;

@Service
public class MyUserDetailsService implements UserDetailsService{

	/*
	 * @Autowired private PasswordEncoder passwordEncoder;
	 */
	
	@Autowired
	private EmployeeDao employeeDao;
	
	
	
	 @Override
	 public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
	 {
		// Optional<Employee> employeeDetails = employeeDao.findByEmailIdOrEmpCode(userName, userName);
		 Optional<Employee> employeeDetails = employeeDao.findByOfficialEmailIdOrPhoneNumber(userName, userName);
		 
		 // System.out.println(employeeDetails.toString());
		  System.out.println("vere");
		  employeeDetails.orElseThrow(() -> new UsernameNotFoundException("Not found: " +
		  userName));
		  
		  return employeeDetails.map(MyUserDetails::new).get();
	 }
}
