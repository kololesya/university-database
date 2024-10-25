package com.laba.solvd.dao.repoImpl;

import com.laba.solvd.dao.CourseDao;
import com.laba.solvd.model.Course;
import com.laba.solvd.model.Grade;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepo implements CourseDao {
    private final static ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(CourseRepo.class.getName());
    private List<Grade> grades = new ArrayList<>();
    private Course mapCourse(ResultSet rs) throws SQLException {
        return new Course(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getByte("credits")
        );
    }

    @Override
    public void create(Course course) {
        String query = "INSERT INTO Courses (name, credits) VALUES (?, ?)";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, course.getName());
            stmt.setByte(2, course.getCredits());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                course.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Error creating course: {}", course, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM Courses WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting course with ID " + id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Course findById(Long id) {
        String query = "SELECT * FROM Courses WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        Course course = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                course = mapCourse(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding course with ID " + id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return course;
    }

    @Override
    public List<Course> findAll() {
        String query = "SELECT * FROM Courses";
        List<Course> courses = new ArrayList<>();
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Course course = mapCourse(rs);
                courses.add(course);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all courses", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return courses;
    }

    public Course update(Course course) {
        String query = "UPDATE Courses SET name = ?, credits = ? WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, course.getName());
            stmt.setByte(2, course.getCredits());
            stmt.setLong(3, course.getId());

            int rowsAffected = stmt.executeUpdate();

            // Проверяем, были ли затронуты строки (это значит, что обновление прошло успешно)
            if (rowsAffected > 0) {
                logger.info("Course with ID {} updated successfully.", course.getId());
                return course; // возвращаем обновленный курс
            } else {
                logger.warn("Course with ID {} not found for update.", course.getId());
                return null; // если запись не была найдена
            }

        } catch (SQLException e) {
            logger.error("Error updating course: {}", course, e);
            return null; // в случае ошибки возвращаем null
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }
}
