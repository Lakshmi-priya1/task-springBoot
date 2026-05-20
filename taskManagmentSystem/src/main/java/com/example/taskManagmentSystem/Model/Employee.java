package com.example.taskManagmentSystem.Model;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.example.taskManagmentSystem.Payload.EmployeeStatus;
import com.example.taskManagmentSystem.Payload.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SQLRestriction("deleted = false") 
@Data
@Entity
@NoArgsConstructor

@AllArgsConstructor


public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String department;
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;
    private String phoneNumber;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private boolean deleted = false;
    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;
    @JsonIgnore
    @ManyToMany(mappedBy = "employees")
    private List<Project> projects;

    @JsonIgnore
    @ManyToMany(mappedBy = "employees")
    private List<Task> tasks = new ArrayList<>();
}
