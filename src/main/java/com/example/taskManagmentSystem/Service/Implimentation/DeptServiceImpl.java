package com.example.taskManagmentSystem.Service.Implimentation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.DeptRequest;
import com.example.taskManagmentSystem.Dto.Response.DeptResponse;
import com.example.taskManagmentSystem.Model.Department;
import com.example.taskManagmentSystem.Repository.DeptRepo;
import com.example.taskManagmentSystem.Service.DeptService;
import com.example.taskManagmentSystem.Util.DeptMapper;

@Service
public class DeptServiceImpl implements DeptService{
    private final DeptRepo deptRepo;
    public DeptServiceImpl(DeptRepo deptRepo) {
        this.deptRepo = deptRepo;
     }

    @Override
    public DeptResponse createDepartment(DeptRequest request) {
        Department dept = DeptMapper.mapToEntity(request);
        Department saved = deptRepo.save(dept );
        return DeptMapper.mapToDto(saved);
    } 
    @Override
    public DeptResponse getDepartmentById(Long deptId) {
        Department dept = deptRepo.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + deptId));
        return DeptMapper.mapToDto(dept);
    }
    @Override
    public List<DeptResponse> getAllDepartments() {
        List<Department> depts;
        depts = deptRepo.findAll();
        return depts.stream()
                .map(DeptMapper::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public DeptResponse updateDepartment(Long deptId, DeptRequest request) {
        Department dept = deptRepo.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + deptId));
        dept.setDeptName(request.getDeptName());
        dept.setDeptDescription(request.getDeptDescription());
        Department updated = deptRepo.save(dept);
        return DeptMapper.mapToDto(updated);
    }
    @Override
    public void deleteDepartment(Long deptId) {
        Department dept = deptRepo.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + deptId));
        deptRepo.delete(dept);
    }
    @Override
    public Page<DeptResponse> searchAndFilterDepartments(String keyword, int page, int size) {
        if (keyword != null && keyword.trim().isEmpty()) {  
        keyword = null;
    }
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> deptPage = deptRepo.searchAndFilter(keyword, pageable);
        return deptPage.map(DeptMapper::mapToDto);
    }
     
    
}
