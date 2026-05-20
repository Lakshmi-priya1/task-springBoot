package com.example.taskManagmentSystem.Util;


import com.example.taskManagmentSystem.Dto.Request.MilestoneRequest;
import com.example.taskManagmentSystem.Dto.Response.MilestoneResponse;
import com.example.taskManagmentSystem.Model.Milestone;
import java.util.Collections;

import java.util.stream.Collectors;

public final class MilestoneMapper {
     public static Milestone mapToEntity(MilestoneRequest dto) {
        Milestone milestone = new Milestone();
        milestone.setMilestoneName(dto.getMilestoneName());
        milestone.setDescription(dto.getDescription());
        milestone.setStatus(dto.getStatus());
        milestone.setDueDate(dto.getDueDate());
        return milestone;
        
    }

     public static MilestoneResponse mapToDto(Milestone milestone) {
        MilestoneResponse dto = new MilestoneResponse();
        dto.setMilestoneId(milestone.getMilestoneId());
        dto.setMilestoneName(milestone.getMilestoneName());
        dto.setDescription(milestone.getDescription());
        dto.setDueDate(milestone.getDueDate());
        dto.setStatus(milestone.getStatus());
        dto.setProjectId(milestone.getProject().getProjectId());
        dto.setProjectName(milestone.getProject().getProjectName());
          if (milestone.getTasks() != null && !milestone.getTasks().isEmpty()) {
            dto.setTaskIds(
                milestone.getTasks().stream()
                    .map(task -> task.getId())
                    .collect(Collectors.toList())
            );
            dto.setTaskTitles(
                milestone.getTasks().stream()
                    .map(task -> task.getTitle())
                    .collect(Collectors.toList())
            );
            dto.setTaskStatuses(
                milestone.getTasks().stream()
                    .map(task -> task.getStatus())
                    .collect(Collectors.toList())
            );
        } else {
            dto.setTaskIds(Collections.emptyList());
            dto.setTaskTitles(Collections.emptyList());
            dto.setTaskStatuses(Collections.emptyList());
        }

        // Employees
        if (milestone.getEmployees() != null && !milestone.getEmployees().isEmpty()) {
            dto.setEmployeeIds(
                milestone.getEmployees().stream()
                    .map(e -> e.getEmployeeId())
                    .collect(Collectors.toList())
            );
            dto.setEmployeeNames(
                milestone.getEmployees().stream()
                    .map(e -> e.getFirstName() + " " + e.getLastName())
                    .collect(Collectors.toList())
            );
        } else {
            dto.setEmployeeIds(Collections.emptyList());
            dto.setEmployeeNames(Collections.emptyList());
        }

        return dto;
    }
}
