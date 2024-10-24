package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.impl.StudentRepo;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.model.Student;
import com.laba.solvd.service.IScholarshipService;
import com.laba.solvd.service.IStudentService;

import java.util.List;

public class StudentServiceImpl implements IStudentService {
    private final StudentRepo studentRepo;
    private final IScholarshipService scholarshipService;

    public StudentServiceImpl() {
        this.studentRepo = new StudentRepo();
        this.scholarshipService = new ScholarshipServiceImpl();
    }

    @Override
    public Student findById(Long id) {
        return studentRepo.findById(id);
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = studentRepo.findAll();
//        for (Student student : students) {
//            Scholarship scholarship = scholarshipService.findByStudentId(student.getId());
//            student.setScholarship(scholarship);
//        }
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

    @Override
    public void assignScholarshipToStudent(Long studentId, Long scholarshipId) {
        studentRepo.assignScholarshipToStudent(studentId, scholarshipId);
    }

    @Override
    public void getStudentWithClubInfo(Long studentId){
        studentRepo.getStudentWithClubInfo(studentId);
    }

    @Override
    public void getStudentWithScholarshipInfo(Long studentId){
        studentRepo.getStudentWithScholarshipInfo(studentId);
    }
}
