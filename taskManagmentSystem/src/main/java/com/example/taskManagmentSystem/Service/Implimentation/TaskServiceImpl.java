package com.example.taskManagmentSystem.Service.Implimentation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.TaskRequest;
import com.example.taskManagmentSystem.Dto.Response.TaskResponse;
import com.example.taskManagmentSystem.Model.Employee;
import com.example.taskManagmentSystem.Model.Project;
import com.example.taskManagmentSystem.Model.Task;
import com.example.taskManagmentSystem.Repository.EmployeeRepo;
import com.example.taskManagmentSystem.Repository.ProjectRepo;
import com.example.taskManagmentSystem.Repository.TaskRepo;
import com.example.taskManagmentSystem.Service.TaskService;
import com.example.taskManagmentSystem.Util.TaskMapper;
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepo taskRepo;
    private final EmployeeRepo employeeRepo;
    private final ProjectRepo projectRepo;
    public TaskServiceImpl(TaskRepo taskRepo, EmployeeRepo employeeRepo, ProjectRepo projectRepo) {
        this.taskRepo = taskRepo;
        this.employeeRepo = employeeRepo;
        this.projectRepo = projectRepo;
    }
    @Override
    public TaskResponse createTask(TaskRequest request) {
    Task task = TaskMapper.mapToEntity(request);
     if (request.getEmployeeId() != null) {
        Employee employee = employeeRepo.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        task.setEmployee(employee);
    }

    if (request.getProjectId() != null) {
        Project project = projectRepo.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        task.setProject(project);
    }
    Task savedTask = taskRepo.save(task);

    return TaskMapper.mapToDTO(savedTask);

}
     @Override
    public List<TaskResponse> getAllTasks() {
    return taskRepo.findAll()
            .stream()
            .map(TaskMapper::mapToDTO)
            .toList();
}
     @Override
    public TaskResponse getTaskById(Long id) {
        return TaskMapper.mapToDTO(taskRepo.findById(id).orElse(null));
    }
     @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {

    Task task = taskRepo.findById(id).orElse(null);

    if (task != null) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepo.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            task.setEmployee(employee);
        }
        if (request.getProjectId() != null) {
            Project project = projectRepo.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));

            task.setProject(project);
        }
        
        return TaskMapper.mapToDTO(taskRepo.save(task));
    } 
    return null;  
}
     @Override
    public void deleteTask(Long id) {
        taskRepo.deleteById(id);    
    }
   @Override

   public Page<TaskResponse> searchFilterTasks(String keyword, String status, int page, int size) {
    if (keyword != null && keyword.trim().isEmpty()) {
        keyword = null;
    }

    if (status != null && status.trim().isEmpty()) {
        status = null;
    }

    PageRequest pageable = PageRequest.of(page, size);

    Page<Task> taskPage = taskRepo.searchAndFilter(keyword, status, pageable);

    return taskPage.map(TaskMapper::mapToDTO);
}
    
}
