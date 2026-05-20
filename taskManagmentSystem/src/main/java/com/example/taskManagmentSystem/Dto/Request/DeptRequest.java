package com.example.taskManagmentSystem.Dto.Request;

@Data
public class DeptRequest {
    @NotBlank(message = "Department name is required")
    private String deptName;
    private String deptDescription;
    
}
