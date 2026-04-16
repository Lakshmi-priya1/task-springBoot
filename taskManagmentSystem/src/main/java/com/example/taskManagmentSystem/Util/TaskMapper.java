package com.example.taskManagmentSystem.Util;

import com.example.taskManagmentSystem.Dto.Request.TaskRequest;
import com.example.taskManagmentSystem.Dto.Response.TaskResponse;
import com.example.taskManagmentSystem.Model.Task;

public final  class TaskMapper {

    public static Task mapToEntity(TaskRequest dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        task.setEmployeeId(null); // Set employeeId to null, it will be handled in the service layer
        return task;
    }

    public static TaskResponse mapToDTO(Task task) {
        TaskResponse dto = new TaskResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setEmployeeId(task.getEmployeeId() != null ? task.getEmployeeId().getEmployeeId() : null);  
        return dto;
    }
}
