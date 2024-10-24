package com.laba.solvd.dao.repoImpl;

import com.laba.solvd.dao.StudentDao;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.model.Student;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepo implements StudentDao {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(StudentRepo.class.getName());

    private Student mapStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getTimestamp("enrollment_date").toLocalDateTime(),
                rs.getLong("university_id")
        );
    }

    @Override
    public void create(Student student) {
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            if (isEmailExists(student.getEmail(), connection)) {
                logger.error("Student with email {} already exists.", student.getEmail());
                return;
            }
            String query = "INSERT INTO students (first_name, last_name, email, enrollment_date, university_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setTimestamp(4, Timestamp.valueOf(student.getEnrollmentDate()));
            stmt.setLong(5, student.getUniversityId());

            stmt.executeUpdate();

            try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    student.setId(resultSet.getLong(1));
                }
            }

        } catch (SQLException e) {
            logger.error("Error creating student: {}", student, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    private boolean isEmailExists(String email, Connection connection) {
        String query = "SELECT COUNT(*) FROM students WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking email existence for: {}", email, e);
        }
        return false;
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM students WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.info("Student with ID " + id + " was deleted");
        } catch (SQLException e) {
            logger.error("Error deleting student with id: " + id, e);
        } finally {CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Student findById(Long id) {
        String query = """
        SELECT s.id, s.first_name, s.last_name, s.email, s.enrollment_date, s.university_id, 
               sc.id AS scholarshipId, sc.scholarship_amount, sc.award_date
        FROM Students s
        LEFT JOIN Scholarships sc ON s.id = sc.student_id
        WHERE s.id = ?""";

        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id); // Устанавливаем ID студента
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Student student = mapStudent(rs);

                if (rs.getInt("scholarshipId") != 0) {
                    Scholarship scholarship = new Scholarship();
                    scholarship.setId(rs.getLong("scholarshipId"));
                    scholarship.setScholarshipAmount(rs.getDouble("scholarship_amount"));
                    //scholarship.setStudentId(rs.getLong("student_id"));
                    scholarship.setAwardDate(rs.getTimestamp("award_date").toLocalDateTime()); // или rs.getDate("award_date").toLocalDate(), если нужно только дату
                    student.setScholarship(scholarship);
                }

                return student;
            }

        } catch (SQLException e) {
            logger.error("Error finding student with id: " + id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return null;
    }
    @Override
    public List<Student> findAll() {
        String query = """
            SELECT s.id, s.first_name, s.last_name, s.email, s.enrollment_date, s.university_id,
                   sc.id AS scholarshipId, sc.student_id, sc.scholarship_amount, sc.award_date
            FROM Students s
            LEFT JOIN Scholarships sc ON s.id = sc.student_id""";

        List<Student> students = new ArrayList<>();
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Student student = mapStudent(rs);

                if (rs.getInt("scholarshipId") != 0) {
                    Scholarship scholarship = new Scholarship();
                    scholarship.setId(rs.getLong("scholarshipId"));
                    scholarship.setScholarshipAmount(rs.getDouble("scholarship_amount"));
                    scholarship.setStudentId(rs.getLong("student_id"));
                    scholarship.setAwardDate(rs.getTimestamp("award_date").toLocalDateTime()); // или rs.getDate("award_date").toLocalDate(), если нужно только дату
                    student.setScholarship(scholarship);
                }

                students.add(student);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all students", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return students;
    }

    @Override
    public Student update(Student student) {
        String updateQuery = "UPDATE students SET first_name = ?, last_name = ?, email = ?, enrollment_date = ?, university_id = ? WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            if (!isIdExists(student.getId(), connection)) {
                logger.error("Student with ID " + student.getId() + " does not exist, update failed.");
                return null;
            }
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);

            updateStmt.setString(1, student.getFirstName());
            updateStmt.setString(2, student.getLastName());
            updateStmt.setString(3, student.getEmail());
            updateStmt.setTimestamp(4, Timestamp.valueOf(student.getEnrollmentDate()));
            updateStmt.setLong(5, student.getUniversityId());
            updateStmt.setLong(6, student.getId());

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated == 0) {
                logger.error("No student with ID " + student.getId() + " found to update.");
            } else {
                logger.info("Student with ID " + student.getId() + " was updated successfully.");
            }

        } catch (SQLException e) {
            logger.error("Error updating student: " + student, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return null;
    }

    private boolean isIdExists(Long id, Connection connection) {
        String query = "SELECT COUNT(*) FROM students WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking id existence for: {}", id, e);
        }
        return false;
    }

}
