package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.GenericDao;
import com.laba.solvd.dao.StudentRepo;
import com.laba.solvd.model.Student;
import com.laba.solvd.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final GenericDao<Student> studentRepo;

    public StudentServiceImpl(StudentRepo studentRepo) {
        this.studentRepo = new StudentRepo();
    }

    @Override
    public Student findById(Long id) {
        try {
            return studentRepo.findById(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при попытке найти студента с ID: " + id, e);
        }
    }

    @Override
    public List<Student> findAll() {
        return studentRepo.findAll();
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
}
