package com.example.taskManagmentSystem.Model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.taskManagmentSystem.Payload.MilestoneStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @JsonIgnore
    @OneToMany(mappedBy = "milestone", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks; 
}
