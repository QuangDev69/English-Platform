package com.example.Platform.controller;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.response.CourseResponse;
import com.example.Platform.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/getAllCourses")
    public ResponseEntity<?> getAllCourses(){
        return ResponseEntity.ok("Get all success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoursesById(@PathVariable("id") Long courseId){
        try {
            Course existCourse = courseService.getById(courseId);
            return ResponseEntity.ok(CourseResponse.fromEntityToRes(existCourse));

        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping("/")
    public ResponseEntity<?> createCourses(
            @ModelAttribute @Valid CourseDTO courseDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errMessage = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errMessage);
        }

        try {
            Course newCourse = courseService.createCourse(courseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id){
        return ResponseEntity.ok("Delete " + id + " successfully!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id){
        return ResponseEntity.ok("Update " + id + " successfully!");
    }

}
