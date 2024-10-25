package com.laba.solvd.service;

import com.laba.solvd.model.Grade;
import com.laba.solvd.model.Student;

import java.util.List;

public interface IGradeService {
    List<Grade> findAll() throws InterruptedException;

    void create(Grade grade);

    Grade update(Grade grade);

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByCourseId(Long courseId);

    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);
}
