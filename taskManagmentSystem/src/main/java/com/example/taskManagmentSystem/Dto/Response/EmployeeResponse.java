package com.example.taskManagmentSystem.Dto.Response;

import com.example.taskManagmentSystem.Payload.EmployeeStatus;

import lombok.Data;

@Data
public class EmployeeResponse {
    
    private Long employeeId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String department;

    private EmployeeStatus status;
    
    private String phoneNumber;
    private String password; 
    
    private Long orgId;

    
}
