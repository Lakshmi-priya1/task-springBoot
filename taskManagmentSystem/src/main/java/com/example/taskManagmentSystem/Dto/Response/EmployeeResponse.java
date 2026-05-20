package com.example.taskManagmentSystem.Dto.Response;

import com.example.taskManagmentSystem.Model.Employee;
import com.example.taskManagmentSystem.Payload.EmployeeStatus;
import com.example.taskManagmentSystem.Payload.Role;

import lombok.Data;

@Data
public class EmployeeResponse {
    private Long employeeId;  
    private String employeeCode; 
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String department;
    private EmployeeStatus status;
    private String phoneNumber;
    private Role role;
    private Long userId;
    private Long orgId;
    private String companyName;

    public static EmployeeResponse from(Employee emp) {
        EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(emp.getEmployeeId());

        String prefix = switch (emp.getRole()) {
            case ADMIN           -> "ADM";
            case PROJECT_MANAGER -> "PM";
            case TEAM_LEAD       -> "TL";
            case EMPLOYEE        -> "EMP";
        };
        response.setEmployeeCode(prefix + String.format("%03d", emp.getEmployeeId())); 
        response.setUsername(emp.getUsername());
        response.setEmail(emp.getEmail());
        response.setFirstName(emp.getFirstName());
        response.setLastName(emp.getLastName());
        response.setDepartment(emp.getDepartment());
        response.setStatus(emp.getStatus());
        response.setPhoneNumber(emp.getPhoneNumber());
        response.setRole(emp.getRole());
        response.setUserId(emp.getUser() != null ? emp.getUser().getUserId() : null);
        response.setOrgId(emp.getOrganization() != null ? emp.getOrganization().getOrgId() : null);
        response.setCompanyName(emp.getOrganization() != null ? emp.getOrganization().getCompanyName() : null);
        return response;
    }

    
}
