//package com.laba.solvd.dao;
//
//import com.laba.solvd.model.Course;
//import com.laba.solvd.model.Professor;
//import com.laba.solvd.model.University;
//import com.laba.solvd.utils.ConnectionPool;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProfessorRepo implements GenericDao<Professor>{
//    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
//    private static final Logger logger = LoggerFactory.getLogger(CourseRepo.class.getName());
//
//    private Professor mapProfessor(ResultSet rs) throws SQLException {
//        Long id = rs.getLong("id");
//        String firstName = rs.getString("first_name");
//        String lastName = rs.getString("last_name");
//        String email = rs.getString("email");
//        Double salary = rs.getDouble("salary");
//
//        return new Professor(id, firstName, lastName, email, salary, null);
//    }
//
//    @Override
//    public Professor findById(Long id) {
//        String query = "SELECT * FROM professors WHERE id = ?";
//        String coursesQuery = "SELECT c.id, c.name, c.credits FROM courses c " +
//                "JOIN professor_courses pc ON c.id = pc.course_id WHERE pc.professor_id = ?";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//        PreparedStatement coursesStmt = null;
//        ResultSet rs = null;
//        ResultSet coursesRs = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setLong(1, id);
//            rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                Professor professor = mapProfessor(rs);
//
//                coursesStmt = connection.prepareStatement(coursesQuery);
//                coursesStmt.setLong(1, id);
//                coursesRs = coursesStmt.executeQuery();
//
//                List<Course> courses = new ArrayList<>();
//                while (coursesRs.next()) {
//                    courses.add(new Course(
//                            coursesRs.getLong("id"),
//                            coursesRs.getString("name"),
//                            coursesRs.getByte("credits"),
//                            null
//                    ));
//                }
//                professor.setCourses(courses);
//                return professor;
//            }
//        } catch (SQLException e) {
//            logger.error("Error finding professor with id: {}", id, e);
//        } finally {
//            try {
//                if (coursesRs != null) coursesRs.close();
//                if (coursesStmt != null) coursesStmt.close();
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
//    public List<Professor> findAll() {
//        String query = "SELECT * FROM professors";
//        List<Professor> professors = new ArrayList<>();
//        Connection connection = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            rs = stmt.executeQuery(query);
//
//            while (rs.next()) {
//                professors.add(mapProfessor(rs));
//            }
//        } catch (SQLException e) {
//            logger.error("Error retrieving all professors", e);
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
//        return professors;
//    }
//
//    @Override
//    public void create(Professor professor) {
//        String query = "INSERT INTO professors (first_name, last_name, email, salary) VALUES (?, ?, ?, ?)";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setString(1, professor.getFirstName());
//            stmt.setString(2, professor.getLastName());
//            stmt.setString(3, professor.getEmail());
//            stmt.setDouble(4, professor.getSalary());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Error creating professor: {}", professor, e);
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
//        String query = "DELETE FROM professors WHERE id = ?";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setLong(1, id);
//            stmt.executeUpdate();
//            logger.info("Professor with ID {} was deleted", id);
//        } catch (SQLException e) {
//            logger.error("Error deleting professor with id: {}", id, e);
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
//    public void update(Professor professor) {
//        String query = "UPDATE professors SET first_name = ?, last_name = ?, email = ?, salary = ? WHERE id = ?";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setString(1, professor.getFirstName());
//            stmt.setString(2, professor.getLastName());
//            stmt.setString(3, professor.getEmail());
//            stmt.setDouble(4, professor.getSalary());
//            stmt.setLong(5, professor.getId());
//            stmt.executeUpdate();
//
//            logger.info("Professor updated with ID: {}", professor.getId());
//
//            if (professor.getCourses() != null && !professor.getCourses().isEmpty()) {
//                for (Course course : professor.getCourses()) {
//                    addCourseToProfessor(professor.getId(), course.getId());
//                }
//            }
//
//        } catch (SQLException e) {
//            logger.error("Error updating professor: {}", professor, e);
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
//    private void addCourseToProfessor(Long professorId, Long courseId) {
//        String query = "INSERT INTO professor_courses (professor_id, course_id) VALUES (?, ?)";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setLong(1, professorId);
//            stmt.setLong(2, courseId);
//            stmt.executeUpdate();
//            logger.info("Course {} added to Professor ID {}", courseId, professorId);
//        } catch (SQLException e) {
//            logger.error("Error adding course {} to Professor ID {}", courseId, professorId, e);
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
//    public void removeCourseFromProfessor(Long professorId, Long courseId) {
//        String query = "DELETE FROM professor_courses WHERE professor_id = ? AND course_id = ?";
//        Connection connection = null;
//        PreparedStatement stmt = null;
//
//        try {
//            connection = connectionPool.getConnection();
//            stmt = connection.prepareStatement(query);
//            stmt.setLong(1, professorId);
//            stmt.setLong(2, courseId);
//            stmt.executeUpdate();
//            logger.info("Course {} removed from Professor ID {}", courseId, professorId);
//        } catch (SQLException e) {
//            logger.error("Error removing course {} from Professor ID {}", courseId, professorId, e);
//        } finally {
//            try {
//                if (stmt != null) stmt.close();
//                if (connection != null) connectionPool.releaseConnection(connection);
//            } catch (SQLException e) {
//                logger.error("Error closing resources", e);
//            }
//        }
//    }
//}
