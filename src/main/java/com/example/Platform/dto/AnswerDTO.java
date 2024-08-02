package com.example.Platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerDTO {
    private Long answerId;  // Thêm thuộc tính answerId

    private String answerContent;

    @JsonProperty("isCorrect")
    private boolean isCorrect;
}

