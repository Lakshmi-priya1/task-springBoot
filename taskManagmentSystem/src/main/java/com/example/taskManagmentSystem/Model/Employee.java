package com.example.taskManagmentSystem.Model;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    private String status;
    
    private String phoneNumber;
    private String password;

    @OneToMany(mappedBy = "employeeId", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Task> tasks;
    @ManyToOne
    @JoinColumn(name = "org_id")
    
    private Organization organization;

}