package com.laba.solvd;

import com.laba.solvd.dao.ScholarshipRepo;
import com.laba.solvd.dao.StudentRepo;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.model.Student;
import com.laba.solvd.service.ScholarshipService;
import com.laba.solvd.service.StudentService;
import com.laba.solvd.service.serviceImpl.ScholarshipServiceImpl;
import com.laba.solvd.service.serviceImpl.StudentServiceImpl;
import com.laba.solvd.utils.ConnectionPool;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        StudentRepo studentRepo = new StudentRepo();
        ScholarshipRepo scholarshipRepo = new ScholarshipRepo();

        ScholarshipService scholarshipService = new ScholarshipServiceImpl(scholarshipRepo);
        StudentService studentService = new StudentServiceImpl(studentRepo, scholarshipService);

        Student newStudent = new Student(null, "Alice", "Johnson", "alice.johnson@example.com", LocalDateTime.now(), 1L);

        studentService.create(newStudent);

        Scholarship newScholarship = new Scholarship(null, 3L, 1500.00, LocalDateTime.now());
        scholarshipService.create(newScholarship);

        List<Student> students = studentService.findAll();
        System.out.println("All Students:");
        for (Student student : students) {
            System.out.println(student);
        }

        Student studentToUpdate = studentRepo.findById(3L);
        studentToUpdate.setFirstName("UpdatedFirstName");
        studentToUpdate.setLastName("UpdatedLastName");
        studentToUpdate.setEmail("updated.email@example.com");
        studentToUpdate.setUniversityId(2L);

        studentRepo.update(studentToUpdate);

        System.out.println(connectionPool.getActiveConnectionsCount());
    }
}