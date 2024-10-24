package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.ScholarshipDao;
import com.laba.solvd.dao.repoImpl.ScholarshipRepo;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.service.ScholarshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ScholarshipServiceImpl implements ScholarshipService {
    private static final Logger logger = LoggerFactory.getLogger(ScholarshipRepo.class.getName());
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
    public Scholarship findByStudentId(Long studentId) throws InterruptedException {
        return scholarshipRepo.findById(studentId);
    }

    @Override
    public Scholarship update(Scholarship scholarship) throws InterruptedException {
        Scholarship existScholarship = findById(scholarship.getId());

        if (existScholarship == null) {
            logger.error("Scholarship with ID " + scholarship.getId() + " was not found.");
            return null;
        }
        existScholarship.setScholarshipAmount(scholarship.getScholarshipAmount());
        existScholarship.setAwardDate(scholarship.getAwardDate());

        scholarshipRepo.update(existScholarship);
        return existScholarship;
    }
}
