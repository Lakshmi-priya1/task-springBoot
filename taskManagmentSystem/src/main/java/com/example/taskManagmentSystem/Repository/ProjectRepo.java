package com.example.taskManagmentSystem.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.taskManagmentSystem.Model.Project;
import com.example.taskManagmentSystem.Payload.ProjectStatus;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
   @Query("""
    SELECT p FROM Project p
    WHERE (
        :keyword IS NULL OR
        LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
    )
        AND (
        :status IS NULL OR
        p.status = :status
)

""")
    Page<Project> searchAndFilter(String keyword, ProjectStatus status, Pageable pageable);
}
