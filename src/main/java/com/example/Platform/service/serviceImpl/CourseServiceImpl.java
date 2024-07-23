package com.example.Platform.service.serviceImpl;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.service.CourseService;
import com.example.Platform.service.LevelService;
import com.example.Platform.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    public  final CourseService courseService;
    public  final LevelService levelService;
    public  final TopicService topicService;

    @Override
    public Course createCourse(CourseDTO courseDTO) {
        Course newCourse = courseService.createCourse(courseDTO);
        return null;
    }

    @Override
    public Course getById(Long id) {
        return null;
    }

    @Override
    public List<Course> getAllCourse() {
        return null;
    }

    @Override
    public void deleteCourse(Long id) {

    }

    @Override
    public void updateCourse(CourseDTO courseDTO, Long id) {

    }
}
