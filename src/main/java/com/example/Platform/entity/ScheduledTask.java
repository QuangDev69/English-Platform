package com.example.Platform.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;

    private String taskType;

    @Lob
    private String taskData;

    private LocalDateTime scheduledTime;

    private String status; // Ví dụ: "PENDING", "COMPLETED"
}