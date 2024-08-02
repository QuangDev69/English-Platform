package com.example.Platform.repository;

import com.example.Platform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByLesson_LessonId (Long lessonId);
}
