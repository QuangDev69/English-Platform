package com.example.Platform.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course  extends BasenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String courseName;
    private String courseDes;
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "level_id")
    @JsonManagedReference
    private Level level;

    @ManyToMany
    @JoinTable(
            name = "course_topic",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> topics  = new HashSet<>();
}
