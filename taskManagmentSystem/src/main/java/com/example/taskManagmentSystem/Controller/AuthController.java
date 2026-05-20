package com.example.taskManagmentSystem.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskManagmentSystem.Dto.Request.CreateUserRequest;
import com.example.taskManagmentSystem.Dto.Request.LoginRequest;
import com.example.taskManagmentSystem.Dto.Response.LoginResponse;
import com.example.taskManagmentSystem.Dto.Response.UserResponse;
import com.example.taskManagmentSystem.Service.AuthService;
import com.example.taskManagmentSystem.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
     private final AuthService authService;
     private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }
    
}
