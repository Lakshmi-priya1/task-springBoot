package com.example.taskManagmentSystem.Service.Implimentation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.ProjectRequest;
import com.example.taskManagmentSystem.Dto.Response.ProjectResponse;
import com.example.taskManagmentSystem.Model.Employee;
import com.example.taskManagmentSystem.Model.Project;
import com.example.taskManagmentSystem.Model.Task;
import com.example.taskManagmentSystem.Payload.ProjectStatus;
import com.example.taskManagmentSystem.Repository.EmployeeRepo;
import com.example.taskManagmentSystem.Repository.ProjectRepo;
import com.example.taskManagmentSystem.Repository.TaskRepo;
import com.example.taskManagmentSystem.Service.ProjectService;
import com.example.taskManagmentSystem.Util.ProjectMapper;

@Service
public class ProjectServiceImpl implements ProjectService {
    
    private final ProjectRepo projectRepo;
    private final EmployeeRepo employeeRepo; 
    private final TaskRepo taskRepo; 
    public ProjectServiceImpl(ProjectRepo projectRepo, EmployeeRepo employeeRepo, TaskRepo taskRepo) {
        this.projectRepo = projectRepo;
        this.employeeRepo = employeeRepo;
        this.taskRepo = taskRepo;
    }
    @Override
    public List<ProjectResponse> getAllProjects() {
        return projectRepo.findAll()
                .stream()
                .map(ProjectMapper::mapToDto)
                .toList();
    }
    @Override
    public ProjectResponse getProjectById(Long projectId) {
        return ProjectMapper.mapToDto(projectRepo.findById(projectId).orElse(null));
    }
    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        Project project = ProjectMapper.mapToEntity(request);
        Project saved = projectRepo.save(project);
        return ProjectMapper.mapToDto(saved);
    }
    @Override
    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {  
        Project project = projectRepo.findById(projectId).orElse(null);
        if (project != null) {
            project.setProjectName(request.getProjectName());
            project.setDescription(request.getDescription());
            project.setStatus(request.getStatus());
            project.setStartDate(request.getStartDate());
            project.setEndDate(request.getEndDate());
            return ProjectMapper.mapToDto(projectRepo.save(project));
        }
        return null;
    }
    @Override
    public void deleteProject(Long projectId) {
        projectRepo.deleteById(projectId);
    }   

    @Override
public void assignEmployeeToProject(Long projectId, Long employeeId) {

    Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    Employee employee = employeeRepo.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    if (!project.getEmployees().contains(employee)) {
        project.getEmployees().add(employee);
    }

    if (!employee.getProjects().contains(project)) {
        employee.getProjects().add(project);
    }

    projectRepo.save(project);
}
    @Override
public void removeEmployeeFromProject(Long projectId, Long employeeId) {

    Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    Employee employee = employeeRepo.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    project.getEmployees().remove(employee);
    employee.getProjects().remove(project);

    projectRepo.save(project);
}
    @Override
public void assignTaskToProject(Long projectId, Long taskId) {

    Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    Task task = taskRepo.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    task.setProject(project); 

    taskRepo.save(task);
}
   @Override
public void removeTaskFromProject(Long projectId, Long taskId) {

    Task task = taskRepo.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    task.setProject(null); 



    taskRepo.save(task);
}
    @Override
    public Page<ProjectResponse> searchAndFilterProjects(String keyword, ProjectStatus status, int page, int size, String sortBy, String direction) {
       
    if (keyword != null && keyword.trim().isEmpty()) {
        keyword = null;
    }

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    PageRequest pageable = PageRequest.of(page, size, sort);
        Page <Project> projectPage = projectRepo.searchAndFilter(keyword, status, pageable);
        return projectPage.map(ProjectMapper::mapToDto);
    }
}
