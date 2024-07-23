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

public class CourseDTO {
    @NotBlank(message = "Can not be empty")
    @Size(min = 3, max = 100, message = "Must be between 3 to 100 char")
    private String courseName;

    @NotBlank(message = "Can not be empty")
    @Size(min = 3, max = 200, message = "Must be between 3 to 200 char")
    private String courseDes;

    private String imagePath;
    private Long topicId;
    private Long levelId;
}
