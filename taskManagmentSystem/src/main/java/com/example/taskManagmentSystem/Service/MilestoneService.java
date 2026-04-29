package com.example.taskManagmentSystem.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.taskManagmentSystem.Dto.Request.MilestoneRequest;
import com.example.taskManagmentSystem.Dto.Response.MilestoneResponse;

public interface MilestoneService {
    MilestoneResponse createMilestone(MilestoneRequest request);
    MilestoneResponse getMilestoneById(Long milestoneId);
    List<MilestoneResponse> getAllMilestones();
    MilestoneResponse updateMilestone(Long milestoneId, MilestoneRequest request);
    void deleteMilestone(Long milestoneId);
    void assignTaskToMilestone(Long milestoneId, Long taskId);
    void removeTaskFromMilestone(Long milestoneId, Long taskId);
    Page<MilestoneResponse> searchAndFilterMilestones(String keyword, Long projectId, int page, int size, String sortBy, String direction);

}
