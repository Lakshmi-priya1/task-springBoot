package com.example.taskManagmentSystem.Util;

import com.example.taskManagmentSystem.Dto.Request.EmployeeRequest;
import com.example.taskManagmentSystem.Dto.Response.EmployeeResponse;
import com.example.taskManagmentSystem.Model.Employee;

public final  class EmployeeMapper {
    public static Employee mapToEntity(EmployeeRequest dto) {
        Employee employee = new Employee();
        employee.setUsername(dto.getUsername());
        employee.setEmail(dto.getEmail());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setDepartment(dto.getDepartment());
        employee.setStatus(dto.getStatus());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setPassword(dto.getPassword());
        employee.setRole(dto.getRole());
        return employee;
    }

    public static EmployeeResponse mapToDTO(Employee employee) {
        EmployeeResponse dto = new EmployeeResponse();

        String prefix = switch (employee.getRole()) {
            case ADMIN           -> "ADM";
            case PROJECT_MANAGER -> "PM";
            case TEAM_LEAD       -> "TL";
            case EMPLOYEE        -> "EMP";
        };
        dto.setEmployeeCode(prefix + String.format("%03d", employee.getEmployeeId()));
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setUsername(employee.getUsername());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(employee.getDepartment());
        dto.setStatus(employee.getStatus()); 
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setRole(employee.getRole());
        dto.setUserId(employee.getUser() != null ? employee.getUser().getUserId() : null);
        dto.setOrgId(employee.getOrganization() != null ? employee.getOrganization().getOrgId() : null);
        dto.setCompanyName(employee.getOrganization() != null ? employee.getOrganization().getCompanyName() : null);
        return dto;
    }
    
}
