package com.laba.solvd.dao;

import com.laba.solvd.model.Student;

public interface StudentDao extends GenericDao<Student>{

    Student findByEmail(String email);
    void assignScholarshipToStudent(Long studentId, Long scholarshipId);
}
