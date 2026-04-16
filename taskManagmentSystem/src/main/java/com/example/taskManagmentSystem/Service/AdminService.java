package com.example.taskManagmentSystem.Service;

import com.example.taskManagmentSystem.Dto.Request.AdminRequest;
import com.example.taskManagmentSystem.Dto.Response.AdminResponse;

public interface AdminService {
        AdminResponse login(AdminRequest request);
        AdminResponse createAdmin(AdminRequest request);
        AdminResponse getAdminByEmail(String email);
        AdminResponse updateAdmin(String email, AdminRequest request);
        void deleteAdmin(String email);
    
}
