package com.example.taskManagmentSystem.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class DeptRequest {
    @NotBlank(message = "Department name is required")
    private String deptName;
    @NotBlank(message = "Department description is required")
    private String deptDescription;
    
}
