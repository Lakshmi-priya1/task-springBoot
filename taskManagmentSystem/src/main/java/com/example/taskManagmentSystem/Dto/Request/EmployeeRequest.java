package com.example.taskManagmentSystem.Dto.Request;

import lombok.Data;

@Data
public class EmployeeRequest {
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
