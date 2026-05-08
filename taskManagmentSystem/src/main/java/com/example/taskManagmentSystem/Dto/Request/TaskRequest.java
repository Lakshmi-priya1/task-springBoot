package com.example.taskManagmentSystem.Dto.Request;

import java.time.LocalDateTime;
import com.example.taskManagmentSystem.Payload.TaskPriority;
import com.example.taskManagmentSystem.Payload.TaskStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
    @NotNull(message = "Status is required")
    private TaskStatus status;
    @NotNull(message = "Priority is required")
    private TaskPriority priority;
    @Future(message = "Due date must be in the future")
    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;
    
    private Long employeeId;

    @NotNull(message = "Milestone is required")
private Long milestoneId;
}
