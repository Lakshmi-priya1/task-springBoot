package com.example.taskManagmentSystem.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskManagmentSystem.Dto.Request.OrganizationRequest;
import com.example.taskManagmentSystem.Dto.Response.OrganizationResponse;
import com.example.taskManagmentSystem.Service.OrganizationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
     }
        @PostMapping("/add")
    public OrganizationResponse addOrganization(@RequestBody OrganizationRequest request) {
        return organizationService.createOrganization(request);
    }
    @GetMapping("/all")
    public List<OrganizationResponse> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }
    @GetMapping("/{orgId}")
    public OrganizationResponse getOrganizationById(@PathVariable Long orgId) {
        return organizationService.getOrganizationById(orgId);
    }
    @PutMapping("/update/{orgId}")
    public OrganizationResponse updateOrganization(@PathVariable Long orgId, @RequestBody OrganizationRequest request) {
         return organizationService.updateOrganization(orgId, request);
    }

    @DeleteMapping("/delete/{orgId}")
    public String deleteOrganization(@PathVariable Long orgId) {
        organizationService.deleteOrganization(orgId);
        return "Organization deleted successfully";
    }
    @DeleteMapping("/soft-delete/{orgId}")
    public String softDeleteOrganization(@PathVariable Long orgId) {
        organizationService.softDeleteOrganization(orgId);
        return "Organization soft-deleted successfully";
    }
}
