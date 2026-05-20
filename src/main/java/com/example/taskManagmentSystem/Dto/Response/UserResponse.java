package com.example.taskManagmentSystem.Dto.Response;
import lombok.Data;


@Data
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String role;
    
}
