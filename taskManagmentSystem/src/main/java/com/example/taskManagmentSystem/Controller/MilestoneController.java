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

import com.example.taskManagmentSystem.Dto.Request.MilestoneRequest;
import com.example.taskManagmentSystem.Dto.Response.MilestoneResponse;
import com.example.taskManagmentSystem.Service.MilestoneService;

@RestController
@RequestMapping("/milestones")
public class MilestoneController {
    private final MilestoneService milestoneService;
    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }
    @GetMapping("/all")
    public List<MilestoneResponse> getAllMilestones() {
        return milestoneService.getAllMilestones();
    }

    @GetMapping("/project/{projectId}")
    public List<MilestoneResponse> getMilestonesByProject(@PathVariable Long projectId) {
        return milestoneService.getMilestonesByProject(projectId);
    }
    @GetMapping("/{id}")
    public MilestoneResponse getMilestoneById(@PathVariable Long id) {
        return milestoneService.getMilestoneById(id);
    }
    @PostMapping("/add")
    public MilestoneResponse createMilestone(@RequestBody MilestoneRequest request) {
        return milestoneService.createMilestone(request);
    }
    @PutMapping("/update/{id}")
    public MilestoneResponse updateMilestone(@PathVariable Long id, @RequestBody MilestoneRequest request) {
        return milestoneService.updateMilestone(id, request);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteMilestone(@PathVariable Long id) {
        milestoneService.deleteMilestone(id);
        return "Milestone deleted successfully";
    }
    @DeleteMapping("/soft-delete/{id}")
    public String softDeleteMilestone(@PathVariable Long id) {
        milestoneService.softDeleteMilestone(id);
        return "Milestone soft-deleted successfully";
    }

    @PostMapping("/{milestoneId}/tasks/{taskId}")
    
    public String assignTaskToMilestone(
            @PathVariable Long milestoneId,
            @PathVariable Long taskId) {

        milestoneService.assignTaskToMilestone(milestoneId, taskId);
        return "Task assigned to milestone successfully";
    }
    @DeleteMapping("/{milestoneId}/tasks/{taskId}")
    public String removeTaskFromMilestone(
            @PathVariable Long milestoneId,
            @PathVariable Long taskId) {

        milestoneService.removeTaskFromMilestone(milestoneId, taskId);
        return "Task removed from milestone successfully";
    }

    @PutMapping("/{milestoneId}/assign/{employeeId}")
public MilestoneResponse assignEmployee(
        @PathVariable Long milestoneId,
        @PathVariable Long employeeId) {
    return milestoneService.assignEmployeeToMilestone(milestoneId, employeeId);
}

@DeleteMapping("/{milestoneId}/unassign/{employeeId}")
public MilestoneResponse unassignEmployee(
        @PathVariable Long milestoneId,
        @PathVariable Long employeeId) {
    return milestoneService.unassignEmployeeFromMilestone(milestoneId, employeeId);
        }

    @GetMapping
    public Page<MilestoneResponse> searchAndFilterMilestones(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "milestoneId") String sortBy,
            
            @RequestParam(defaultValue = "asc") String direction

    ) {
        return milestoneService.searchAndFilterMilestones(keyword, projectId, page, size, sortBy, direction);
    }
    

    
}
