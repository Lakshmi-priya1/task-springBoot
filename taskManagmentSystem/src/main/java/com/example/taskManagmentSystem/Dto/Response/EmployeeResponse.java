package com.example.taskManagmentSystem.Dto.Response;

import lombok.Data;

@Data
public class EmployeeResponse {
    
    private Long employeeId;
    private String username;
    private String email;
    private String FirstName;
    private String LastName;
    private String department;

    private String status;
    
    private String phoneNumber;
    private String password; 
    
    private Long orgId;

    
}
