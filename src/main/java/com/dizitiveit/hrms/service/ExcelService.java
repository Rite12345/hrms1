package com.dizitiveit.hrms.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dizitiveit.hrms.Reports.ExcelGenerator;
import com.dizitiveit.hrms.dao.UnsavedPayrollDataDao;
import com.dizitiveit.hrms.model.UnsavedPayrollData;

@Service
public class ExcelService {

	@Autowired
	private UnsavedPayrollDataDao unsavedPayrollDataDao;
	
public ByteArrayInputStream load(int month,int year) {
		
	    List<UnsavedPayrollData> unsavedPayrollList = unsavedPayrollDataDao.findBymonthAndyear(month,year);
         System.out.println(unsavedPayrollList.size());
	    ByteArrayInputStream in = ExcelGenerator.transactionsToExcel(unsavedPayrollList);
	   
	    return in;
	  }
	
}
