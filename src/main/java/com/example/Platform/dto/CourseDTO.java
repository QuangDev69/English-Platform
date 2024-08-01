package com.example.Platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CourseDTO {
    @NotBlank(message = "Course Name Can not be empty")
    @Size(min = 3, max = 100, message = "Must be between 3 to 100 char")
    private String courseName;

    @NotBlank(message = "Course Des Can not be empty")
    @Size(min = 3, max = 200, message = "Must be between 3 to 200 char")
    private String courseDes;

    @Size(max= 3, message = "Must be  3 file")
    private List<MultipartFile> images;

    @NotNull(message = "Topic Can not be empty")
    @Size(max = 3, message = "Only select up to 3 topics")
    private List<Long> topicId;

    @NotNull(message = "Level Can not be empty")
    private Long levelId;

    @NotNull(message = "Active type Can not be empty")
    private Long isActive;


}
