package com.example.taskManagmentSystem.Dto.Request;

import java.time.LocalDateTime;

import com.example.taskManagmentSystem.Payload.MilestoneStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MilestoneRequest {
    @NotBlank(message = "Milestone name is required")
    private String milestoneName;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Status is required")
    private MilestoneStatus status;
    
    @Future(message = "Due date must be in the future") 
    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate; 
    private Long projectId;
    
    private String projectName;
    
}
