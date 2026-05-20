package com.example.taskManagmentSystem.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.taskManagmentSystem.Dto.Request.DeptRequest;
import com.example.taskManagmentSystem.Dto.Response.DeptResponse;

public interface DeptService {
    DeptResponse createDepartment(DeptRequest deptRequest);
    DeptResponse getDepartmentById(Long deptId);
    List<DeptResponse> getAllDepartments();
    DeptResponse updateDepartment(Long deptId, DeptRequest deptRequest);
    void deleteDepartment(Long deptId);

    Page <DeptResponse> searchAndFilterDepartments(String keyword, int page, int size);

    
}
