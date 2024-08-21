//package com.example.Platform.service.serviceImpl;
//
//
//import com.example.Platform.dto.CourseDTO;
//import com.example.Platform.dto.LessonDTO;
//import com.example.Platform.entity.ScheduledTask;
//import com.example.Platform.repository.ScheduledTaskRepository;
//import com.example.Platform.service.CourseService;
//import com.example.Platform.service.LessonService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.Map;
//import java.util.concurrent.*;
//
//@Service
//@RequiredArgsConstructor
//public class SchedulingService {
//
//    private final ScheduledTaskRepository scheduledTaskRepository;
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    private final ObjectMapper objectMapper;
//    private final CourseService courseService;
//    private final LessonService lessonService;
//    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
//
//
//    public <T> void scheduleTask(String taskName, String taskType, T taskData, LocalDateTime scheduledTime) throws JsonProcessingException {
//        ScheduledTask task = ScheduledTask.builder()
//                .taskName(taskName)
//                .taskType(taskType)
//                .taskData(objectMapper.writeValueAsString(taskData))
//                .scheduledTime(scheduledTime)
//                .status("PENDING")
//                .build();
//        scheduledTaskRepository.save(task);
//
//        long delay = Duration.between(LocalDateTime.now(), scheduledTime).toMillis();
//        ScheduledFuture<?> future = scheduler.schedule(() -> {
//            try {
//                executeTask(task);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }, delay, TimeUnit.MILLISECONDS);
//
//        scheduledTasks.put(task.getId(), future);
//
//    }
//
//    private void executeTask(ScheduledTask task) throws Exception {
//        switch (task.getTaskType()) {
//            case "CreateCourse" -> {
//                CourseDTO courseDTO = objectMapper.readValue(task.getTaskData(), CourseDTO.class);
//                courseService.createCourse(courseDTO);
//            }
//            case "CreateLesson" -> {
//                LessonDTO lessonDTO = objectMapper.readValue(task.getTaskData(), LessonDTO.class);
//                lessonService.createLesson(lessonDTO);
//            }
//            default -> throw new IllegalArgumentException("Unknown task type: " + task.getTaskType());
//        }
//        task.setStatus("COMPLETED");
//        scheduledTaskRepository.save(task);
//    }
//
//    public boolean cancelScheduledTask(Long taskId) {
//        ScheduledTask task = scheduledTaskRepository.findById(taskId)
//                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
//
//        ScheduledFuture<?> future = scheduledTasks.get(taskId);
//
//        if (future != null && !future.isDone()) {
//            future.cancel(true);
//            task.setStatus("CANCELLED");
//            scheduledTaskRepository.save(task);
//            scheduledTasks.remove(taskId);
//            return true;
//        }
//
//        return false;
//    }
//}


package com.example.Platform.service.serviceImpl;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.dto.LessonDTO;
import com.example.Platform.entity.ScheduledTask;
import com.example.Platform.repository.ScheduledTaskRepository;
import com.example.Platform.service.CourseService;
import com.example.Platform.service.LessonService;
import com.example.Platform.service.SchedulingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {

    private final ScheduledTaskRepository scheduledTaskRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    private final ObjectMapper objectMapper;
    private final CourseService courseService;
    private final LessonService lessonService;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Override
    public <T> void scheduleTask(String taskName, String taskType, T taskData, LocalDateTime scheduledTime) throws JsonProcessingException {
        ScheduledTask task = ScheduledTask.builder()
                .taskName(taskName)
                .taskType(taskType)
                .taskData(objectMapper.writeValueAsString(taskData))
                .scheduledTime(scheduledTime)
                .status("PENDING")
                .build();
        task = scheduledTaskRepository.save(task);

        long delay = Duration.between(LocalDateTime.now(), scheduledTime).toMillis();
        ScheduledTask finalTask = task;
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            try {
                executeTask(finalTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delay, TimeUnit.MILLISECONDS);

        scheduledTasks.put(task.getId(), future);
    }

    private void executeTask(ScheduledTask task) {
        try {
            switch (task.getTaskType()) {
                case "CreateCourse" -> {
                    CourseDTO courseDTO = objectMapper.readValue(task.getTaskData(), CourseDTO.class);
                    courseService.createCourse(courseDTO);
                }
                case "CreateLesson" -> {
                    LessonDTO lessonDTO = objectMapper.readValue(task.getTaskData(), LessonDTO.class);
                    lessonService.createLesson(lessonDTO);
                }
                default -> throw new IllegalArgumentException("Unknown task type: " + task.getTaskType());
            }
            task.setStatus("COMPLETED");
        } catch (Exception e) {
            e.printStackTrace();
            task.setStatus("FAILED");
        } finally {
            scheduledTaskRepository.save(task);
            scheduledTasks.remove(task.getId());
        }
    }

    @Override
    public boolean cancelScheduledTask(Long taskId) {
        ScheduledTask task = scheduledTaskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        ScheduledFuture<?> future = scheduledTasks.get(taskId);

        if (future != null && !future.isDone()) {
            if (future.cancel(true)) {
                task.setStatus("CANCELLED");
            } else {
                task.setStatus("CANCELLATION_FAILED");
            }
            scheduledTaskRepository.save(task);
            scheduledTasks.remove(taskId);
            return true;
        }

        return false;
    }

    @PreDestroy
    public void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException ex) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
