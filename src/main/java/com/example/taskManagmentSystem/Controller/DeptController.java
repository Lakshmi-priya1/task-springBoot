package com.example.taskManagmentSystem.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.taskManagmentSystem.Dto.Request.DeptRequest;
import com.example.taskManagmentSystem.Dto.Response.DeptResponse;
import com.example.taskManagmentSystem.Service.DeptService;

@RestController
@RequestMapping("/departments")
public class DeptController {
    private final DeptService deptService;
    public DeptController(DeptService deptService) {
        this.deptService = deptService;
     }
     @GetMapping("/all")
    public List<DeptResponse> getAllDepartments() {
        return deptService.getAllDepartments();
    }
    @GetMapping("/{deptId}")
    public DeptResponse getDepartmentById(@PathVariable Long deptId) {
        return deptService.getDepartmentById(deptId);
    }
    @PostMapping("/add")
    public DeptResponse addDepartment(@RequestBody DeptRequest request) {
        return deptService.createDepartment(request);
    }   
    @PutMapping("/update/{deptId}")
    public DeptResponse updateDepartment(@PathVariable Long deptId, @RequestBody DeptRequest request) {
         return deptService.updateDepartment(deptId, request);
    }
    @DeleteMapping("/delete/{deptId}")
    public String deleteDepartment(@PathVariable Long deptId) {
        deptService.deleteDepartment(deptId);
        return "Department deleted successfully";
    }
    @GetMapping
    public Page<DeptResponse> searchAndFilterDepartments(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return deptService.searchAndFilterDepartments(keyword, page, size);
    }

    
}
