//package com.laba.solvd.dao;
//
//import com.laba.solvd.model.Grade;
//import com.laba.solvd.utils.ConnectionPool;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GradeRepo implements GenericDao<Grade>{
//    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
//    private static final Logger logger = LoggerFactory.getLogger(GradeRepo.class);
//
//    @Override
//    public Grade findById(Long id) {
//        throw new UnsupportedOperationException("Find by ID is not supported for Grades table.");
//    }
//
//    @Override
//    public List<Grade> findAll() {
//        String query = "SELECT * FROM Grades";
//        List<Grade> grades = new ArrayList<>();
//        Connection connection = null;
//        Statement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.createStatement();
//            rs = stmt.executeQuery(query);
//
//            while (rs.next()) {
//                Grade grade = mapGrade(rs);
//                grades.add(grade);
//            }
//        } catch (SQLException e) {
//            logger.error("Error retrieving all grades", e);
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (stmt != null) stmt.close();
//                if (connection != null) connectionPool.releaseConnection(connection);
//            } catch (SQLException e) {
//                logger.error("Error closing resources", e);
//            }
//        }
//
//        return grades;
//    }
//
//    @Override
//    public void create(Grade grade) {
//        String query = "INSERT INTO Grades (student_id, course_id, grade) VALUES (?, ?, ?)";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setLong(1, grade.getStudentId());
//            stmt.setLong(2, grade.getCourseId());
//            stmt.setString(3, grade.getGrade());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Error creating grade: {}", grade, e);
//        } finally {
//            try {
//                if (stmt != null) stmt.close();
//                if (connection != null) connectionPool.releaseConnection(connection);
//            } catch (SQLException e) {
//                logger.error("Error closing resources", e);
//            }
//        }
//    }
//
//    @Override
//    public void update(Grade grade) {
//        String query = "UPDATE Grades SET grade = ? WHERE student_id = ? AND course_id = ?";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setString(1, grade.getGrade());
//            stmt.setLong(2, grade.getStudentId());
//            stmt.setLong(3, grade.getCourseId());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Error updating grade: {}", grade, e);
//        } finally {
//            try {
//                if (stmt != null) stmt.close();
//                if (connection != null) connectionPool.releaseConnection(connection);
//            } catch (SQLException e) {
//                logger.error("Error closing resources", e);
//            }
//        }
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        throw new UnsupportedOperationException("Delete by ID is not supported for Grades table.");
//    }
//
//    public void deleteByStudentAndCourse(Long studentId, Long courseId) {
//        String query = "DELETE FROM Grades WHERE student_id = ? AND course_id = ?";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setLong(1, studentId);
//            stmt.setLong(2, courseId);
//            stmt.executeUpdate();
//            logger.info("Grade for student_id {} and course_id {} was deleted.", studentId, courseId);
//        } catch (SQLException e) {
//            logger.error("Error deleting grade for student_id: {} and course_id: {}", studentId, courseId, e);
//        } finally {
//            try {
//                if (stmt != null) stmt.close();
//                if (connection != null) connectionPool.releaseConnection(connection);
//            } catch (SQLException e) {
//                logger.error("Error closing resources", e);
//            }
//        }
//    }
//
//    private Grade mapGrade(ResultSet rs) throws SQLException {
//        Long studentId = rs.getLong("student_id");
//        Long courseId = rs.getLong("course_id");
//        String gradeValue = rs.getString("grade");
//
//        return new Grade(studentId, courseId, gradeValue);
//    }
//}
