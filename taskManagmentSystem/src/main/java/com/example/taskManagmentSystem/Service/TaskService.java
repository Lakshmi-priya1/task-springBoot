package com.example.taskManagmentSystem.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import com.example.taskManagmentSystem.Dto.Request.TaskRequest;
import com.example.taskManagmentSystem.Dto.Response.TaskResponse;
import com.example.taskManagmentSystem.Payload.TaskStatus;


public  interface  TaskService {
   TaskResponse createTask(TaskRequest request);
    List<TaskResponse> getAllTasks();
    TaskResponse getTaskById(Long id);
    TaskResponse updateTask(Long id, TaskRequest request);
    void deleteTask(Long id);

    List<TaskResponse> getTasksByMilestone(Long milestoneId);
    TaskResponse assignEmployeeToTask(Long taskId, Long employeeId);
    TaskResponse unassignEmployeeFromTask(Long taskId, Long employeeId);

    Page<TaskResponse> searchFilterTasks(
        String keyword,
        TaskStatus status,
        int page,
        int size,
        String sortBy,
        String direction
    );
}