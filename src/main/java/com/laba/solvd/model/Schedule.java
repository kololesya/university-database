package com.laba.solvd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private Long id;
    private Long courseId;
    private Long classroomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
