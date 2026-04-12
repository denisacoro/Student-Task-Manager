package com.example.studenttaskmanager.service;

import com.example.studenttaskmanager.dto.TaskCreateRequest;
import com.example.studenttaskmanager.dto.TaskResponse;
import com.example.studenttaskmanager.entity.Task;
import com.example.studenttaskmanager.entity.User;
import com.example.studenttaskmanager.exception.ResourceNotFoundException;
import com.example.studenttaskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository,
                       UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public TaskResponse createTask(TaskCreateRequest request, String username) {
        User currentUser = userService.findByUsername(username);

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCreatedAt(LocalDateTime.now());
        task.setOwnerUserId(currentUser.getId());

        Task savedTask = taskRepository.save(task);

        return mapToTaskResponse(savedTask);
    }

    public List<TaskResponse> getMyTasks(String username) {
        User currentUser = userService.findByUsername(username);

        return taskRepository.findByOwnerUserId(currentUser.getId())
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        taskRepository.delete(task);
    }

    private TaskResponse mapToTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getOwnerUserId()
        );
    }
}