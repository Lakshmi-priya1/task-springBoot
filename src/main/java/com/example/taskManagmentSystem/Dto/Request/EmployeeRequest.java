package com.example.taskManagmentSystem.Dto.Request;

import com.example.taskManagmentSystem.Payload.EmployeeStatus;
import com.example.taskManagmentSystem.Payload.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data

public class EmployeeRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be 3 to 20 characters")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Department is required")
    private String department;
    @NotNull(message = "Status is required")
    private EmployeeStatus status;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters")
    private String password;
    @NotNull(message = "Role is required")
    private Role role;
    private Long orgId;
    private String companyName;
    
}
