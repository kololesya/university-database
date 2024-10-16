package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.GenericDao;
import com.laba.solvd.dao.StudentRepo;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.model.Student;
import com.laba.solvd.service.ScholarshipService;
import com.laba.solvd.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final GenericDao<Student> studentRepo;
    private final ScholarshipService scholarshipService;

    public StudentServiceImpl(StudentRepo studentRepo, ScholarshipService scholarshipService) {
        this.studentRepo = studentRepo;
        this.scholarshipService = scholarshipService;
    }

    @Override
    public Student findById(Long id) {
        try {
            return studentRepo.findById(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("The student with ID: " + id + "doesn't exist", e);
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = studentRepo.findAll();
        for (Student student : students) {
            Scholarship scholarship = scholarshipService.findByStudentId(student.getId());
            student.setScholarship(scholarship);
        }
        return students;
    }

    @Override
    public void create(Student student) {
        studentRepo.create(student);
    }

    @Override
    public void update(Student student) {
        studentRepo.update(student);
    }

    @Override
    public void deleteById(Long id) {
        studentRepo.deleteById(id);
    }

    public Scholarship getScholarshipsForStudent(Long studentId) {
        return scholarshipService.findByStudentId(studentId);
    }
}
