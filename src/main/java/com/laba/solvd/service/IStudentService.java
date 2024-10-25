package com.laba.solvd.service;

import com.laba.solvd.model.Student;

import java.util.List;

public interface IStudentService {
    Student findById(Long id) throws InterruptedException;

    List<Student> findAll() throws InterruptedException;

    void create(Student student);

    Student update(Student student);

    void deleteById(Long id);
}
