package com.example.taskManagmentSystem.Service.Implimentation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.MilestoneRequest;
import com.example.taskManagmentSystem.Dto.Response.MilestoneResponse;
import com.example.taskManagmentSystem.Model.Milestone;
import com.example.taskManagmentSystem.Model.Project;
import com.example.taskManagmentSystem.Model.Task;
import com.example.taskManagmentSystem.Repository.MilestoneRepo;
import com.example.taskManagmentSystem.Repository.ProjectRepo;
import com.example.taskManagmentSystem.Repository.TaskRepo;
import com.example.taskManagmentSystem.Service.MilestoneService;
import com.example.taskManagmentSystem.Util.MilestoneMapper;

@Service
public class MilestoneServiceImpl implements MilestoneService {
    private final MilestoneRepo milestoneRepo;
    private final ProjectRepo projectRepo;
    private final TaskRepo taskRepo;
    public MilestoneServiceImpl(MilestoneRepo milestoneRepo, ProjectRepo projectRepo, TaskRepo taskRepo ) {
        this.milestoneRepo = milestoneRepo;
        this.projectRepo = projectRepo;
        this.taskRepo = taskRepo;
    }

    @Override
public MilestoneResponse createMilestone(MilestoneRequest request) {

    Project project = projectRepo.findById(request.getProjectId())
            .orElseThrow(() -> new RuntimeException("Project not found"));
    Milestone milestone = MilestoneMapper.mapToEntity(request);

    milestone.setProject(project); 
    Milestone saved = milestoneRepo.save(milestone);

    return MilestoneMapper.mapToDto(saved);
}
    @Override
    public MilestoneResponse getMilestoneById(Long milestoneId) {
        Milestone milestone = milestoneRepo.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));
        return MilestoneMapper.mapToDto(milestone);
    }
    @Override
    public List<MilestoneResponse> getAllMilestones() {
        return milestoneRepo.findAll()
                .stream()
                .map(MilestoneMapper::mapToDto)
                .toList();
    }
     @Override
public MilestoneResponse updateMilestone(Long milestoneId, MilestoneRequest request) {

    Milestone milestone = milestoneRepo.findById(milestoneId)
            .orElseThrow(() -> new RuntimeException("Milestone not found"));

    milestone.setMilestoneName(request.getMilestoneName());
    milestone.setDescription(request.getDescription());
    milestone.setStatus(request.getStatus());
    milestone.setDueDate(request.getDueDate());

    return MilestoneMapper.mapToDto(milestoneRepo.save(milestone));
}
    @Override
    public void deleteMilestone(Long milestoneId) {
        milestoneRepo.deleteById(milestoneId);
    }

    @Override
public void assignTaskToMilestone(Long milestoneId, Long taskId) {

    Task task = taskRepo.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    Milestone milestone = milestoneRepo.findById(milestoneId)
            .orElseThrow(() -> new RuntimeException("Milestone not found"));

    if (task.getProject() == null ||
        !task.getProject().getProjectId().equals(milestone.getProject().getProjectId())) {
        throw new RuntimeException("Task and Milestone belong to different projects");
    }

    task.setMilestone(milestone);
    taskRepo.save(task);
}
    @Override
public void removeTaskFromMilestone(Long milestoneId, Long taskId) {

    Task task = taskRepo.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    task.setMilestone(null); 

    taskRepo.save(task);
}

    @Override
    public Page<MilestoneResponse> searchAndFilterMilestones(String keyword, Long projectId, int page, int size, String sortBy, String direction) {
        if (keyword == null || keyword.isEmpty()) {
            keyword = null; 
        }
        if (projectId == null) {
            projectId = null;
        }
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Milestone> milestonePage = milestoneRepo.searchAndFilter(keyword, projectId, pageable);
        return milestonePage.map(MilestoneMapper::mapToDto);
    }
}
