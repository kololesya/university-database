package com.laba.solvd.service;

import com.laba.solvd.model.Scholarship;

import java.util.List;

public interface IScholarshipService {
    void create(Scholarship scholarship);
    void deleteById(Long id);
    Scholarship findById(Long id) throws InterruptedException;
    List<Scholarship> findAll();
    void update(Scholarship scholarship);
}
