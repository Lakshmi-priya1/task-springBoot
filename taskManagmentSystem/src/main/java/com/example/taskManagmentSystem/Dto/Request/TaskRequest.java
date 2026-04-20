package com.example.taskManagmentSystem.Dto.Request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private String status;
    private String priority;

    private LocalDateTime dueDate;
    
    private Long employeeId;

    private Long projectId;
    
}
