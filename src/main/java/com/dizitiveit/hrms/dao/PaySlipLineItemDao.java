package com.dizitiveit.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dizitiveit.hrms.model.MonthlySalarySummary;
import com.dizitiveit.hrms.model.PaySlipLineItem;

public interface PaySlipLineItemDao extends JpaRepository<PaySlipLineItem, Integer> {
	
	@Query(value = "select * FROM pay_slip_line_item where monthly_salary_summary_monthly_salaryid=?1 and current_month=?2 and current_year=?3 and item_type=?4", nativeQuery = true)
	 List<PaySlipLineItem> findByItemtype(long monthlySalarySummaryId,String currentMonth,int currentYear,boolean itemType);
	 
	 @Query(value = "select * FROM pay_slip_line_item where monthly_salary_summary_monthly_salaryid=?1 and current_month=?2 and current_year=?3 and item_type=?4", nativeQuery = true)
	 List<PaySlipLineItem> findByItemType(long monthlySalarySummaryId,String currentMonth,long currentYear,boolean itemType);
	 
	 @Query(value = "select * FROM pay_slip_line_item where current_month=?1 and current_year=?2 ", nativeQuery = true)
	 List<PaySlipLineItem> findByMonthAndYear(String currentMonth,int currentYear);
	 
	
	 @Query(value = "select item_details FROM dems.pay_slip_line_item", nativeQuery = true)
	 List<String> findByItemDetails();
	
	 @Query(value = "select * FROM pay_slip_line_item where  current_month=?1 and current_year=?2 and monthly_salary_summary_monthly_salaryid=?3", nativeQuery = true)
	 List<PaySlipLineItem> findByExistingPayslipLineItem(String currentMonth,int currentYear,long monthlySalarySummaryMonthlySalaryId);
	
	 @Query(value = "select * FROM pay_slip_line_item where item_details=?1 and current_month=?2 and current_year=?3 and monthly_salary_summary_monthly_salaryid=?4", nativeQuery = true)
	 PaySlipLineItem findByItemDetails(String itemDetails,String currentMonth,int currentYear,long monthlySalarySummary);
	 
	 List<PaySlipLineItem> findByMonthlySalarySummary(MonthlySalarySummary monthlySalarySummary);

	 @Query(value = "select *  FROM pay_slip_line_item  where monthly_salary_summary_monthly_salaryid=?1 ", nativeQuery = true)
	 List<PaySlipLineItem> findByMonthlySalarySummary(long monthlySalarySummaryId);
		

}
