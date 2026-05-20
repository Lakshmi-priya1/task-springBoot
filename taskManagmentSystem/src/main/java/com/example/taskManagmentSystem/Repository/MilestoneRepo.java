package com.example.taskManagmentSystem.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.taskManagmentSystem.Model.Milestone;

@Repository
public interface MilestoneRepo extends JpaRepository<Milestone, Long> {
        @Query("""
        SELECT m FROM Milestone m
        WHERE (
            :keyword IS NULL OR
            LOWER(m.milestoneName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
        )
        AND (
            :projectId IS NULL OR
            m.project.projectId = :projectId
        )
    """)
        Page<Milestone> searchAndFilter(String keyword, Long projectId, Pageable pageable);

    List<Milestone> findByProjectProjectId(Long projectId);  
}
