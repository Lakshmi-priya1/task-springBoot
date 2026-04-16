package com.example.taskManagmentSystem.Util;

import com.example.taskManagmentSystem.Dto.Request.AdminRequest;
import com.example.taskManagmentSystem.Dto.Response.AdminResponse;
import com.example.taskManagmentSystem.Model.Admin;

public final class AdminMapper {
    public static Admin mapToEntity(AdminRequest request) {
        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setPassword(request.getPassword());
        return admin;
    }
    public static AdminResponse mapToDto(Admin admin) {
        AdminResponse dto = new AdminResponse();
        dto.setAdminId(admin.getAdminId());
        dto.setEmail(admin.getEmail());
        return dto;
        
    }

}
