package com.example.Platform.repository;

import com.example.Platform.entity.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseImageRepository extends JpaRepository<CourseImage, Long> {
}
