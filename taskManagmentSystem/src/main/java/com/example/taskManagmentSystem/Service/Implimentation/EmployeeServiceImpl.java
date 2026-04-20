package com.example.taskManagmentSystem.Service.Implimentation;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.EmployeeRequest;
import com.example.taskManagmentSystem.Dto.Response.EmployeeResponse;
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

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

    Employee employee = EmployeeMapper.mapToEntity(request);

    if (request.getOrgId() != null) {
        Organization org = organizationRepo.findById(request.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        employee.setOrganization(org); 
    }

    Employee savedEmployee = employeeRepo.save(employee);

    return EmployeeMapper.mapToDTO(savedEmployee);
}
    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepo.findAll()
                .stream()
                .map(EmployeeMapper::mapToDTO)
                .toList();
    }
    @Override
    public EmployeeResponse getEmployeeById(Long employeeId) {
        return EmployeeMapper.mapToDTO(employeeRepo.findById(employeeId).orElse(null));
    }
    @Override
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest request) {
         Employee employee = employeeRepo.findById(employeeId)
          .orElseThrow(() -> new RuntimeException("Employee not found"));
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
                .orElseThrow(() -> new RuntimeException("Organization not found"));
                employee.setOrganization(org);
            }
            return EmployeeMapper.mapToDTO(employeeRepo.save(employee));
    }
    @Override
    public void deleteEmployee(Long employeeId) {
        employeeRepo.deleteById(employeeId);    
    }
    @Override
public Page<EmployeeResponse> searchFilterEmployees(String keyword, String department, int page, int size) {

    if (keyword != null && keyword.trim().isEmpty()) {
        keyword = null;
    }

    if (department != null && department.trim().isEmpty()) {
        department = null;
    }

    PageRequest pageable = PageRequest.of(page, size);

    Page<Employee> employeePage = employeeRepo.searchAndFilter(keyword, department, pageable);

    return employeePage.map(EmployeeMapper::mapToDTO);
}
   
    
    

}
