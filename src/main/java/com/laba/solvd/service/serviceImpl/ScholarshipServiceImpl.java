package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.ScholarshipRepo;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.service.ScholarshipService;

import java.util.List;

public class ScholarshipServiceImpl implements ScholarshipService {
    private final ScholarshipRepo scholarshipRepo;

    public ScholarshipServiceImpl(ScholarshipRepo scholarshipRepo) {
        this.scholarshipRepo = scholarshipRepo;
    }

    @Override
    public void create(Scholarship scholarship) {
        scholarshipRepo.create(scholarship);
    }

    @Override
    public void deleteById(Long id) {
        scholarshipRepo.deleteById(id);
    }

    @Override
    public Scholarship findById(Long id) {
        return scholarshipRepo.findById(id);
    }

    @Override
    public List<Scholarship> findAll() {
        return scholarshipRepo.findAll();
    }

    @Override
    public Scholarship findByStudentId(Long studentId) {
        return scholarshipRepo.findByStudentId(studentId);
    }

    @Override
    public void update(Scholarship scholarship) {
        scholarshipRepo.update(scholarship);
    }
}
