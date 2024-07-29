package com.example.Platform.service;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.response.CourseResponse;

import java.util.List;

public interface CourseService {
    Course createCourse(CourseDTO courseDTO) throws Exception;
    Course getById(Long id);
    List<Course> getAllCourse();
    void deleteCourse(Long id);
    void updateCourse(CourseDTO courseDTO, Long id);
}
