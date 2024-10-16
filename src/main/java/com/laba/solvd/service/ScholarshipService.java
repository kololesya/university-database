package com.laba.solvd.service;

import com.laba.solvd.model.Scholarship;

import java.util.List;

public interface ScholarshipService {
    void create(Scholarship scholarship);
    void deleteById(Long id);
    Scholarship findById(Long id);
    List<Scholarship> findAll();
    Scholarship findByStudentId(Long studentId);
    void update(Scholarship scholarship);
}
