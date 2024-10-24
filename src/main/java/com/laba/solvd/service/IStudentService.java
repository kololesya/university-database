package com.laba.solvd.service;

import com.laba.solvd.model.Student;

import java.util.List;

public interface IStudentService {
    Student findById(Long id) throws InterruptedException;

    List<Student> findAll();

    void create(Student student);

    void update(Student student);

    void deleteById(Long id);

    void assignScholarshipToStudent(Long studentId, Long scholarshipId);

    void getStudentWithClubInfo(Long studentId);

    void getStudentWithScholarshipInfo(Long studentId);
}
