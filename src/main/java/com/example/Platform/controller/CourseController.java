package com.example.Platform.controller;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> getCoursesById(@PathVariable Long id){
        return ResponseEntity.ok("Get courses by " + id + " successfully!");
    }

    @PostMapping("/")
    public ResponseEntity<?> createCourses(@Valid @RequestBody CourseDTO courseDTO, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            List<String> errMessage= result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errMessage);
        }

        Course newCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.ok("Create courses by successfully! "+ newCourse);
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
