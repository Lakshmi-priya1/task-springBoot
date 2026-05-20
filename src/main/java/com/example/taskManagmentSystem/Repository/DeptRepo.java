package com.example.taskManagmentSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.taskManagmentSystem.Model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface DeptRepo extends JpaRepository<Department, Long> {
    @Query("""
        SELECT d FROM Department d
         WHERE(
            :keyword IS NULL OR
            LOWER(d.deptName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
        """)
        Page<Department> searchAndFilter(String keyword, Pageable pageable);  
}
    
    

