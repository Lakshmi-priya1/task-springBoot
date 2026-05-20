package com.example.taskManagmentSystem.Service;
import com.example.taskManagmentSystem.Dto.Request.CreateUserRequest;
import com.example.taskManagmentSystem.Dto.Request.UpdateUserRequest;
import com.example.taskManagmentSystem.Dto.Response.UserResponse;
import com.example.taskManagmentSystem.Model.User;
import com.example.taskManagmentSystem.Payload.Role;
import com.example.taskManagmentSystem.Repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@Service
public class UserService {
     private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Admin creates PM, Team Lead, Employee
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        User saved = userRepo.save(user);
        return mapToResponse(saved);
    }

    public List<UserResponse> getAllUsers() {
        return userRepo.findAll().stream().map(this::mapToResponse).toList();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        user.setRole(request.getRole());
        User updated = userRepo.save(user);
        return mapToResponse(updated);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public Page<UserResponse> searchFilterUsers(String keyword, Role role, int page, int size) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        PageRequest pageable = PageRequest.of(page, size);
        return userRepo.searchAndFilter(keyword, role, pageable)  // ✅ pageable passed correctly
                .map(this::mapToResponse);
    }



    private UserResponse mapToResponse(User user) {
        UserResponse res = new UserResponse();
        res.setUserId(user.getUserId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole().name());
        return res;
    }
    
}
