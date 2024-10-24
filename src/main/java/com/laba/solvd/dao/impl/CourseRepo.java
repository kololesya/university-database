//package com.laba.solvd.dao;
//
//import com.laba.solvd.model.Course;
//import com.laba.solvd.utils.ConnectionPool;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class CourseRepo implements GenericDao<Course>{
//    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
//    private static final Logger logger = LoggerFactory.getLogger(CourseRepo.class.getName());
//    private List<Grade> grades = new ArrayList<>();
//    private Course mapCourse(ResultSet rs) throws SQLException {
//        return new Course(
//                rs.getLong("id"),
//                rs.getString("name"),
//                rs.getByte("credits"),
//                grades
//        );
//    }
//
//    @Override
//    public void create(Course course) {
//        String query = "INSERT INTO courses (name, credits) VALUES (?, ?)";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//        ResultSet resultSet = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//            stmt.setString(1, course.getName());
//            stmt.setByte(2, course.getCredits());
//            stmt.executeUpdate();
//
//            resultSet = stmt.getGeneratedKeys();
//            if (resultSet.next()) {
//                course.setId(resultSet.getLong(1));
//            }
//        } catch (SQLException e) {
//            logger.error("Error creating course: " + course, e);
//        } finally {
//            try {
//                if (resultSet != null) resultSet.close();
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
//        String query = "DELETE FROM courses WHERE id = ?";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setLong(1, id);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Error deleting course with ID: " + id, e);
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
//    public Course findById(Long id) {
//        String query = "SELECT * FROM courses WHERE id = ?";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setLong(1, id);
//            rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return mapCourse(rs);
//            }
//        } catch (SQLException e) {
//            logger.error("Error finding course with ID: " + id, e);
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (stmt != null) stmt.close();
//                if (connection != null) connectionPool.releaseConnection(connection);
//            } catch (SQLException e) {
//                logger.error("Error closing resources", e);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Course> findAll() {
//        String query = "SELECT c.id AS course_id, c.name, c.credits, g.student_id, g.value " +
//                "FROM courses c LEFT JOIN grades g ON c.id = g.course_id";
//        List<Course> courses = new ArrayList<>();
//        Map<Long, Course> courseMap = new HashMap<>();
//
//        try (Connection connection = connectionPool.getConnection();
//             Statement stmt = connection.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                Long courseId = rs.getLong("course_id");
//                Course course = courseMap.get(courseId);
//
//                if (course == null) {
//                    course = new Course();
//                    course.setId(courseId);
//                    course.setName(rs.getString("name"));
//                    course.setCredits(rs.getByte("credits"));
//                    course.setGrades(new ArrayList<>());
//                    courseMap.put(courseId, course);
//                    courses.add(course);
//                }
//
//                Long studentId = rs.getLong("student_id");
//                if (studentId != 0) {
//                    Grade grade = new Grade();
//                    grade.setStudentId(studentId);
//                    grade.setCourseId(courseId);
//                    grade.setGrade(rs.getString("grade"));
//                    course.getGrades().add(grade);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error("Error retrieving all courses with grades", e);
//        }
//        return courses;
//    }
//
//    @Override
//    public void update(Course course) {
//        String updateCourseQuery = "UPDATE courses SET name = ?, credits = ? WHERE id = ?";
//        String deleteGradesQuery = "DELETE FROM grades WHERE course_id = ?";
//        String insertGradeQuery = "INSERT INTO grades (student_id, course_id, grade) VALUES (?, ?, ?)";
//
//        Connection connection = null;
//        PreparedStatement updateStmt = null;
//        PreparedStatement deleteStmt = null;
//        PreparedStatement insertStmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            connection.setAutoCommit(false);
//
//            updateStmt = connection.prepareStatement(updateCourseQuery);
//            updateStmt.setString(1, course.getName());
//            updateStmt.setByte(2, course.getCredits());
//            updateStmt.setLong(3, course.getId());
//            updateStmt.executeUpdate();
//
//            deleteStmt = connection.prepareStatement(deleteGradesQuery);
//            deleteStmt.setLong(1, course.getId());
//            deleteStmt.executeUpdate();
//
//            insertStmt = connection.prepareStatement(insertGradeQuery);
//            for (Grade grade : course.getGrades()) {
//                insertStmt.setLong(1, grade.getStudentId());
//                insertStmt.setLong(2, grade.getCourseId());
//                insertStmt.setString(3, grade.getGrade());
//                insertStmt.addBatch();
//            }
//            insertStmt.executeBatch();
//
//            connection.commit();
//
//        } catch (SQLException e) {
//            logger.error("Error updating course: {}", course, e);
//            try {
//                connection.rollback();
//            } catch (SQLException rollbackException) {
//                logger.error("Error during rollback", rollbackException);
//            }
//        } finally {
//            try {
//                if (insertStmt != null) insertStmt.close();
//                if (deleteStmt != null) deleteStmt.close();
//                if (updateStmt != null) updateStmt.close();
//                if (connection != null) connectionPool.releaseConnection(connection);
//            } catch (SQLException e) {
//                logger.error("Error closing resources", e);
//            }
//        }
//    }
//
//}
