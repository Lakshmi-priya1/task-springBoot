package com.example.taskManagmentSystem.Util;

import java.util.List;

import com.example.taskManagmentSystem.Dto.Request.ProjectRequest;
import com.example.taskManagmentSystem.Dto.Response.ProjectResponse;
import com.example.taskManagmentSystem.Model.Project;

public final class ProjectMapper {
    private ProjectMapper() {
    }

    public static Project mapToEntity(ProjectRequest request) {

        Project project = new Project();

        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        return project;
    }

    public static ProjectResponse mapToDto(Project project) {

        ProjectResponse dto = new ProjectResponse();

        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());

        // Employees
        dto.setEmployeeIds(
            project.getEmployees() == null
                ? List.of()
                : project.getEmployees()
                    .stream()
                    .map(emp -> emp.getEmployeeId())
                    .toList()
        );

        dto.setEmployeeFirstNames(
            project.getEmployees() == null
                ? List.of()
                : project.getEmployees()
                    .stream()
                    .map(emp -> emp.getFirstName())
                    .toList()
        );

        // Milestones
        dto.setMilestoneIds(
            project.getMilestones() == null
                ? List.of()
                : project.getMilestones()
                    .stream()
                    .map(m -> m.getMilestoneId())
                    .toList()
        );

        dto.setMilestoneNames(
            project.getMilestones() == null
                ? List.of()
                : project.getMilestones()
                    .stream()
                    .map(m -> m.getMilestoneName())
                    .toList()
        );

        // Tasks (across all milestones)
        List<com.example.taskManagmentSystem.Model.Task> allTasks = project.getMilestones() == null
            ? List.of()
            : project.getMilestones().stream()
                .filter(m -> m.getTasks() != null)
                .flatMap(m -> m.getTasks().stream())
                .toList();

        dto.setTaskIds(allTasks.stream().map(t -> t.getId()).toList());
        dto.setTaskTitles(allTasks.stream().map(t -> t.getTitle()).toList());

        return dto;
    }
}