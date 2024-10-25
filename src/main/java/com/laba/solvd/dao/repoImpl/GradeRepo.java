package com.laba.solvd.dao.repoImpl;

import com.laba.solvd.dao.GradeDao;
import com.laba.solvd.model.Grade;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeRepo implements GradeDao {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(GradeRepo.class);

    private Grade mapGrade(ResultSet rs) throws SQLException {
        Long studentId = rs.getLong("student_id");
        Long courseId = rs.getLong("course_id");
        String gradeValue = rs.getString("grade");

        return new Grade(studentId, courseId, gradeValue);
    }

    @Override
    public List<Grade> findAll() {
        String query = "SELECT * FROM Grades";
        List<Grade> grades = new ArrayList<>();
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Grade grade = mapGrade(rs);
                grades.add(grade);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all grades", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return grades;
    }

    @Override
    public void create(Grade grade) {
        String query = "INSERT INTO Grades (student_id, course_id, grade) VALUES (?, ?, ?)";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, grade.getStudentId());
            stmt.setLong(2, grade.getCourseId());
            stmt.setString(3, grade.getGrade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error creating grade: {}", grade, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Grade update(Grade grade) {
        String query = "UPDATE Grades SET grade = ? WHERE student_id = ? AND course_id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, grade.getGrade());
            stmt.setLong(2, grade.getStudentId());
            stmt.setLong(3, grade.getCourseId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return grade;
            } else {
                logger.warn("No rows updated for student_id: {} and course_id: {}", grade.getStudentId(), grade.getCourseId());
            }
        } catch (SQLException e) {
            logger.error("Error updating grade: {}", grade, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return null;
    }

    public void deleteByStudentIdAndCourseId(Long studentId, Long courseId) {
        String query = "DELETE FROM Grades WHERE student_id = ? AND course_id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, studentId);
            stmt.setLong(2, courseId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Grade for student_id {} and course_id {} was successfully deleted.", studentId, courseId);
            } else {
                logger.warn("No grade found for student_id {} and course_id {} to delete.", studentId, courseId);
            }
        } catch (SQLException e) {
            logger.error("Error deleting grade for student_id: {} and course_id: {}", studentId, courseId, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }


    public void deleteByStudentAndCourse(Long studentId, Long courseId) {
        String query = "DELETE FROM Grades WHERE student_id = ? AND course_id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, studentId);
            stmt.setLong(2, courseId);
            stmt.executeUpdate();
            logger.info("Grade for student_id {} and course_id {} was deleted.", studentId, courseId);
        } catch (SQLException e) {
            logger.error("Error deleting grade for student_id: {} and course_id: {}", studentId, courseId, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    public List<Grade> findByCourseId(Long courseId) {
        String query = "SELECT * FROM Grades WHERE course_id = ?";
        List<Grade> grades = new ArrayList<>();
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Grade grade = mapGrade(rs);
                grades.add(grade);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving grades for course_id: {}", courseId, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return grades;
    }

    public List<Grade> findByStudentId(Long studentId) {
        String query = "SELECT * FROM Grades WHERE student_id = ?";
        List<Grade> grades = new ArrayList<>();
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Grade grade = mapGrade(rs);
                grades.add(grade);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving grades for student_id: {}", studentId, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return grades;
    }

    public Grade findByStudentAndCourse(Long studentId, Long courseId) {
        String query = "SELECT * FROM Grades WHERE student_id = ? AND course_id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, studentId);
            stmt.setLong(2, courseId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapGrade(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding grade for student_id: {} and course_id: {}", studentId, courseId, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return null;
    }
}
