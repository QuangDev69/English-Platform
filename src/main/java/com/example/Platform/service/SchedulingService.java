package com.example.Platform.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;

public interface SchedulingService {
    <T> void scheduleTask(String taskName, String taskType, T taskData, LocalDateTime scheduledTime) throws JsonProcessingException;
    boolean cancelScheduledTask(Long taskId);
}
