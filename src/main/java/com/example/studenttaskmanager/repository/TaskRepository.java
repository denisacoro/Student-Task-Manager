package com.example.studenttaskmanager.repository;

import com.example.studenttaskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwnerUserId(Long ownerUserId);
}