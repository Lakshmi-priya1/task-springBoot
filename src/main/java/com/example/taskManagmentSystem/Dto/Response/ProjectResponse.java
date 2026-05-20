package com.example.taskManagmentSystem.Dto.Response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.taskManagmentSystem.Payload.ProjectStatus;

import lombok.Data;

@Data
public class ProjectResponse {

    private Long projectId;

    private String projectName;
    private String description;
    private ProjectStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> employeeIds;
    private List<Long> taskIds;
    private List<String> employeeFirstNames;
    private List<String> taskTitles;
    private List<Long> milestoneIds;
    private List<String> milestoneNames;
}
