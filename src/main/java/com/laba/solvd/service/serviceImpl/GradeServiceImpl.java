package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.GradeDao;
import com.laba.solvd.dao.StudentDao;
import com.laba.solvd.dao.repoImpl.GradeRepo;
import com.laba.solvd.dao.repoImpl.ScholarshipRepo;
import com.laba.solvd.model.Grade;
import com.laba.solvd.service.IGradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GradeServiceImpl implements IGradeService {
    private static final Logger logger = LoggerFactory.getLogger(ScholarshipRepo.class.getName());
    private final GradeDao gradeDao;

    public GradeServiceImpl() {
        this.gradeDao = new GradeRepo();
        }

    @Override
    public List<Grade> findAll() throws InterruptedException {
        return gradeDao.findAll();
    }

    @Override
    public void create(Grade grade) {
        gradeDao.create(grade);
    }

    @Override
    public List<Grade> findByStudentId(Long studentId){
        return gradeDao.findByStudentId(studentId);
    }

    @Override
    public List<Grade> findByCourseId(Long courseId){
        return gradeDao.findByCourseId(courseId);
    }

    @Override
    public Grade update(Grade grade) {
        Grade existingGrade = gradeDao.findByStudentAndCourse(grade.getStudentId(), grade.getCourseId());

        if (existingGrade == null) {
            logger.error("Grade for student_id " + grade.getStudentId() + " and course_id " + grade.getCourseId() + " was not found.");
            return null;
        }

        existingGrade.setGrade(grade.getGrade());

        gradeDao.update(existingGrade);
        return existingGrade;
    }

    public void deleteByStudentIdAndCourseId(Long studentId, Long courseId){
        gradeDao.deleteByStudentIdAndCourseId(studentId, courseId);
    }
}
