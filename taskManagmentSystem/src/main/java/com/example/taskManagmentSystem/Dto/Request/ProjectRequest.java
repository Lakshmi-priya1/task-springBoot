package com.example.taskManagmentSystem.Dto.Request;

import java.time.LocalDateTime;

import lombok.Data;

@Data 

public class ProjectRequest {
    private String projectName;
    private String description;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
