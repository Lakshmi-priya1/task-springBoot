package com.example.taskManagmentSystem.Controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskManagmentSystem.Dto.Request.AdminRequest;
import com.example.taskManagmentSystem.Dto.Response.AdminResponse;
import com.example.taskManagmentSystem.Service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/login")
    public AdminResponse login(@RequestBody AdminRequest request) {
        return adminService.login(request);
    }
    @PostMapping("/create")
    public AdminResponse createAdmin(@RequestBody AdminRequest request) {
        return adminService.createAdmin(request);
    }

    @GetMapping("/get/{email}")
    public AdminResponse getAdminByEmail(@PathVariable String email) {
        return adminService.getAdminByEmail(email);
    }

    @PutMapping("/update/{email}")
    public AdminResponse updateAdmin(@PathVariable String email, @RequestBody AdminRequest request) {
        return adminService.updateAdmin(email, request);
    }

    @DeleteMapping("/delete/{email}")
    public String deleteAdmin(@PathVariable String email) {
        adminService.deleteAdmin(email);
        return "Admin deleted successfully";
    }
}