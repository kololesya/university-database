package com.laba.solvd.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@XmlRootElement(name = "university")
@XmlAccessorType(XmlAccessType.FIELD)
public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime enrollmentDate;
    private Long universityId;
    private Scholarship scholarship;
    private List<Grade> grades;

    public Student(Long id, String firstName, String lastName, String email, LocalDateTime enrollmentDate, Long universityId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
        this.universityId = universityId;
        this.scholarship = null;
        this.grades = null;
    }
}
