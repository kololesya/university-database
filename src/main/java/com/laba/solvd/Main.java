package com.laba.solvd;

import com.laba.solvd.model.Scholarship;
import com.laba.solvd.model.Student;
import com.laba.solvd.service.ScholarshipService;
import com.laba.solvd.service.StudentService;
import com.laba.solvd.service.serviceImpl.ScholarshipServiceImpl;
import com.laba.solvd.service.serviceImpl.StudentServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ScholarshipService scholarshipService = new ScholarshipServiceImpl();
        StudentService studentService = new StudentServiceImpl();

//        Student newStudent = new Student(null, "Alice", "Johnson", "alice.johnson@example.com",
//                LocalDateTime.now(), 1L);
//
//        studentService.create(newStudent);
//
//        Scholarship newScholarship = new Scholarship(1L, 3L, 1350.00, LocalDateTime.now());
//        scholarshipService.update(newScholarship);
//
//        List<Student> students = studentService.findAll();
//        System.out.println("All Students:");
//        for (Student student : students) {
//            System.out.println(student);
//        }
//
//        System.out.println(studentService.findById(students.get(5).getId()));
//
//        List<Scholarship> scholarships = scholarshipService.findAll();
//        for (Scholarship scholarship : scholarships){
//            System.out.println(scholarship);
//        }
//
//        Student studentToUpdate = new Student(1L, "Olesya", "Kolenchenko", "kololesya@gmail.com",
//                LocalDateTime.now(), 1L);
//        studentService.update(studentToUpdate);
    }
}