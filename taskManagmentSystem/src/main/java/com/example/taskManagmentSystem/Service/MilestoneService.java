package com.example.taskManagmentSystem.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.taskManagmentSystem.Dto.Request.MilestoneRequest;
import com.example.taskManagmentSystem.Dto.Response.MilestoneResponse;

public interface MilestoneService {
     MilestoneResponse createMilestone(MilestoneRequest request);
    List<MilestoneResponse> getAllMilestones();
    MilestoneResponse getMilestoneById(Long milestoneId);
    MilestoneResponse updateMilestone(Long milestoneId, MilestoneRequest request);
    void deleteMilestone(Long milestoneId);

    // ✅ KEEP - task assignment at milestone level
    void assignTaskToMilestone(Long milestoneId, Long taskId);
    void removeTaskFromMilestone(Long milestoneId, Long taskId);
    MilestoneResponse assignEmployeeToMilestone(Long milestoneId, Long employeeId);
MilestoneResponse unassignEmployeeFromMilestone(Long milestoneId, Long employeeId);
    List<MilestoneResponse> getMilestonesByProject(Long projectId);

    Page<MilestoneResponse> searchAndFilterMilestones(
        String keyword,
        Long projectId,
        int page,
        int size,
        String sortBy,
        String direction
    );
}