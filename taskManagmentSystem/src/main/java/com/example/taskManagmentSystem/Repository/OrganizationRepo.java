package com.example.taskManagmentSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.taskManagmentSystem.Model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;

public interface OrganizationRepo extends JpaRepository<Organization, Long> {
    @Query("""
            SELECT o FROM Organization o
            WHERE(
            :keyword IS NULL OR
            LOWER(o.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
            
            
            Page<Organization> searchAndFilter(String keyword, Pageable pageable);  
}
