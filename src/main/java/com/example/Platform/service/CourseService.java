package com.example.Platform.service;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    Course createCourse(CourseDTO courseDTO) throws Exception;

    Course getById(Long id);

    Page<CourseResponse> getAllCourses(Pageable pageable, String keyword, Long levelId, Long topicId);

    void deleteCourse(Long id);

    Course updateCourse(CourseDTO courseDTO, Long id) throws IOException;


    Course addImage(Long courseId, List<MultipartFile> images) throws IOException;

    Course removeImageById(Long imageId) throws IOException;
//    Course removeImage(Long courseId, String imagePath) throws IOException;
}
