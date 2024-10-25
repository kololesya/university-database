package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.CourseDao;
import com.laba.solvd.dao.repoImpl.CourseRepo;
import com.laba.solvd.dao.repoImpl.ScholarshipRepo;
import com.laba.solvd.model.Course;
import com.laba.solvd.service.ICourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CourseServiceImpl implements ICourseService {
    private static final Logger logger = LoggerFactory.getLogger(ScholarshipRepo.class.getName());
    private final CourseDao courseRepo;

    public CourseServiceImpl() {
        this.courseRepo = new CourseRepo();
    }

    @Override
    public void create(Course course) {
        if (course != null) {
            courseRepo.create(course);
            logger.info("Course created: {}", course);
        } else {
            logger.warn("Attempted to create a null course.");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (id != null && id > 0) {
            courseRepo.deleteById(id);
            logger.info("Course with ID {} deleted.", id);
        } else {
            logger.warn("Attempted to delete course with invalid ID: {}", id);
        }
    }

    @Override
    public Course findById(Long id) throws InterruptedException {
        return courseRepo.findById(id);
    }

    @Override
    public List<Course> findAll() {
        return courseRepo.findAll();
    }

    @Override
    public Course findByCourseId(Long courseId) throws InterruptedException {
        return courseRepo.findById(courseId);
    }

    @Override
    public Course update(Course course) throws InterruptedException {
        Course existCourse = findById(course.getId());

        if (existCourse == null) {
            logger.error("Course with ID " + course.getId() + " was not found.");
            return null;
        }

        existCourse.setName(course.getName());
        existCourse.setCredits(course.getCredits());

        courseRepo.update(existCourse);
        return existCourse;
    }

}
