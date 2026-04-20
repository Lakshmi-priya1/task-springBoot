package com.example.taskManagmentSystem.Controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskManagmentSystem.Dto.Request.ProjectRequest;
import com.example.taskManagmentSystem.Dto.Response.ProjectResponse;
import com.example.taskManagmentSystem.Service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    @GetMapping("/all")
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }
    @GetMapping("/all/{projectId}")
    public ProjectResponse getProjectById(@PathVariable Long projectId) {
        return projectService.getProjectById(projectId);
    } 
    @PostMapping("/add")
    public ProjectResponse addProject(@RequestBody ProjectRequest request) {
        return projectService.createProject(request);
    }
    @PostMapping("/{projectId}/employees/{employeeId}")
public String assignEmployeeToProject(
        @PathVariable Long projectId,
        @PathVariable Long employeeId) {

    projectService.assignEmployeeToProject(projectId, employeeId);
    return "Employee assigned to project successfully";
}
    @PostMapping("/{projectId}/tasks/{taskId}")
    public String assignTaskToProject(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {

        projectService.assignTaskToProject(projectId, taskId);
        return "Task assigned to project successfully";
    }
    @PutMapping("/update/{projectId}")
    public ProjectResponse updateProject(@PathVariable Long projectId, @RequestBody ProjectRequest request) {
         return projectService.updateProject(projectId, request);
    }
    @DeleteMapping("/delete/{projectId}")
    public String deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return "Project deleted successfully";
    }
    @DeleteMapping("/{projectId}/employees/{employeeId}")
public String removeEmployeeFromProject(
        @PathVariable Long projectId,
        @PathVariable Long employeeId) {

    projectService.removeEmployeeFromProject(projectId, employeeId);
    return "Employee removed from project";
}
    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public String removeTaskFromProject(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {

        projectService.removeTaskFromProject(projectId, taskId);
        return "Task removed from project";
    }

    @GetMapping
    public Page<ProjectResponse> searchAndFilterProjects(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        return
         projectService.searchAndFilterProjects(keyword, status , page, size);
    }
}
