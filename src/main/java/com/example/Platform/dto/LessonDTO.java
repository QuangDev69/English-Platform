package com.example.Platform.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonDTO {

    @NotBlank(message = "Can not be empty")
    @Size(min = 3, max = 200, message = "Must be between 3 to 200 char")
    private String lessonName;

    @NotBlank(message = "Can not be empty")
    @Size(min = 3, max = 200, message = "Must be between 3 to 200 char")
    private String lessonDes;

    @NotBlank(message = "Can not be empty")
    @Size(min = 3, max = 8, message = "Must be between 3 to 8 char")
    private String code;

    @NotBlank(message = "Can not be empty")
    private String content;

    private Long courseId;
}
