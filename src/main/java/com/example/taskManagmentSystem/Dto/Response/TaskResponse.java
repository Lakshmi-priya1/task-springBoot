package com.example.taskManagmentSystem.Dto.Response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.taskManagmentSystem.Payload.TaskPriority;
import com.example.taskManagmentSystem.Payload.TaskStatus;

import lombok.Data;


@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;

    private Long milestoneId;
    private String milestoneName;
    
    private Long projectId;
    private String projectName;

    private List<Long> employeeIds;
    private List<String> employeeNames;
    
}
