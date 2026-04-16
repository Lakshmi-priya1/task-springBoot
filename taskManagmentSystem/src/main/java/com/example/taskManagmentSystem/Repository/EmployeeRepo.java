package com.example.taskManagmentSystem.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.taskManagmentSystem.Model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
      @Query("""
        SELECT e FROM Employee e
        WHERE (:keyword IS NULL OR
               LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
               LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:department IS NULL OR e.department = :department)
    """)
    Page<Employee> searchAndFilter(String keyword, String department, Pageable pageable);
    
}
