package com.example.taskManagmentSystem.Dto.Request;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class EmployeeRequest {
    @Id
    
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String department;
    private String status;
    private String phoneNumber;
    private String password; 
    private Long orgId;
    
}
