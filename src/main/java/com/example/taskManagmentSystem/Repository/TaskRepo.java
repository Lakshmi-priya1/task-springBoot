package com.example.taskManagmentSystem.Repository;
import java.util.List;

import com.example.taskManagmentSystem.Model.Task;
import com.example.taskManagmentSystem.Payload.TaskStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    @Query("""
    SELECT COUNT(t) > 0 FROM Task t
    WHERE LOWER(t.title) = LOWER(:title)
    AND t.milestone.project.projectId = :projectId
    """)
    boolean existsByTitleInProject(String title, Long projectId);

    List<Task> findByMilestoneMilestoneId(Long milestoneId);

     @Query("""
    SELECT t FROM Task t
    WHERE (
        :keyword IS NULL OR
        LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) 
    )
    AND (
        :status IS NULL OR
        t.status = :status
    )
""")   
    Page<Task> searchAndFilter(String keyword, TaskStatus status, Pageable pageable);
}