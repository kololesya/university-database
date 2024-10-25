package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.StudentDao;
import com.laba.solvd.dao.repoImpl.ScholarshipRepo;
import com.laba.solvd.dao.repoImpl.StudentRepo;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.model.Student;
import com.laba.solvd.service.IScholarshipService;
import com.laba.solvd.service.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StudentServiceImpl implements IStudentService {
    private static final Logger logger = LoggerFactory.getLogger(ScholarshipRepo.class.getName());
    private final StudentDao studentRepo;
    private final IScholarshipService scholarshipService;

    public StudentServiceImpl() {
        this.studentRepo = new StudentRepo();
        this.scholarshipService = new ScholarshipServiceImpl();
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
    public List<Student> findAll() throws InterruptedException {
        return studentRepo.findAll();
    }

    @Override
    public void create(Student student) {
        studentRepo.create(student);
    }

    @Override
    public Student update(Student student) {
        Student existStudent = findById(student.getId());

        if (existStudent == null) {
            logger.error("Student with ID " + student.getId() + " was not found.");
            return null;
        }

        existStudent.setEmail(student.getEmail());
        existStudent.setLastName(student.getLastName());
        existStudent.setFirstName(student.getFirstName());

        studentRepo.update(existStudent);
        return existStudent;
    }

    @Override
    public void deleteById(Long id) {
        studentRepo.deleteById(id);
    }

    public Scholarship getScholarshipsForStudent(Long studentId) throws InterruptedException {
        return scholarshipService.findByStudentId(studentId);
    }
}
