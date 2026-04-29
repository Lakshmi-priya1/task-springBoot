package com.example.taskManagmentSystem.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.taskManagmentSystem.Dto.Request.ProjectRequest;
import com.example.taskManagmentSystem.Dto.Response.ProjectResponse;
import com.example.taskManagmentSystem.Payload.ProjectStatus;

public interface ProjectService {
    List<ProjectResponse> getAllProjects();
    ProjectResponse getProjectById(Long projectId);
    
    ProjectResponse createProject(ProjectRequest request);
    ProjectResponse updateProject(Long projectId, ProjectRequest request);
    void deleteProject(Long projectId);
    void assignEmployeeToProject(Long projectId, Long employeeId);
    void removeEmployeeFromProject(Long projectId, Long employeeId);
    void assignTaskToProject(Long projectId, Long taskId);
    void removeTaskFromProject(Long projectId, Long taskId);
    Page<ProjectResponse> searchAndFilterProjects(String keyword, ProjectStatus status, int page, int size, String sortBy, String direction); 
}
