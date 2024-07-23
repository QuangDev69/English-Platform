package com.example.Platform.controller;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    @GetMapping("/getAllCourses")
    public ResponseEntity<?> getAllCourses(){
        return ResponseEntity.ok("Get all success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoursesById(@PathVariable Long id){
        return ResponseEntity.ok("Get courses by " + id + " successfully!");
    }

    @PostMapping("/")
    public ResponseEntity<?> createCourses(){
        return ResponseEntity.ok("Get courses by successfully!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id){
        return ResponseEntity.ok("Delete " +id+ " successfully!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id){
        return ResponseEntity.ok("Update " +id+ " successfully!");
    }

}
