package com.example.taskManagmentSystem.Dto.Response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.taskManagmentSystem.Payload.MilestoneStatus;
import com.example.taskManagmentSystem.Payload.TaskStatus;

import lombok.Data;

@Data
public class MilestoneResponse {
    private Long milestoneId;
    private String milestoneName;
    private String description;
    private MilestoneStatus status;
    private LocalDateTime dueDate;
    private Long projectId;
    private String projectName;
    private List<Long> taskIds;
    private List<String> taskTitles;
    private List<TaskStatus> taskStatuses;
    private List<Long> employeeIds;
    private List<String> employeeNames;
    

}
