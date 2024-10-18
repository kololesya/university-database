package com.laba.solvd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Double salary;
    private List<Course> courses;
}
