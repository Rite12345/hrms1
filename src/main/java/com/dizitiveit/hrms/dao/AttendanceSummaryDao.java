package com.dizitiveit.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizitiveit.hrms.model.AttendanceSummary;

public interface AttendanceSummaryDao extends JpaRepository<AttendanceSummary, Integer> {

}
