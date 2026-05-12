package com.example.taskManagmentSystem.Controller;

import java.util.List;

import org.springframework.data.domain.Page; 
import org.springframework.web.bind.annotation.*;

import com.example.taskManagmentSystem.Dto.Request.TaskRequest;
import com.example.taskManagmentSystem.Dto.Response.TaskResponse;
import com.example.taskManagmentSystem.Payload.TaskStatus;
import com.example.taskManagmentSystem.Service.TaskService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public TaskResponse addTask(@RequestBody TaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/all")
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/milestone/{milestoneId}")
    public List<TaskResponse> getTasksByMilestone(@PathVariable Long milestoneId) {
        return taskService.getTasksByMilestone(milestoneId);
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/update/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Task deleted successfully";
    }
    @DeleteMapping("/soft-delete/{id}")
    public String softDeleteTask(@PathVariable Long id) {
        taskService.softDeleteTask(id);
        return "Task soft-deleted successfully";
    }

     @PutMapping("/{taskId}/assign/{employeeId}")
     
    public TaskResponse assignEmployee(
            @PathVariable Long taskId,
            @PathVariable Long employeeId) {
        return taskService.assignEmployeeToTask(taskId, employeeId);
    }

    // Unassign one specific employee from a task — employeeId now required
    @DeleteMapping("/{taskId}/unassign/{employeeId}")
    public TaskResponse unassignEmployee(
            @PathVariable Long taskId,
            @PathVariable Long employeeId) {
        return taskService.unassignEmployeeFromTask(taskId, employeeId);
    }

    @GetMapping
    public Page<TaskResponse> getTasks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
) {
    return taskService.searchFilterTasks(keyword, status, page, size, sortBy, direction);
}
}