package com.example.Platform.service.serviceImpl;

import com.example.Platform.convert.LessonConvert;
import com.example.Platform.dto.LessonDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.entity.Lesson;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.repository.CourseRepository;
import com.example.Platform.repository.LessonRepository;
import com.example.Platform.response.LessonResponse;
import com.example.Platform.service.LessonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonConvert lessonConvert;

    @Override
    public Lesson createLesson(LessonDTO lessonDTO) throws Exception {
        Course existCourse = courseRepository.findById(lessonDTO.getCourseId()).orElseThrow(()->
                                                    new DataNotFoundException("Course id not found!"));
        Lesson newLesson = lessonConvert.toEntity(lessonDTO);
        newLesson.setCourse(existCourse);
        return lessonRepository.save(newLesson);
    }

    @Override
    public Lesson getById(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Lesson not found"));

    }

    @Override
    public List<LessonResponse> getAllLessonByCourseId(Long courseId) {
        List<Lesson> lessons = lessonRepository.findByCourse_CourseId(courseId);
        return lessons.stream().map(LessonResponse::fromLesson).collect(Collectors.toList());
    }

    @Override
    @Transactional

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    @Transactional

    public Lesson updateLesson(LessonDTO lessonDTO, Long id) throws IOException {
        Lesson existLesson = lessonRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Lesson not found"));

        lessonConvert.updateEntity(lessonDTO, existLesson);

        // Nếu có cập nhật courseId
        if (lessonDTO.getCourseId() != null && !lessonDTO.getCourseId().equals(existLesson.getCourse().getCourseId())) {
            Course existCourse = courseRepository.findById(lessonDTO.getCourseId()).orElseThrow(() ->
                    new DataNotFoundException("Course id not found!"));
            existLesson.setCourse(existCourse);
        }

        return lessonRepository.save(existLesson);
    }
}
