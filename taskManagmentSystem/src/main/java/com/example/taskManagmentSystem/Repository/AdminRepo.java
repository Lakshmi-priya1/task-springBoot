package com.example.taskManagmentSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskManagmentSystem.Model.Admin;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
    
}
