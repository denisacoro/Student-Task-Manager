package com.example.studenttaskmanager.service;

import com.example.studenttaskmanager.dto.TaskCreateRequest;
import com.example.studenttaskmanager.dto.TaskResponse;
import com.example.studenttaskmanager.entity.Task;
import com.example.studenttaskmanager.entity.User;
import com.example.studenttaskmanager.exception.ResourceNotFoundException;
import com.example.studenttaskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_ShouldSaveTask_ForCurrentUser() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Finish project");
        request.setDescription("Implement tests");

        User user = new User();
        user.setId(1L);
        user.setUsername("denisa");

        when(userService.findByUsername("denisa")).thenReturn(user);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Finish project");
        savedTask.setDescription("Implement tests");
        savedTask.setCreatedAt(LocalDateTime.now());
        savedTask.setOwnerUserId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse result = taskService.createTask(request, "denisa");

        assertNotNull(result);
        assertEquals("Finish project", result.getTitle());
        assertEquals("Implement tests", result.getDescription());
        assertEquals(1L, result.getOwnerUserId());

        verify(userService).findByUsername("denisa");
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getMyTasks_ShouldReturnOnlyCurrentUsersTasks() {
        User user = new User();
        user.setId(1L);
        user.setUsername("denisa");

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Desc 1");
        task1.setCreatedAt(LocalDateTime.now());
        task1.setOwnerUserId(1L);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Desc 2");
        task2.setCreatedAt(LocalDateTime.now());
        task2.setOwnerUserId(1L);

        when(userService.findByUsername("denisa")).thenReturn(user);
        when(taskRepository.findByOwnerUserId(1L)).thenReturn(List.of(task1, task2));

        List<TaskResponse> result = taskService.getMyTasks("denisa");

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());

        verify(userService).findByUsername("denisa");
        verify(taskRepository).findByOwnerUserId(1L);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Desc 1");
        task1.setCreatedAt(LocalDateTime.now());
        task1.setOwnerUserId(1L);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Desc 2");
        task2.setCreatedAt(LocalDateTime.now());
        task2.setOwnerUserId(2L);

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<TaskResponse> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());

        verify(taskRepository).findAll();
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L));

        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).delete(any(Task.class));
    }
}