package com.example.taskManagmentSystem.Service.Implimentation;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.EmployeeRequest;
import com.example.taskManagmentSystem.Dto.Response.EmployeeResponse;
import com.example.taskManagmentSystem.Exception.ResourceNotFoundException;
import com.example.taskManagmentSystem.Model.Employee;
import com.example.taskManagmentSystem.Model.Organization;
import com.example.taskManagmentSystem.Repository.EmployeeRepo;
import com.example.taskManagmentSystem.Repository.OrganizationRepo;
import com.example.taskManagmentSystem.Service.EmployeeService;
import com.example.taskManagmentSystem.Util.EmployeeMapper;

@Service
public class EmployeeServiceImpl implements EmployeeService {
     private final EmployeeRepo employeeRepo;
    private final OrganizationRepo organizationRepo;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, OrganizationRepo organizationRepo) {
        this.employeeRepo = employeeRepo;
        this.organizationRepo = organizationRepo;
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        Employee employee = EmployeeMapper.mapToEntity(request);

        if (request.getOrgId() != null) {
            Organization org = organizationRepo.findById(request.getOrgId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", request.getOrgId()));
            employee.setOrganization(org);
        }

        return EmployeeMapper.mapToDTO(employeeRepo.save(employee));
    }

    // ─── READ ─────────────────────────────────────────────────────────────────

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepo.findAll()
                .stream()
                .map(EmployeeMapper::mapToDTO)
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long employeeId) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        return EmployeeMapper.mapToDTO(employee);
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @Override
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest request) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        employee.setUsername(request.getUsername());
        employee.setEmail(request.getEmail());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setDepartment(request.getDepartment());
        employee.setStatus(request.getStatus());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setPassword(request.getPassword());

        if (request.getOrgId() != null) {
            Organization org = organizationRepo.findById(request.getOrgId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", request.getOrgId()));
            employee.setOrganization(org);
        }

        return EmployeeMapper.mapToDTO(employeeRepo.save(employee));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Override
    public void deleteEmployee(Long employeeId) {
        if (!employeeRepo.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee", "id", employeeId);
        }
        employeeRepo.deleteById(employeeId);
    }
    @Override
    public void softDeleteEmployee(Long employeeId) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        employee.setDeleted(true);
        employeeRepo.save(employee);
    }
    // ─── SEARCH & FILTER ──────────────────────────────────────────────────────

    @Override
    public Page<EmployeeResponse> searchFilterEmployees(String keyword, String department,
                                                         int page, int size,
                                                         String sortBy, String direction) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        if (department != null && department.trim().isEmpty()) {
            department = null;
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);
        return employeeRepo.searchAndFilter(keyword, department, pageable)
                           .map(EmployeeMapper::mapToDTO);
    }
}