package com.example.Platform.service.serviceImpl;

import com.example.Platform.convert.AnswerConverter;
import com.example.Platform.convert.QuestionConverter;
import com.example.Platform.dto.AnswerDTO;
import com.example.Platform.dto.QuestionDTO;
import com.example.Platform.entity.Answer;
import com.example.Platform.entity.Lesson;
import com.example.Platform.entity.Question;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.repository.LessonRepository;
import com.example.Platform.repository.QuestionRepository;
import com.example.Platform.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final LessonRepository lessonRepository;
    private final QuestionConverter questionConverter;
    private final AnswerConverter answerConverter;

    @Override
    public Question createQuestion(Long lessonId, QuestionDTO questionDTO) {
        Lesson existLesson = lessonRepository.findById(lessonId).orElseThrow(()-> new DataNotFoundException("Not found lesson!"));
        Question newQuestion = questionConverter.toEntity(questionDTO);
        newQuestion.setLesson(existLesson);

        Set<Answer> answers = new LinkedHashSet<>();
        for(AnswerDTO answerDTO : questionDTO.getAnswers()){
            Answer answer = answerConverter.toEntity(answerDTO);
            System.out.println("Answer Content: " + answer.getAnswerContent() + ", Is Correct: " + answer.isCorrect());
            answer.setQuestion(newQuestion);
            answers.add(answer);
        }
        newQuestion.setAnswers(answers);
        return questionRepository.save(newQuestion);
    }

    @Override
    public List<Question> getQuestionByLessonID(Long lessonId) {
        return questionRepository.findByLesson_LessonId(lessonId);
    }

    @Override
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(()-> new DataNotFoundException("Question id not found!"));
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    @Transactional

    public Question updateQuestion(Long questionId, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new DataNotFoundException("Question id not found!"));

        existingQuestion.setQuestionContent(questionDTO.getQuestionContent());

        // Lấy tập hợp các câu trả lời hiện có liên quan đến câu hỏi hiện có.
        Set<Answer> existingAnswers = existingQuestion.getAnswers();
        if(existingAnswers != null) {
            for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                if (answerDTO.getAnswerId() != null) {
                    Answer existingAnswer = existingAnswers.stream()
                            .filter(answer -> answer.getAnswerId().equals(answerDTO.getAnswerId()))
                            .findFirst()
                            .orElseThrow(() -> new DataNotFoundException("Answer id not found!"));
                    answerConverter.updateEntity(answerDTO, existingAnswer);

                }
            }
        }


        return questionRepository.save(existingQuestion);
    }

}
