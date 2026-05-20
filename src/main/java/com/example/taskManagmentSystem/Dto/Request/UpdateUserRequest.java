package com.example.taskManagmentSystem.Dto.Request;
import com.example.taskManagmentSystem.Payload.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank
    private String name;

    @NotBlank @Email
    private String email;

    private String password; // Optional for updates

    @NotNull
    private Role role;
    
}