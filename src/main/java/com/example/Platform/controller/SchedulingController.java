package com.example.Platform.controller;


import com.example.Platform.dto.CourseDTO;
import com.example.Platform.service.SchedulingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.prefix}/schedules")
@RequiredArgsConstructor
public class SchedulingController {

    private final SchedulingService schedulingService;

    @PostMapping("/course")
    public ResponseEntity<String> scheduleCourseCreation(@Valid @ModelAttribute CourseDTO courseDTO,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime) throws Exception {
        schedulingService.scheduleTask("CreateCourse", "CreateCourse", courseDTO, scheduledTime);
        return ResponseEntity.ok("Course creation scheduled successfully.");
    }


    @DeleteMapping("/cancel/{taskId}")
    public ResponseEntity<String> cancelScheduledTask(@PathVariable Long taskId) {
        boolean isCancelled = schedulingService.cancelScheduledTask(taskId);
        if (isCancelled) {
            return ResponseEntity.ok("Task cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found or already executed.");
        }
    }

//    @PostMapping("/lesson")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> scheduleLessonCreation(@Valid @ModelAttribute LessonDTO lessonDTO,
//                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime,
//                                                    BindingResult result) {
//        if (result.hasErrors()) {
//            List<String> errMessage = result.getFieldErrors().stream()
//                    .map(FieldError::getDefaultMessage)
//                    .collect(Collectors.toList());
//            return ResponseEntity.badRequest().body(errMessage);
//        }
//
//        try {
//            lessonService.scheduleLessonCreation(lessonDTO, scheduledTime);
//            return ResponseEntity.ok("Lesson creation scheduled successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
}