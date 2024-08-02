package com.example.Platform.service;

import com.example.Platform.dto.QuestionDTO;
import com.example.Platform.entity.Question;

import java.util.List;

public interface QuestionService {
    Question createQuestion(Long lessonId, QuestionDTO questionDTO);

    List<Question> getQuestionByLessonID(Long lessonId);
    Question getQuestionById(Long questionId);
    void deleteQuestion(Long id);
    Question updateQuestion(Long quesionId, QuestionDTO questionDTO);
}
