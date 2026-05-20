package com.example.taskManagmentSystem.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.taskManagmentSystem.Model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
       @Query(
        value = """
            SELECT e FROM Employee e
            WHERE (:keyword IS NULL OR
                   LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                   LOWER(e.email)     LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND   (:department IS NULL OR e.department = :department)
            AND    e.deleted = false
        """,
        countQuery = """
            SELECT COUNT(e) FROM Employee e
            WHERE (:keyword IS NULL OR
                   LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                   LOWER(e.email)     LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND   (:department IS NULL OR e.department = :department)
            AND    e.deleted = false
        """
    )
    Page<Employee> searchAndFilter(
        @Param("keyword")    String keyword,
        @Param("department") String department,
        Pageable             pageable
    );
    
}
