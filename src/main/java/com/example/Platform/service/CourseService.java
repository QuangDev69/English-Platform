package com.example.Platform.service;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;

import java.util.List;

public interface CourseService {
    Course createCourse(CourseDTO courseDTO);
    Course getById(Long id);
    List<Course> getAllCourse();
    void deleteCourse(Long id);
    void updateCourse(CourseDTO courseDTO, Long id);
}
