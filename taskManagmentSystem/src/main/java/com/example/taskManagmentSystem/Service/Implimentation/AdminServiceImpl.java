package com.example.taskManagmentSystem.Service.Implimentation;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskManagmentSystem.Dto.Request.AdminRequest;
import com.example.taskManagmentSystem.Dto.Response.AdminResponse;
import com.example.taskManagmentSystem.Model.Admin;
import com.example.taskManagmentSystem.Repository.AdminRepo;
import com.example.taskManagmentSystem.Security.JwtUtil;
import com.example.taskManagmentSystem.Service.AdminService;
import com.example.taskManagmentSystem.Util.AdminMapper;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminServiceImpl(AdminRepo adminRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil   ) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    @Override
public AdminResponse login(AdminRequest request) {

    Admin admin = adminRepo.findByEmail(request.getEmail());

    if (admin == null) {
        throw new RuntimeException("Admin not found");
    }

    if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
        throw new RuntimeException("Invalid password");
    }

    String token = jwtUtil.generateToken(admin.getEmail());
    AdminResponse response = AdminMapper.mapToDto(admin);
    response.setToken(token); 
    return response;
}
    @Override
    public AdminResponse createAdmin(AdminRequest request) {
        Admin admin = AdminMapper.mapToEntity(request);

        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        Admin savedAdmin = adminRepo.save(admin);
        return AdminMapper.mapToDto(savedAdmin);
    }

    @Override
    public AdminResponse getAdminByEmail(String email) {
        Admin admin = adminRepo.findByEmail(email);
        if (admin == null) {
            throw new RuntimeException("Admin not found with email: " + email);
        }
        return AdminMapper.mapToDto(admin);
    }

    @Override
    public AdminResponse updateAdmin(String email, AdminRequest request) {
        Admin admin = adminRepo.findByEmail(email);
        if (admin == null) {
            throw new RuntimeException("Admin not found with email: " + email);
        }
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        Admin updatedAdmin = adminRepo.save(admin);
        return AdminMapper.mapToDto(updatedAdmin);
    }

    @Override
    public void deleteAdmin(String email) {
        Admin admin = adminRepo.findByEmail(email);
        if (admin == null) {
            throw new RuntimeException("Admin not found with email: " + email);
        }
        adminRepo.delete(admin);
    }
}