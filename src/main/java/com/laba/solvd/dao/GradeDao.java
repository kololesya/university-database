package com.laba.solvd.dao;

import com.laba.solvd.model.Grade;

import java.util.List;

public interface GradeDao{
    List<Grade> findAll();
    void create(Grade grade);
    Grade update(Grade t);

    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByCourseId(Long courseId);

    Grade findByStudentAndCourse(Long studentId, Long courseId);
    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);
}
