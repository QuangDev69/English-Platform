//package com.example.Platform.service.serviceImpl;
//
//import com.example.Platform.convert.CourseConverter;
//import com.example.Platform.dto.CourseDTO;
//import com.example.Platform.entity.Course;
//import com.example.Platform.entity.Level;
//import com.example.Platform.entity.Topic;
//import com.example.Platform.exception.DataNotFoundException;
//import com.example.Platform.repository.LevelRepository;
//import com.example.Platform.repository.TopicRepository;
//import com.example.Platform.service.CourseService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class CourseServiceImpl implements CourseService {
//    private  final LevelRepository levelRepository;
//    private  final TopicRepository topicRepository;
//    private  final CourseConverter courseConverter;
//
//    @Override
//    public Course createCourse(CourseDTO courseDTO) {
//        Level exLevel = levelRepository.findById(courseDTO.getLevelId()).orElseThrow(()-> new DataNotFoundException("Level not found!"));
//        Topic exTopic = topicRepository.findById(courseDTO.getTopicId()).orElseThrow(()-> new DataNotFoundException("Topic  not found!"));
//
//        Course newCourse = courseConverter.toEntity(courseDTO);
//        newCourse.setLevel(exLevel);
//        Set<Topic> topics = new HashSet<>();
//        topics.add(exTopic);
//        newCourse.setTopics(topics);
//        return null;
//    }
//
//    @Override
//    public Course getById(Long id) {
//        return null;
//    }
//
//    @Override
//    public List<Course> getAllCourse() {
//        return null;
//    }
//
//    @Override
//    public void deleteCourse(Long id) {
//
//    }
//
//    @Override
//    public void updateCourse(CourseDTO courseDTO, Long id) {
//
//    }
//}

package com.example.Platform.service.serviceImpl;

import com.example.Platform.convert.CourseConverter;
import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.entity.Level;
import com.example.Platform.entity.Topic;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.repository.CourseRepository;
import com.example.Platform.repository.LevelRepository;
import com.example.Platform.repository.TopicRepository;
import com.example.Platform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final LevelRepository levelRepository;
    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;


    @Override
    public Course createCourse(CourseDTO courseDTO) {
        Level exLevel = levelRepository.findById(courseDTO.getLevelId())
                .orElseThrow(() -> new DataNotFoundException("Level not found!"));
        Topic exTopic = topicRepository.findById(courseDTO.getTopicId())
                .orElseThrow(() -> new DataNotFoundException("Topic not found!"));

        Course newCourse = courseConverter.toEntity(courseDTO);

        newCourse.setLevel(exLevel);
        Set<Topic> topics = new HashSet<>();
        topics.add(exTopic);
        newCourse.setTopics(topics);
        return courseRepository.save(newCourse);
    }

    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Not found"));
    }

    @Override
    public List<Course> getAllCourse() {
        return null;
    }

    @Override
    public void deleteCourse(Long id) {

    }

    @Override
    public void updateCourse(CourseDTO courseDTO, Long id) {

    }
}
