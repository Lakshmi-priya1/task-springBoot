package com.example.taskManagmentSystem.Service.Implimentation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.ProjectRequest;
import com.example.taskManagmentSystem.Dto.Response.ProjectResponse;
import com.example.taskManagmentSystem.Exception.ResourceNotFoundException;
import com.example.taskManagmentSystem.Model.Employee;
import com.example.taskManagmentSystem.Model.Project;
import com.example.taskManagmentSystem.Payload.ProjectStatus;
import com.example.taskManagmentSystem.Repository.EmployeeRepo;
import com.example.taskManagmentSystem.Repository.ProjectRepo;
import com.example.taskManagmentSystem.Service.ProjectService;
import com.example.taskManagmentSystem.Util.ProjectMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

private final ProjectRepo projectRepo;
private final EmployeeRepo employeeRepo;

public ProjectServiceImpl(ProjectRepo projectRepo, EmployeeRepo employeeRepo) {
    this.projectRepo = projectRepo;
    this.employeeRepo = employeeRepo;
}

// ================= CREATE =================

@Override
public ProjectResponse createProject(ProjectRequest request) {

    Project project = ProjectMapper.mapToEntity(request);

    if (request.getEmployeeIds() != null && !request.getEmployeeIds().isEmpty()) {

        List<Employee> employees = employeeRepo.findAllById(request.getEmployeeIds());

        if (employees.size() != request.getEmployeeIds().size()) {
            throw new RuntimeException("Some employees not found");
        }

        project.setEmployees(new ArrayList<>(employees));
    } else {
        project.setEmployees(new ArrayList<>());
    }

    return ProjectMapper.mapToDto(projectRepo.save(project));
}

// ================= READ =================

@Override
public List<ProjectResponse> getAllProjects() {
    return projectRepo.findAll()
            .stream()
            .map(project -> {

                // 🔥 FORCE LOAD
                if (project.getEmployees() != null) {
                    project.getEmployees().size();
                }

                if (project.getMilestones() != null) {
                    project.getMilestones().size();
                    project.getMilestones().forEach(m -> {
                        if (m.getTasks() != null) {
                            m.getTasks().size();
                        }
                    });
                }

                return ProjectMapper.mapToDto(project);
            })
            .toList();
}

@Override
public ProjectResponse getProjectById(Long projectId) {

    Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

    // 🔥 FORCE LOAD
    if (project.getEmployees() != null) {
        project.getEmployees().size();
    }

    if (project.getMilestones() != null) {
        project.getMilestones().size();
        project.getMilestones().forEach(m -> {
            if (m.getTasks() != null) {
                m.getTasks().size();
            }
        });
    }

    return ProjectMapper.mapToDto(project);
}

// ================= UPDATE =================

@Override
public ProjectResponse updateProject(Long projectId, ProjectRequest request) {

    Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

    // update basic fields
    project.setProjectName(request.getProjectName());
    project.setDescription(request.getDescription());
    project.setStatus(request.getStatus());
    project.setStartDate(request.getStartDate());
    project.setEndDate(request.getEndDate());

    // update employees safely
    if (request.getEmployeeIds() != null && !request.getEmployeeIds().isEmpty()) {

        List<Employee> employees = employeeRepo.findAllById(request.getEmployeeIds());

        if (employees.size() != request.getEmployeeIds().size()) {
            throw new RuntimeException("Some employees not found");
        }

        project.setEmployees(new ArrayList<>(employees));

    } else {
        project.setEmployees(new ArrayList<>());
    }

    return ProjectMapper.mapToDto(projectRepo.save(project));
}

// ================= DELETE =================

@Override
public void deleteProject(Long projectId) {

    if (!projectRepo.existsById(projectId)) {
        throw new ResourceNotFoundException("Project", "id", projectId);
    }

    projectRepo.deleteById(projectId);
}

@Override 
public void softDeleteProject(Long projectId) {

    Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
    project.setDeleted(true);
    projectRepo.save(project);
}



// ================= ASSIGN EMPLOYEE =================

@Override
public void assignEmployeeToProject(Long projectId, Long employeeId) {
Project project = projectRepo.findById(projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

Employee employee = employeeRepo.findById(employeeId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

if (project.getEmployees() == null) {
    project.setEmployees(new ArrayList<>());
}

// ✅ check duplicate
boolean alreadyMember = project.getEmployees()
        .stream()
        .anyMatch(e -> e.getEmployeeId().equals(employeeId));

if (alreadyMember) {
    throw new RuntimeException("Employee already assigned to project");
}

// ✅ add employee
project.getEmployees().add(employee);

// ✅ IMPORTANT: sync both sides (ManyToMany best practice)
if (employee.getProjects() == null) {
    employee.setProjects(new ArrayList<>());
}
employee.getProjects().add(project);

projectRepo.save(project);

}

// ================= REMOVE EMPLOYEE =================

@Override
public void removeEmployeeFromProject(Long projectId, Long employeeId) {
Project project = projectRepo.findById(projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

Employee employee = employeeRepo.findById(employeeId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

if (project.getEmployees() == null || project.getEmployees().isEmpty()) {
    throw new RuntimeException("No employees assigned to this project");
}

boolean isMember = project.getEmployees()
        .stream()
        .anyMatch(e -> e.getEmployeeId().equals(employeeId));

if (!isMember) {
    throw new RuntimeException("Employee is not part of this project");
}

project.getEmployees().remove(employee);

// ✅ sync both sides
if (employee.getProjects() != null) {
    employee.getProjects().remove(project);
}

projectRepo.save(project);


}


// ================= SEARCH & FILTER =================

@Override
public Page<ProjectResponse> searchAndFilterProjects(
        String keyword,
        ProjectStatus status,
        int page,
        int size,
        String sortBy,
        String direction
) {

    if (keyword != null && keyword.trim().isEmpty()) {
        keyword = null;
    }

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    PageRequest pageable = PageRequest.of(page, size, sort);

    Page<Project> pageData = projectRepo.searchAndFilter(keyword, status, pageable);

    return pageData.map(project -> {
        if (project.getEmployees() != null) {
            project.getEmployees().size();
        }

        if (project.getMilestones() != null) {
            project.getMilestones().size();

            project.getMilestones().forEach(m -> {
                if (m.getTasks() != null) {
                    m.getTasks().size();
                }
            });
        }

        return ProjectMapper.mapToDto(project);
    });
}


}
