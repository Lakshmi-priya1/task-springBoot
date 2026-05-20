package com.example.taskManagmentSystem.Dto.Response;
import lombok.Data;

@Data
public class LoginResponse {
      private Long userId;
    private String name;
    private String email;
    private String role;   // ← React will read this to show/hide UI
    private String token;
    
}
