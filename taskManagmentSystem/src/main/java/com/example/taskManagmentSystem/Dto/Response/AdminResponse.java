package com.example.taskManagmentSystem.Dto.Response;

import lombok.Data;
@Data
public class AdminResponse {

    private Long adminId;  
    private String email;
    private String token;
    
}
