package com.example.taskManagmentSystem.Model;

import java.time.LocalDateTime;
 import java.util.List;

import com.example.taskManagmentSystem.Payload.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "projects")
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long projectId;
    private String projectName;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @JsonIgnore
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
    @ManyToMany
    @JoinTable(
         name = "project_employees",
         joinColumns = @JoinColumn(name = "project_id"),
         inverseJoinColumns = @JoinColumn(name = "employee_id")
        )
    private List<Employee> employees;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Milestone> milestones;
    
}
