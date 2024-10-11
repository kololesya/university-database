package com.laba.solvd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scholarship {
    private Long id;
    private Long studentId;
    private Double scholarshipAmount;
    private LocalDateTime awardDate;
}
