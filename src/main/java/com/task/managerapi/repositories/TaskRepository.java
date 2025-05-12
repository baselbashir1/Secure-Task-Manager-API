package com.task.managerapi.repositories;

import com.task.managerapi.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}