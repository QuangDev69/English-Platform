package com.example.Platform.response;

import com.example.Platform.entity.Course;
import com.example.Platform.entity.Topic;
import lombok.*;

import java.util.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse extends BaseResponse{
    private Long courseId;
    private String courseName;
    private String courseDes;
    private String imagePath;
    private Long level;
    private List<Long> idTopics;


    public static CourseResponse fromEntityToRes(Course course) {
        List<Long> idTopics = new ArrayList<>();
        for(Topic topic : course.getTopics()) {
            Long id = topic.getTopicId();
            idTopics.add(id);
        }

        CourseResponse courseResponse = CourseResponse.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .courseDes(course.getCourseDes())
                .imagePath(course.getImagePath())
                .level(course.getLevel().getLevelId())
                .idTopics(idTopics)
                .build();
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setUpdatedAt(course.getUpdatedAt());
        return courseResponse;
    }
}
