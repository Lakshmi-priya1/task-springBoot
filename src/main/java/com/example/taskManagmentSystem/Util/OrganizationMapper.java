package com.example.taskManagmentSystem.Util;

import com.example.taskManagmentSystem.Dto.Request.OrganizationRequest;
import com.example.taskManagmentSystem.Dto.Response.OrganizationResponse;
import com.example.taskManagmentSystem.Model.Organization;

public final class OrganizationMapper {
    public static Organization mapToEntity(OrganizationRequest request) {
        Organization organization = new Organization();
        organization.setCompanyName(request.getCompanyName());
        organization.setAddress(request.getAddress());
        return organization;
    }
    public static OrganizationResponse mapToDto(Organization organization) {
        OrganizationResponse dto = new OrganizationResponse();
        dto.setOrgId(organization.getOrgId());
        dto.setCompanyName(organization.getCompanyName());
        dto.setAddress(organization.getAddress());
        return dto;
    }
    
}
