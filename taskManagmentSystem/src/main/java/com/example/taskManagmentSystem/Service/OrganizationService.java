package com.example.taskManagmentSystem.Service;

import java.util.List;

import com.example.taskManagmentSystem.Dto.Request.OrganizationRequest;
import com.example.taskManagmentSystem.Dto.Response.OrganizationResponse;

public interface OrganizationService {
    OrganizationResponse createOrganization(OrganizationRequest request);
    OrganizationResponse getOrganizationById(Long orgId);
    List<OrganizationResponse> getAllOrganizations();
    OrganizationResponse updateOrganization(Long orgId, OrganizationRequest request);
    void deleteOrganization(Long orgId);
}
