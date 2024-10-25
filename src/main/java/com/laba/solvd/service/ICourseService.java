package com.laba.solvd.service;

import com.laba.solvd.model.Course;
import com.laba.solvd.model.Scholarship;

import java.util.List;

public interface ICourseService {
    void create(Course course);
    void deleteById(Long id);
    Course findById(Long id) throws InterruptedException;
    List<Course> findAll();
    Course findByCourseId(Long courseId) throws InterruptedException;
    Course update (Course course) throws InterruptedException;
}
