package com.example.Platform.controller;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.response.CourseResponse;
import com.example.Platform.response.LessonResponse;
import com.example.Platform.response.ListCourseResponse;
import com.example.Platform.service.CourseService;
import com.example.Platform.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final LessonService lessonService;


    @GetMapping("/getAllCourses")
    public ResponseEntity<?> getAllCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long levelId,
            @RequestParam(required = false) Long topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int limit) {

            PageRequest pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("createdAt").descending()
//                    Sort.by("id").ascending()
            );
            Page<CourseResponse> pageCourses = courseService.getAllCourses(pageRequest, keyword, levelId, topicId);
            int size = pageCourses.getTotalPages();
            List<CourseResponse> courses = pageCourses.getContent();
        return ResponseEntity.ok(ListCourseResponse
                .builder()
                .courses(courses)
                .totalPage(size)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoursesById(@PathVariable("id") Long courseId) {
        try {
            Course existCourse = courseService.getById(courseId);
            return ResponseEntity.ok(CourseResponse.fromCourse(existCourse));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCourses(
            @Valid @ModelAttribute CourseDTO courseDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errMessage = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errMessage);
        }

        try {
            if (courseDTO.getImages() == null) {
                courseDTO.setImages(new ArrayList<>());
            }
            Course newCourse = courseService.createCourse(courseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Delete " + id + " successfully!");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> updateCourse(@Valid @ModelAttribute CourseDTO courseDTO, @PathVariable Long id, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errMessage = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errMessage);
        }

        try {
            Course existCourse = courseService.getById(id);
            int newImagesSize = (courseDTO.getImages() != null) ? courseDTO.getImages().size() : 0;
            int totalImg = existCourse.getImages().size() + newImagesSize;

            if (totalImg > 3) {
                throw new IllegalArgumentException("Image has reached maximum!");
            }

            Course newCourse = courseService.updateCourse(courseDTO, id);
            return ResponseEntity.ok(newCourse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/addImage")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> addImageToCourse(@PathVariable Long id, @RequestParam("image") List<MultipartFile> images) {
        try {
            Course updatedCourse = courseService.getById(id);

            if(updatedCourse.getImages().size() >= 3 || images.size() > 3) {
                return ResponseEntity.badRequest().body("The course image is enough");
            }else {
                updatedCourse = courseService.addImage(id, images);
                return ResponseEntity.ok(updatedCourse);
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving image");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/removeImage/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> removeImage(@PathVariable Long imageId) {
        try {
            Course updatedCourse = courseService.removeImageById(imageId);
            return ResponseEntity.ok(updatedCourse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/allLesson")
    public ResponseEntity<?> getAllLessonByCourseId(@PathVariable Long id) {
        try {
            List<LessonResponse> lessons = lessonService.getAllLessonByCourseId(id);
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
