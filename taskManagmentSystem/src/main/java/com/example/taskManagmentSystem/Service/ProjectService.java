package com.example.taskManagmentSystem.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.taskManagmentSystem.Dto.Request.ProjectRequest;
import com.example.taskManagmentSystem.Dto.Response.ProjectResponse;
import com.example.taskManagmentSystem.Payload.ProjectStatus;

public interface ProjectService {
     ProjectResponse createProject(ProjectRequest request);
    List<ProjectResponse> getAllProjects();
    ProjectResponse getProjectById(Long projectId);
    ProjectResponse updateProject(Long projectId, ProjectRequest request);
    void deleteProject(Long projectId);

    // ✅ KEEP - employee assignment at project level
    void assignEmployeeToProject(Long projectId, Long employeeId);
    void removeEmployeeFromProject(Long projectId, Long employeeId);


    Page<ProjectResponse> searchAndFilterProjects(
        String keyword,
        ProjectStatus status,
        int page,
        int size,
        String sortBy,
        String direction
    );
}