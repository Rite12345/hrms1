package com.dizitiveit.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.model.EmployeeDocument;

public interface EmployeeDocumentDao extends JpaRepository<EmployeeDocument,Long>{

}
