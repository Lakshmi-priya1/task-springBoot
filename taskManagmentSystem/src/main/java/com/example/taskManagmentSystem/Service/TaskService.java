package com.example.taskManagmentSystem.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import com.example.taskManagmentSystem.Dto.Request.TaskRequest;
import com.example.taskManagmentSystem.Dto.Response.TaskResponse;


public  interface  TaskService {
   TaskResponse createTask(TaskRequest request);

    List<TaskResponse> getAllTasks();

    TaskResponse getTaskById(Long id);

    TaskResponse updateTask(Long id, TaskRequest request);

    void deleteTask(Long id);

    Page<TaskResponse> searchFilterTasks(String keyword, String status, int page, int size);

    
}
