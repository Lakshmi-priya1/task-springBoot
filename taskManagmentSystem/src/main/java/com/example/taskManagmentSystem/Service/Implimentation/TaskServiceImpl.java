package com.example.taskManagmentSystem.Service.Implimentation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.TaskRequest;
import com.example.taskManagmentSystem.Dto.Response.TaskResponse;
import com.example.taskManagmentSystem.Exception.ResourceNotFoundException;
import com.example.taskManagmentSystem.Model.Employee;
import com.example.taskManagmentSystem.Model.Milestone;
import com.example.taskManagmentSystem.Model.Project;
import com.example.taskManagmentSystem.Model.Task;
import com.example.taskManagmentSystem.Payload.TaskStatus;
import com.example.taskManagmentSystem.Repository.EmployeeRepo;
import com.example.taskManagmentSystem.Repository.MilestoneRepo;
import com.example.taskManagmentSystem.Repository.TaskRepo;
import com.example.taskManagmentSystem.Service.TaskService;
import com.example.taskManagmentSystem.Util.TaskMapper;
@Service
public class TaskServiceImpl implements TaskService {
     private final TaskRepo taskRepo;
    private final EmployeeRepo employeeRepo;
    private final MilestoneRepo milestoneRepo;

    public TaskServiceImpl(TaskRepo taskRepo,
                           EmployeeRepo employeeRepo,
                           MilestoneRepo milestoneRepo) {
        this.taskRepo = taskRepo;
        this.employeeRepo = employeeRepo;
        this.milestoneRepo = milestoneRepo;
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {

        Milestone milestone = milestoneRepo.findById(request.getMilestoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", request.getMilestoneId()));

        Long projectId = milestone.getProject().getProjectId();
        if (taskRepo.existsByTitleInProject(request.getTitle(), projectId)) {
            throw new RuntimeException(
                "A task with title '" + request.getTitle() + "' already exists in this project."
            );
        }

        Task task = TaskMapper.mapToEntity(request);
        task.setMilestone(milestone);

        // No employee assigned at creation — use the assign endpoint instead
        return TaskMapper.mapToDTO(taskRepo.save(task));
    }

    // ─── READ ─────────────────────────────────────────────────────────────────

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepo.findAll()
                .stream()
                .map(TaskMapper::mapToDTO)
                .toList();
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        return TaskMapper.mapToDTO(task);
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        // Milestone is immutable after creation
        if (request.getMilestoneId() != null &&
            !request.getMilestoneId().equals(task.getMilestone().getMilestoneId())) {
            throw new RuntimeException("Task milestone cannot be changed after creation.");
        }

        // Employees are managed via assign/unassign endpoints — not updated here
        return TaskMapper.mapToDTO(taskRepo.save(task));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Override
    public void deleteTask(Long id) {
        if (!taskRepo.existsById(id)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }
        taskRepo.deleteById(id);
    }

    // ─── GET TASKS BY MILESTONE ───────────────────────────────────────────────

    @Override
    public List<TaskResponse> getTasksByMilestone(Long milestoneId) {
        if (!milestoneRepo.existsById(milestoneId)) {
            throw new ResourceNotFoundException("Milestone", "id", milestoneId);
        }
        return taskRepo.findByMilestoneMilestoneId(milestoneId)
                .stream()
                .map(TaskMapper::mapToDTO)
                .toList();
    }

    // ─── ASSIGN EMPLOYEE TO TASK ──────────────────────────────────────────────

    @Override
    public TaskResponse assignEmployeeToTask(Long taskId, Long employeeId) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        // Employee must belong to the task's project
        Project project = task.getMilestone().getProject();
        boolean isMember = project.getEmployees()
                .stream()
                .anyMatch(e -> e.getEmployeeId().equals(employee.getEmployeeId()));

        if (!isMember) {
            throw new RuntimeException(
                "Employee " + employee.getFirstName() + " is not a member of project: " + project.getProjectName()
            );
        }

        // Prevent duplicate assignment
        boolean alreadyAssigned = task.getEmployees()
                .stream()
                .anyMatch(e -> e.getEmployeeId().equals(employeeId));

        if (alreadyAssigned) {
            throw new RuntimeException(
                "Employee " + employee.getFirstName() + " is already assigned to this task."
            );
        }

        task.getEmployees().add(employee);
        return TaskMapper.mapToDTO(taskRepo.save(task));
    }

    // ─── UNASSIGN EMPLOYEE FROM TASK ─────────────────────────────────────────

    @Override
    public TaskResponse unassignEmployeeFromTask(Long taskId, Long employeeId) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        boolean wasAssigned = task.getEmployees()
                .removeIf(e -> e.getEmployeeId().equals(employeeId));

        if (!wasAssigned) {
            throw new RuntimeException(
                "Employee " + employee.getFirstName() + " is not assigned to this task."
            );
        }

        return TaskMapper.mapToDTO(taskRepo.save(task));
    }

    // ─── SEARCH & FILTER ──────────────────────────────────────────────────────

    @Override
    public Page<TaskResponse> searchFilterTasks(String keyword, TaskStatus status,
                                                 int page, int size,
                                                 String sortBy, String direction) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);
        return taskRepo.searchAndFilter(keyword, status, pageable)
                       .map(TaskMapper::mapToDTO);
    }
}