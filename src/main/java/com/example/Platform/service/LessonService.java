package com.example.Platform.service;


import com.example.Platform.dto.LessonDTO;
import com.example.Platform.entity.Lesson;
import com.example.Platform.response.LessonResponse;

import java.io.IOException;
import java.util.List;

public interface LessonService {

    Lesson createLesson(LessonDTO lessonDTO) throws Exception;

    Lesson getById(Long id);

    List<LessonResponse> getAllLessonByCourseId(Long courseId); // Thêm phương thức này

    void deleteLesson(Long id);

    Lesson updateLesson(LessonDTO lessonDTO, Long id) throws IOException;

}
