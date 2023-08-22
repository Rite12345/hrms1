package com.dizitiveit.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.service.ExcelService;

@RequestMapping("/reports")
@RestController
public class ReportsController {


	@Autowired
	private ExcelService excelService;

	  @GetMapping("/getUnsavedPayRollReports/{month}/{year}")
	  public ResponseEntity<Resource> getUnsavedPayRollReports(@PathVariable int month,@PathVariable int year) {
	    String filename = "UnsavedPayRoll.xlsx";
	    InputStreamResource file = new InputStreamResource(excelService.load(month,year));
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	  }
	
}
