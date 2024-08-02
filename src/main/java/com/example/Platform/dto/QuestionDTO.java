package com.example.Platform.dto;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDTO {
    private Long lessonId;
    private String questionContent;
    private List<AnswerDTO> answers;
}
