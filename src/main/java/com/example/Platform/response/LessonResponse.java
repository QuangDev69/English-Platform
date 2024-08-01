package com.example.Platform.response;

import com.example.Platform.dto.LessonDTO;
import com.example.Platform.entity.Lesson;
import lombok.*;

@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LessonResponse extends BaseResponse{
    private String lessonName;

    private String lessonDes;

    private String code;

    private String content;

    private Long courseId;

    public static LessonResponse fromLesson (Lesson lesson) {
        LessonResponse lessonResponse = LessonResponse.builder()
                .lessonName(lesson.getLessonName())
                .lessonDes(lesson.getLessonDes())
                .code(lesson.getCode())
                .content(lesson.getContent())
                .courseId(lesson.getCourse().getCourseId())
                .build();

        lessonResponse.setCreatedAt(lesson.getCreatedAt());
        lessonResponse.setUpdatedAt(lesson.getUpdatedAt());
        return  lessonResponse;
    }
}
