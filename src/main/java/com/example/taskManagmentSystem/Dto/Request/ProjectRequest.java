package com.example.taskManagmentSystem.Dto.Request;

import java.time.LocalDateTime;
import java.util.List;

import com.example.taskManagmentSystem.Payload.ProjectStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 

public class ProjectRequest {
    @NotBlank(message = "Project name is required")
    private String projectName;
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
    @NotNull(message = "Status is required")
    private ProjectStatus status;
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    @Future(message = "End date must be in the future")
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    private List<Long> employeeIds;
}
