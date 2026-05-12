package com.example.taskManagmentSystem.Service.Implimentation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.OrganizationRequest;
import com.example.taskManagmentSystem.Dto.Response.OrganizationResponse;
import com.example.taskManagmentSystem.Model.Organization;
import com.example.taskManagmentSystem.Repository.OrganizationRepo;
import com.example.taskManagmentSystem.Service.OrganizationService;
import com.example.taskManagmentSystem.Util.OrganizationMapper;

@Service
public class OrgnizationServiceImpl implements OrganizationService {
    private final OrganizationRepo organizationRepo;

    public OrgnizationServiceImpl(OrganizationRepo organizationRepo) {
        this.organizationRepo = organizationRepo;
    }

    @Override
    public OrganizationResponse createOrganization(OrganizationRequest request) {
        Organization organization = OrganizationMapper.mapToEntity(request);
        Organization saved = organizationRepo.save(organization);
        return OrganizationMapper.mapToDto(saved);
    }

    @Override
    public OrganizationResponse getOrganizationById(Long orgId) {
        Organization org = organizationRepo.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        return OrganizationMapper.mapToDto(org);
    }

    @Override
    public List<OrganizationResponse> getAllOrganizations() {
        return organizationRepo.findAll()
                .stream()
                .map(OrganizationMapper::mapToDto)
                .toList();
    }

    @Override
    public OrganizationResponse updateOrganization(Long orgId, OrganizationRequest request) {

        Organization org = organizationRepo.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        org.setCompanyName(request.getCompanyName());
        org.setAddress(request.getAddress());

        Organization updated = organizationRepo.save(org);
        return OrganizationMapper.mapToDto(updated);
    }
    
    @Override
    public void deleteOrganization(Long orgId) {
        organizationRepo.deleteById(orgId);
    }

    @Override
    public void softDeleteOrganization(Long orgId) {
        Organization org = organizationRepo.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        org.setDeleted(true);
        organizationRepo.save(org);
    }
}
