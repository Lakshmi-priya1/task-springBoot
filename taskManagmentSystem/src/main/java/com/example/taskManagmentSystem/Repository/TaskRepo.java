package com.example.taskManagmentSystem.Repository;
import com.example.taskManagmentSystem.Model.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
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

    Page<Task> searchAndFilter(String keyword, String status, Pageable pageable);

}