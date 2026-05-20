package com.example.taskManagmentSystem.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.taskManagmentSystem.Dto.Request.EmployeeRequest;
import com.example.taskManagmentSystem.Dto.Response.EmployeeResponse;

public interface  EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployeeById(Long employeeId);

    EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest request);

    void deleteEmployee(Long employeeId);
    void softDeleteEmployee(Long employeeId);
    Page<EmployeeResponse> searchFilterEmployees(String keyword, String department, int page, int size, String sortBy, String direction);
    List<EmployeeResponse> bulkCreateEmployees(List<EmployeeRequest> requests);
}
