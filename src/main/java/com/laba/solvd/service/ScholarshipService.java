package com.laba.solvd.service;

import com.laba.solvd.model.Scholarship;

import java.util.List;

public interface ScholarshipService {
    void create(Scholarship scholarship);
    void deleteById(Long id);
    Scholarship findById(Long id) throws InterruptedException;
    List<Scholarship> findAll();
    Scholarship findByStudentId(Long studentId) throws InterruptedException;
    Scholarship update (Scholarship scholarship) throws InterruptedException;
}
