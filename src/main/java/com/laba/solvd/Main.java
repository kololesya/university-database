package com.laba.solvd;

import com.laba.solvd.model.Course;
import com.laba.solvd.model.Grade;
import com.laba.solvd.model.Student;
import com.laba.solvd.service.ICourseService;
import com.laba.solvd.service.IGradeService;
import com.laba.solvd.service.IScholarshipService;
import com.laba.solvd.service.IStudentService;
import com.laba.solvd.service.serviceImpl.CourseServiceImpl;
import com.laba.solvd.service.serviceImpl.GradeServiceImpl;
import com.laba.solvd.service.serviceImpl.ScholarshipServiceImpl;
import com.laba.solvd.service.serviceImpl.StudentServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        IScholarshipService scholarshipService = new ScholarshipServiceImpl();
        IStudentService studentService = new StudentServiceImpl();
        IGradeService gradeService = new GradeServiceImpl();
        ICourseService courseService = new CourseServiceImpl();

        List<Course> courses = courseService.findAll();
        for (Course course : courses){
            System.out.println(course);
        }

//        Grade grade1 = new Grade(3L, 3L, "B");
//        gradeService.create(grade1);
//        List<Grade> grades =  gradeService.findAll();
//        List<Grade> grades = gradeService.findByStudentId(2L);
        List<Grade> grades = gradeService.findByCourseId(3L);
        for(Grade grade : grades){
            System.out.println(grade);
        }

//        Grade newGrade = new Grade(3L, 3L, "C");
//        gradeService.update(newGrade);
//
//        gradeService.deleteByStudentIdAndCourseId(3L, 3L);


//        Student newStudent = new Student(null, "Alice", "Johnson", "alice.johnson@example.com",
//                LocalDateTime.now(), 1L);
//
//        studentService.create(newStudent);
//
//        Scholarship newScholarship = new Scholarship(1L, 3L, 1350.00, LocalDateTime.now());
//        scholarshipService.update(newScholarship);
//
        List<Student> students = studentService.findAll();
        System.out.println("All Students:");
        for (Student student : students) {
            System.out.println(student);
        }
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