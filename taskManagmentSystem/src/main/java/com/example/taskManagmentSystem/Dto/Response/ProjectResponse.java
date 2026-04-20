package com.example.taskManagmentSystem.Dto.Response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ProjectResponse {

    private Long projectId;

    private String projectName;
    private String description;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> employeeIds;
    private List<Long> taskIds;
    private List<String> employeeFirstNames;
    private List<String> taskTitles;
}
