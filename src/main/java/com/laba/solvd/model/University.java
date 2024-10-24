package com.laba.solvd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class University {
    private Long id;
    private String name;
    private String location;
    private int foundedYear;
    private List<Department> departments;
    private List<Student> students;
    private List<Dormitory> dormitories;
}