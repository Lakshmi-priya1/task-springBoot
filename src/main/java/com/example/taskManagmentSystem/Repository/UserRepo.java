package com.example.taskManagmentSystem.Repository;
import com.example.taskManagmentSystem.Model.User;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.taskManagmentSystem.Payload.Role;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
        @Query("""
            SELECT u FROM User u
            WHERE (:keyword IS NULL OR
               LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
               LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:role IS NULL OR u.role = :role)
        
    """)
    Page<User> searchAndFilter(String keyword, Role role, Pageable pageable);
    
}
