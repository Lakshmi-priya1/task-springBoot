package com.example.taskManagmentSystem.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.taskManagmentSystem.Payload.MilestoneStatus;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@SQLRestriction("deleted = false") 

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "milestones")
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long milestoneId;
    private String milestoneName;
    private String description;
    @Enumerated(EnumType.STRING)
    private MilestoneStatus status; 
    private LocalDateTime dueDate;
    @Column(nullable = false)
private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "milestone", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks; 

    @ManyToMany
    @JoinTable(
        name = "milestone_employees",
        joinColumns = @JoinColumn(name = "milestone_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees = new ArrayList<>();

    
}
