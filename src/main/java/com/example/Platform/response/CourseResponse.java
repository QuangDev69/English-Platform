package com.example.Platform.response;

import com.example.Platform.entity.Course;
import com.example.Platform.entity.CourseImage;
import com.example.Platform.entity.Topic;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse extends BaseResponse {
    private Long courseId;
    private String courseName;
    private String courseDes;
    private List<String> imageUrls;
    private Boolean isActive;
    private Long level;
    private List<Long> idTopics;


    public static CourseResponse fromCourse(Course course) {
//        List<Long> idTopics = new ArrayList<>();
//        for(Topic topic : course.getTopics()) {
//            Long id = topic.getTopicId();
//            idTopics.add(id);
//        }

        List<Long> idTopics = course.getTopics().stream()
                .map(Topic::getTopicId)
                .collect(Collectors.toList());

        List<String> imageUrls = course.getImages().stream()
                .map(CourseImage::getImageUrl)
                .collect(Collectors.toList());

        CourseResponse courseResponse = CourseResponse.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .courseDes(course.getCourseDes())
                .imageUrls(imageUrls)
                .isActive(course.getIsActive())
                .level(course.getLevel().getLevelId())
                .idTopics(idTopics)
                .build();
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setUpdatedAt(course.getUpdatedAt());
        return courseResponse;
    }
}
