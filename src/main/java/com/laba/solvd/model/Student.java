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
@XmlRootElement(name = "student")
@XmlAccessorType(XmlAccessType.FIELD)
public class Student {
    private Long id;
    private Long universityId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime enrollmentDate;
    private Scholarship scholarship;
    private List<Club> clubs;
    private Room room;

    public Student(Long id, Long universityId, String firstName, String lastName,
                   String email, LocalDateTime enrollmentDate) {
        this.id = id;
        this.universityId = universityId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
        this.scholarship = null;
        this.clubs = null;
        this.room = null;
    }
}
