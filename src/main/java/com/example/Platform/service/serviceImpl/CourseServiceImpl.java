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
import com.example.Platform.entity.CourseImage;
import com.example.Platform.entity.Level;
import com.example.Platform.entity.Topic;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.repository.CourseImageRepository;
import com.example.Platform.repository.CourseRepository;
import com.example.Platform.repository.LevelRepository;
import com.example.Platform.repository.TopicRepository;
import com.example.Platform.response.CourseResponse;
import com.example.Platform.service.CourseService;
import com.example.Platform.service.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final LevelRepository levelRepository;
    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;
    private final CourseImageRepository courseImageRepository;
    private final FileService fileService;
    private final CourseConverter courseConverter;


    @Override
    public Course createCourse(CourseDTO courseDTO) throws IOException {
        Level exLevel = levelRepository.findById(courseDTO.getLevelId())
                .orElseThrow(() -> new DataNotFoundException("Level not found!"));

        Set<Topic> topics = new HashSet<>();
        for (Long topicId : courseDTO.getTopicId()) {
            Topic exTopic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new DataNotFoundException("Topic not found!"));
            topics.add(exTopic);
        }
        Course newCourse = courseConverter.toEntity(courseDTO);


        Set<CourseImage> images = new HashSet<>();
        for (MultipartFile image : courseDTO.getImages()) {
            // Store the image file and get its storage URI
            String uri = fileService.storeFile(image);
            // Create a new CourseImage object and set its course and imageUrl properties
            CourseImage courseImage = CourseImage.builder()
                    .course(newCourse) // Associate this image with the current course
                    .imageUrl(uri)     // Set the image URL obtained from the storage service
                    .build();

            // Add the newly created CourseImage object to the images set
            images.add(courseImage);
        }
        newCourse.setImages(images);
        newCourse.setLevel(exLevel);
        newCourse.setTopics(topics);
        exLevel.getCourse().add(newCourse);

        return courseRepository.save(newCourse);
    }

    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Not found"));
    }

    @Override
    public Page<CourseResponse> getAllCourses(Pageable pageable, String keyword, Long levelId, Long topicId) {
        Page<Course> courses = courseRepository.findByFilter(pageable, keyword, levelId, topicId);
        return courses.map(CourseResponse::fromCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        Course exCourse = courseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Not exist!"));
        if (exCourse != null) {
            exCourse.setIsActive(false);
            courseRepository.save(exCourse);
        }
    }

    @Override
    @Transactional
    public Course updateCourse(CourseDTO courseDTO, Long id) throws IOException {
        Course existCourse = courseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Not exist!"));

        int newImagesSize = (courseDTO.getImages() != null) ? courseDTO.getImages().size() : 0;
        int totalImg = existCourse.getImages().size() + newImagesSize;

        if (totalImg > 3) {
            throw new IllegalArgumentException("Image has reached maximum!");
        }
        courseConverter.updateEntity(courseDTO, existCourse);

        if (courseDTO.getImages() != null) {
            for (MultipartFile file : courseDTO.getImages()) {
                CourseImage courseImage = convertMultipartFileToCourseImage(file);
                courseImage.setCourse(existCourse);
                existCourse.getImages().add(courseImage);
            }
        }

        Set<Topic> topics = new HashSet<>();
        for (Long topicId : courseDTO.getTopicId()) {
            Topic exTopic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new DataNotFoundException("Topic not found!"));
            topics.add(exTopic);
        }
        existCourse.setTopics(topics);
        courseRepository.save(existCourse);
        return existCourse;
    }
    public CourseImage convertMultipartFileToCourseImage(MultipartFile file) throws IOException {
        CourseImage image = new CourseImage();
        image.setImageUrl(file.getOriginalFilename());
        return image;
    }

    @Override
    public Course addImage(Long courseId, List<MultipartFile> images) throws IOException {
        Course existCourse = courseRepository.findById(courseId).orElseThrow(null);
        int totalImg = existCourse.getImages().size() + images.size();
        if(totalImg > 3) {
            throw new IllegalArgumentException("Cannot upload more than 3 images");
        }
        for (MultipartFile image : images) {
            String url = fileService.storeFile(image);
            CourseImage courseImage = CourseImage.builder().imageUrl(url).course(existCourse).build();
            existCourse.getImages().add(courseImage);
        }
        return courseRepository.save(existCourse);
    }


    @Override
    @Transactional
    public Course removeImageById(Long imageId) throws IOException {
        CourseImage courseImage = courseImageRepository.findById(imageId)
                .orElseThrow(() -> new DataNotFoundException("Image not found"));

        Course course = courseImage.getCourse();
        course.getImages().remove(courseImage);

        try {
            // Lấy đường dẫn tương đối từ đường dẫn tuyệt đối
            Path path = Paths.get(courseImage.getImageUrl()).getFileName();
            fileService.deleteFile(path.toString());
        } catch (IOException e) {
            System.err.println("Error deleting file: " + courseImage.getImageUrl());
            e.printStackTrace();
            throw new IOException("Error deleting file: " + courseImage.getImageUrl(), e);
        }
        courseImageRepository.delete(courseImage);

        return courseRepository.save(course);
    }



}
