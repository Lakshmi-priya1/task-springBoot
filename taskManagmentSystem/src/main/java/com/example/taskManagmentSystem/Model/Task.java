package com.example.taskManagmentSystem.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import com.example.taskManagmentSystem.Payload.TaskPriority;
import com.example.taskManagmentSystem.Payload.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    private String title;

    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority; // LOW, MEDIUM, HIGH
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @JsonIgnore
    @ManyToOne
     @JoinColumn(name = "project_id")
     private Project project;
    @ManyToOne
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;
}
