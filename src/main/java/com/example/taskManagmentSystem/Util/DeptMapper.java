package com.example.taskManagmentSystem.Util;
import com.example.taskManagmentSystem.Dto.Request.DeptRequest;
import com.example.taskManagmentSystem.Dto.Response.DeptResponse;
import com.example.taskManagmentSystem.Model.Department;

public  final class DeptMapper {
     public static Department mapToEntity(DeptRequest request){
        Department department = new Department();
        department.setDeptName(request.getDeptName());
        department.setDeptDescription(request.getDeptDescription());
        return department;
    }
     public static DeptResponse mapToDto(Department department) {
        DeptResponse dto = new DeptResponse();
        dto.setDeptId(department.getDeptId());
        dto.setDeptName(department.getDeptName());
        dto.setDeptDescription(department.getDeptDescription());
        return dto;
    }
    
}
