package com.example.taskManagmentSystem.Util;

import com.example.taskManagmentSystem.Dto.Request.TaskRequest;
import com.example.taskManagmentSystem.Dto.Response.TaskResponse;
import com.example.taskManagmentSystem.Model.Task;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final  class TaskMapper {

    public static Task mapToEntity(TaskRequest dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        return task;
    }

    public static TaskResponse mapToDTO(Task task) {
        TaskResponse dto = new TaskResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
         if (task.getEmployees() != null && !task.getEmployees().isEmpty()) {
            List<Long> ids = task.getEmployees()
                .stream()
                .map(e -> e.getEmployeeId())
                .collect(Collectors.toList());

            List<String> names = task.getEmployees()
                .stream()
                .map(e -> e.getFirstName() + " " + e.getLastName())
                .collect(Collectors.toList());

            dto.setEmployeeIds(ids);
            dto.setEmployeeNames(names);
        } else {
            dto.setEmployeeIds(Collections.emptyList());
            dto.setEmployeeNames(Collections.emptyList());
        }

        // Milestone info
        if (task.getMilestone() != null) {
            dto.setMilestoneId(task.getMilestone().getMilestoneId());
            dto.setMilestoneName(task.getMilestone().getMilestoneName());

            // Project info via milestone
            if (task.getMilestone().getProject() != null) {
                dto.setProjectId(task.getMilestone().getProject().getProjectId());
                dto.setProjectName(task.getMilestone().getProject().getProjectName());
            }
        }

        return dto;
    }
}