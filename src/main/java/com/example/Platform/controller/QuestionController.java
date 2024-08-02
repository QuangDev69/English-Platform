package com.example.Platform.controller;

import com.example.Platform.dto.AnswerDTO;
import com.example.Platform.dto.LevelDTO;
import com.example.Platform.dto.QuestionDTO;
import com.example.Platform.entity.Question;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/questions")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/create/{lessonId}")
    public ResponseEntity<?> createQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable Long lessonId, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errMessage = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errMessage);
        }
        try {
            if (lessonId == null) {
                throw new DataNotFoundException("Not found lesson");
            }
            for (AnswerDTO answer : questionDTO.getAnswers()) {
                System.out.println("Answer Content: " + answer.getAnswerContent() + ", Is Correct: " + answer.isCorrect());
            }
            Question newQuestion = questionService.createQuestion(lessonId, questionDTO);
            return ResponseEntity.ok(newQuestion);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
        try {
            Question question = questionService.getQuestionById(id);
            return ResponseEntity.ok(question);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errMessage = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errMessage);
        }
        try {
            Question updatedQuestion = questionService.updateQuestion(id, questionDTO);
            return ResponseEntity.ok(updatedQuestion);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id){
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("Delete question " +id+" successfully!");
    }
}
