package com.example.Platform.repository;

import com.example.Platform.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE " +
            "(:keyword IS NULL OR c.courseName LIKE %:keyword% OR c.courseDes LIKE %:keyword%) AND " +
            "(:levelId IS NULL OR c.level.levelId = :levelId) AND " +
            "(:topicId IS NULL OR :topicId MEMBER OF c.topics)")

    Page<Course> findByFilter(
            Pageable pageable,String keyword,Long levelId,Long topicId);
}
