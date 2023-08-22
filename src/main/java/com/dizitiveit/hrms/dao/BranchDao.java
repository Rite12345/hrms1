package com.dizitiveit.hrms.dao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.dizitiveit.hrms.model.Branch;

public interface BranchDao extends JpaRepository<Branch, Integer> {

    Branch findBybranchId(Integer branchId);
	
	Branch findBybranchName(String branchName);
	
	Boolean existsByBranchName(String branchName);
	
	Boolean existsByBranchId(int branchId);
	
	 @Query(value = "select branch_name FROM branch ", nativeQuery = true)
	 List<String> findBybranchName();
	 
	 
	 @Query(value = "select * FROM dems.branch where status=?1", nativeQuery = true)
		List<Branch> findBysetStatus(boolean status);
	 
	 
	 @Query(value = "select * FROM dems.branch order by branch_id asc", nativeQuery = true)
	 List<Branch> findAll();
}
