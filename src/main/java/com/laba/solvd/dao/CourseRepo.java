package com.laba.solvd.dao;

import com.laba.solvd.model.Course;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepo implements GenericDao<Course>{
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(CourseRepo.class.getName());
    private Course mapCourse(ResultSet rs) throws SQLException {
        return new Course(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getByte("credits"),
                rs.getLong("professor_id"),
                rs.getLong("university_id")
        );
    }

    @Override
    public void create(Course course) {
        String query = "INSERT INTO courses (name, credits, professor_id, university_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, course.getName());
            stmt.setByte(2, course.getCredits());
            stmt.setLong(3, course.getProfessorId());
            stmt.setLong(4, course.getUniversityId());

            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()) {
                course.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Error creating course: " + course, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM courses WHERE id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting course with ID: " + id, e);
        }
    }

    @Override
    public Course findById(Long id) {
        String query = "SELECT * FROM courses WHERE id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapCourse(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding course with ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Course> findAll() {
        String query = "SELECT * FROM courses";
        List<Course> courses = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                courses.add(mapCourse(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all courses", e);
        }
        return courses;
    }

    @Override
    public void update(Course course) {
        String query = "UPDATE courses SET name = ?, credits = ?, professor_id = ?, university_id = ? WHERE id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, course.getName());
            stmt.setByte(2, course.getCredits());
            stmt.setLong(3, course.getProfessorId());
            stmt.setLong(4, course.getUniversityId());
            stmt.setLong(5, course.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating course: " + course, e);
        }
    }
}
