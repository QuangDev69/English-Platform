package com.example.Platform.controller;


import com.example.Platform.dto.LessonDTO;
import com.example.Platform.entity.Lesson;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.response.LessonResponse;
import com.example.Platform.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping("/")
    public ResponseEntity<?> createLesson (@Valid @RequestBody LessonDTO lessonDTO, BindingResult result) throws Exception {
        if(result.hasErrors()){
            List<String> errMessage = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errMessage);
        }
        Lesson lesson = lessonService.createLesson(lessonDTO);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonById(@PathVariable Long id) {
       try {
           Lesson existLesson = lessonService.getById(id);
           return ResponseEntity.ok(LessonResponse.fromLesson(existLesson));
       }catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @GetMapping("/{courseId}/all")
    public ResponseEntity<?> getAllLessonByCourseId(@PathVariable Long courseId) {
        try {
            List<LessonResponse> lessons = lessonService.getAllLessonByCourseId(courseId);
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@Valid @RequestBody LessonDTO lessonDTO, @PathVariable Long id, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            List<String> errMessage = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errMessage);
        }
        try {
            Lesson updatedLesson = lessonService.updateLesson(lessonDTO, id);
            return ResponseEntity.ok(updatedLesson);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
