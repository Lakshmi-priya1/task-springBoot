package com.example.taskManagmentSystem.Dto.Response;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;

    private Long employeeId;
    
}
