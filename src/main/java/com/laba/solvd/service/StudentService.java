package com.laba.solvd.service;

import com.laba.solvd.model.Student;

import java.util.List;

public interface StudentService {
    Student findById(Long id) throws InterruptedException;

    List<Student> findAll();

    void create(Student student);

    void update(Student student);

    void deleteById(Long id);
}
