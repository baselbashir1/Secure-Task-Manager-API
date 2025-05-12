package com.task.managerapi.repositories;

import com.task.managerapi.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByOwnerId(Pageable pageable, String ownerId);

    Optional<Task> findByIdAndOwnerId(long id, String ownerId);
}