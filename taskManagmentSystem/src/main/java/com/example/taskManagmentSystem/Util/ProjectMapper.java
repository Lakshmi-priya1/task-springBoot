package com.example.taskManagmentSystem.Util;

import com.example.taskManagmentSystem.Dto.Request.ProjectRequest;
import com.example.taskManagmentSystem.Dto.Response.ProjectResponse;
import com.example.taskManagmentSystem.Model.Project;

public final class ProjectMapper {
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
        if (project.getEmployees() != null && !project.getEmployees().isEmpty()) {
    dto.setEmployeeIds(
        project.getEmployees().stream()
               .map(employee -> employee.getEmployeeId())
               .toList()
    );

    dto.setEmployeeFirstNames(
        project.getEmployees().stream()
               .map(employee -> employee.getFirstName())
               .toList()
    );
}
        if (project.getTasks() != null && !project.getTasks().isEmpty()) {
    dto.setTaskIds(
        project.getTasks().stream()
               .map(task -> task.getId())
               .toList()
    );

    dto.setTaskTitles(
        project.getTasks().stream()
               .map(task -> task.getTitle())
               .toList()
    );
}

    
if (project.getMilestones() != null && !project.getMilestones().isEmpty()) {

    dto.setMilestoneIds(
        project.getMilestones().stream()
               .map(milestone -> milestone.getMilestoneId())
               .toList()
    );

    dto.setMilestoneNames(
        project.getMilestones().stream()
               .map(milestone -> milestone.getMilestoneName())
               .toList()
    );
}
       return dto;
    }
}