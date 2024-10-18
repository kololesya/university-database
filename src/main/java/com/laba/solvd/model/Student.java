package com.laba.solvd.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime enrollmentDate;
    private Long universityId;
    private Scholarship scholarship;
    private List<Club> clubs;
    private List<Grade> grades;

    public Student(Long id, String firstName, String lastName, String email, LocalDateTime enrollmentDate, Long universityId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
        this.universityId = universityId;
        this.scholarship = null;
        this.clubs = null;
    }
}
