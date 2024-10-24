package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.ScholarshipDao;
import com.laba.solvd.dao.impl.ScholarshipRepo;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.service.IScholarshipService;

import java.util.List;

public class ScholarshipServiceImpl implements IScholarshipService {
    private final ScholarshipDao scholarshipRepo;

    public ScholarshipServiceImpl() {
        this.scholarshipRepo = new ScholarshipRepo();
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
    public Scholarship findById(Long id) throws InterruptedException {
        return scholarshipRepo.findById(id);
    }

    @Override
    public List<Scholarship> findAll() {
        return scholarshipRepo.findAll();
    }

    @Override
    public void update(Scholarship scholarship) {
        scholarshipRepo.update(scholarship);
    }
}
