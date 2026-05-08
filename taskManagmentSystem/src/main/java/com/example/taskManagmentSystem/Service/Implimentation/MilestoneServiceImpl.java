package com.example.taskManagmentSystem.Service.Implimentation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.MilestoneRequest;
import com.example.taskManagmentSystem.Dto.Response.MilestoneResponse;
import com.example.taskManagmentSystem.Model.Employee;
import com.example.taskManagmentSystem.Model.Milestone;
import com.example.taskManagmentSystem.Model.Project;
import com.example.taskManagmentSystem.Model.Task;
import com.example.taskManagmentSystem.Repository.EmployeeRepo;
import com.example.taskManagmentSystem.Repository.MilestoneRepo;
import com.example.taskManagmentSystem.Repository.ProjectRepo;
import com.example.taskManagmentSystem.Repository.TaskRepo;
import com.example.taskManagmentSystem.Exception.ResourceNotFoundException;
import com.example.taskManagmentSystem.Service.MilestoneService;
import com.example.taskManagmentSystem.Util.MilestoneMapper;

@Service
public class MilestoneServiceImpl implements MilestoneService {
   private final MilestoneRepo milestoneRepo;
    private final ProjectRepo projectRepo;
    private final TaskRepo taskRepo;
    private final EmployeeRepo employeeRepo;

    public MilestoneServiceImpl(MilestoneRepo milestoneRepo,
                                 ProjectRepo projectRepo,
                                 TaskRepo taskRepo,
                                 EmployeeRepo employeeRepo) {
        this.milestoneRepo = milestoneRepo;
        this.projectRepo = projectRepo;
        this.taskRepo = taskRepo;
        this.employeeRepo = employeeRepo;
    }

     // ─── CREATE ───────────────────────────────────────────────────────────────

    @Override
    public MilestoneResponse createMilestone(MilestoneRequest request) {
        Project project = projectRepo.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", request.getProjectId()));

        Milestone milestone = MilestoneMapper.mapToEntity(request);
        milestone.setProject(project);

        return MilestoneMapper.mapToDto(milestoneRepo.save(milestone));
    }

    // ─── READ ─────────────────────────────────────────────────────────────────

    @Override
    public List<MilestoneResponse> getAllMilestones() {
        return milestoneRepo.findAll()
                .stream()
                .map(MilestoneMapper::mapToDto)
                .toList();
    }

    @Override
    public MilestoneResponse getMilestoneById(Long milestoneId) {
        Milestone milestone = milestoneRepo.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));
        return MilestoneMapper.mapToDto(milestone);
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @Override
    public MilestoneResponse updateMilestone(Long milestoneId, MilestoneRequest request) {
        Milestone milestone = milestoneRepo.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));

        milestone.setMilestoneName(request.getMilestoneName());
        milestone.setDescription(request.getDescription());
        milestone.setStatus(request.getStatus());
        milestone.setDueDate(request.getDueDate());

        if (request.getProjectId() != null &&
            !request.getProjectId().equals(milestone.getProject().getProjectId())) {
            Project newProject = projectRepo.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project", "id", request.getProjectId()));
            milestone.setProject(newProject);
        }

        return MilestoneMapper.mapToDto(milestoneRepo.save(milestone));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Override
    public void deleteMilestone(Long milestoneId) {
        if (!milestoneRepo.existsById(milestoneId)) {
            throw new ResourceNotFoundException("Milestone", "id", milestoneId);
        }
        milestoneRepo.deleteById(milestoneId);
    }

    // ─── ASSIGN TASK TO MILESTONE ─────────────────────────────────────────────

    @Override
    public void assignTaskToMilestone(Long milestoneId, Long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        Milestone milestone = milestoneRepo.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));

        if (task.getMilestone() != null) {
            throw new RuntimeException(
                "Task '" + task.getTitle() + "' already belongs to milestone '" +
                task.getMilestone().getMilestoneName() + "' and cannot be reassigned."
            );
        }

        task.setMilestone(milestone);
        taskRepo.save(task);
    }

    // ─── REMOVE TASK FROM MILESTONE ───────────────────────────────────────────

    @Override
    public void removeTaskFromMilestone(Long milestoneId, Long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        if (task.getMilestone() == null ||
            !task.getMilestone().getMilestoneId().equals(milestoneId)) {
            throw new RuntimeException("Task does not belong to this milestone");
        }

        throw new RuntimeException(
            "Cannot unassign task from milestone. Delete the task instead."
        );
    }

    // ─── GET ALL MILESTONES BY PROJECT ────────────────────────────────────────

    @Override
    public List<MilestoneResponse> getMilestonesByProject(Long projectId) {
        if (!projectRepo.existsById(projectId)) {
            throw new ResourceNotFoundException("Project", "id", projectId);
        }
        return milestoneRepo.findByProjectProjectId(projectId)
                .stream()
                .map(MilestoneMapper::mapToDto)
                .toList();
    }

    // ─── SEARCH & FILTER ──────────────────────────────────────────────────────

    @Override
    public Page<MilestoneResponse> searchAndFilterMilestones(String keyword, Long projectId,
                                                              int page, int size,
                                                              String sortBy, String direction) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);
        return milestoneRepo.searchAndFilter(keyword, projectId, pageable)
                            .map(MilestoneMapper::mapToDto);
    }

    // ─── ASSIGN EMPLOYEE TO MILESTONE ────────────────────────────────────────

    @Override
    public MilestoneResponse assignEmployeeToMilestone(Long milestoneId, Long employeeId) {

        Milestone milestone = milestoneRepo.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        // Employee must belong to the milestone's project first
        Project project = milestone.getProject();
        boolean inProject = project.getEmployees().stream()
                .anyMatch(e -> e.getEmployeeId().equals(employeeId));

        if (!inProject) {
            throw new RuntimeException(
                "Employee " + employee.getFirstName() +
                " is not a member of project: " + project.getProjectName() +
                ". Assign them to the project first."
            );
        }

        // Prevent duplicate assignment
        boolean alreadyAssigned = milestone.getEmployees().stream()
                .anyMatch(e -> e.getEmployeeId().equals(employeeId));

        if (alreadyAssigned) {
            throw new RuntimeException(
                "Employee " + employee.getFirstName() +
                " is already assigned to this milestone."
            );
        }

        milestone.getEmployees().add(employee);
        return MilestoneMapper.mapToDto(milestoneRepo.save(milestone));
    }

    // ─── UNASSIGN EMPLOYEE FROM MILESTONE ────────────────────────────────────

    @Override
    public MilestoneResponse unassignEmployeeFromMilestone(Long milestoneId, Long employeeId) {

        Milestone milestone = milestoneRepo.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        boolean wasAssigned = milestone.getEmployees()
                .removeIf(e -> e.getEmployeeId().equals(employeeId));

        if (!wasAssigned) {
            throw new RuntimeException(
                "Employee " + employee.getFirstName() +
                " is not assigned to this milestone."
            );
        }

        return MilestoneMapper.mapToDto(milestoneRepo.save(milestone));
    }
}