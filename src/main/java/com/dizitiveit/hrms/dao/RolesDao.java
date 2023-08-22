package com.dizitiveit.hrms.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.Roles;
import org.springframework.data.jpa.repository.Query;

public interface RolesDao extends JpaRepository<Roles,Long>{


	
	Roles findByroleId(long roleId);
    Boolean existsByRoleName(String roleName);
	Roles findByRoleName(String roleName);
	Roles deleteById(long roleId);
	Boolean existsByRoleId(long roleId);
	
	@Query(value = "select *  FROM roles where role_name=?1 ", nativeQuery = true)
	   Roles findByroleName(String Manager);
	
	 @Query(value = "select role_name FROM roles ", nativeQuery = true)
	 List<String> findByroleName();
	
	
	@Query(value = "select * FROM dems.roles order by role_id asc", nativeQuery = true)
	 List<Roles> findAll();
	
}
