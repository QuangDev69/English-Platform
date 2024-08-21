package com.example.Platform.repository;


import com.example.Platform.entity.ScheduledTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, Long> {
    List<ScheduledTask> findByStatusAndScheduledTimeBefore(String status, LocalDateTime scheduledTime);
}