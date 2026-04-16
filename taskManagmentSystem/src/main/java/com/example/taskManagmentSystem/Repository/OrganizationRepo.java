package com.example.taskManagmentSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.taskManagmentSystem.Model.Organization;

public interface OrganizationRepo extends JpaRepository<Organization, Long> {
    
}
