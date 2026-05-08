package com.example.taskManagmentSystem.Model;


import java.util.ArrayList;
import java.util.List;

import com.example.taskManagmentSystem.Payload.EmployeeStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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