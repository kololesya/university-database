package com.laba.solvd.dao.impl;

import com.laba.solvd.dao.StudentDao;
import com.laba.solvd.model.Club;
import com.laba.solvd.model.Student;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepo implements StudentDao {
    private final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(StudentRepo.class.getName());

    private Student mapStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getLong("id"),
                rs.getLong("university_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getTimestamp("enrollment_date").toLocalDateTime()
        );
    }

    @Override
    public void create(Student student) {
        String checkQuery = "SELECT COUNT(*) FROM students WHERE email = ?";
        String insertQuery = "INSERT INTO students (first_name, last_name, email, enrollment_date, university_id) VALUES (?, ?, ?, ?, ?)";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            if (isEmailExists(student.getEmail(), connection)) {
                logger.error("Student with email {} already exists.", student.getEmail());
                return;
            }

            PreparedStatement stmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

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
            logger.info("Student created: {}", student);
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

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.info("Student with ID " + id + " was deleted");
        } catch (SQLException e) {
            logger.error("Error deleting student with id: " + id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Student findById(Long id) {
        String query = """
    SELECT s.id AS student_id, s.first_name, s.last_name, s.email, 
           c.id AS club_id, c.name AS club_name
    FROM students s
    
    """;

        Student student = null;
        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                student = mapStudent(rs);

                // Check for club information and map it if present
                List<Club> clubs = new ArrayList<>();
                do {
                    Long clubId = rs.getLong("club_id");
                    String clubName = rs.getString("club_name");
                    if (clubId != 0) { // Ensure club exists
                        Club club = new Club();
                        club.setId(clubId);
                        club.setName(clubName);
                        clubs.add(club);
                    }
                } while (rs.next());

                student.setClubs(clubs); // Assuming you have a setClubs method in Student
            }
        } catch (SQLException e) {
            logger.error("Error finding student with id: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return student;
    }

    @Override
    public List<Student> findAll() {
        String query = "SELECT * FROM students";
        List<Student> students = new ArrayList<>();
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                students.add(mapStudent(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all students", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return students;
    }

    @Override
    public void update(Student student) {
        String updateQuery = "UPDATE students SET first_name = ?, last_name = ?, email = ?, enrollment_date = ?, university_id = ? WHERE id = ?";
        PreparedStatement updateStmt = null;
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            updateStmt = connection.prepareStatement(updateQuery);

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
            if (updateStmt != null) {
                try { updateStmt.close(); } catch (SQLException e) { logger.error("Error closing update statement", e); }
            }
            if (connection != null) {
                CONNECTION_POOL.releaseConnection(connection);
            }
        }
    }

    @Override
    public Student findByEmail(String email) {
        String query = "SELECT * FROM students WHERE email = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapStudent(rs);
            }

        } catch (SQLException e) {
            logger.error("Error finding student with email: {}", email, e);
        } finally {
            if (connection != null) {
                CONNECTION_POOL.releaseConnection(connection);
            }
        }
        return null;
    }

    @Override
    public void assignScholarshipToStudent(Long studentId, Long scholarshipId) {
        String query = "UPDATE students SET scholarship_id = ? WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, scholarshipId);
            stmt.setLong(2, studentId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                logger.info("Scholarship with ID {} assigned to student with ID: {}", scholarshipId, studentId);
            } else {
                logger.error("No student found with ID: {}", studentId);
            }
        } catch (SQLException e) {
            logger.error("Error assigning scholarship with ID {} to student with ID: {}", scholarshipId, studentId, e);
        } finally {
                CONNECTION_POOL.releaseConnection(connection);
        }
    }

    public void getStudentWithClubInfo(Long studentId) {
        String query = "SELECT s.id AS student_id, s.first_name, s.last_name, s.email, " +
                "c.id AS club_id, c.name AS club_name " +
                "FROM students s " +
                "LEFT JOIN club_memberships cm ON s.id = cm.student_id " +
                "LEFT JOIN clubs c ON cm.club_id = c.id " +
                "WHERE s.id = ?";

        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, studentId);
            ResultSet rs = stmt.executeQuery();

            String firstName = null;
            String lastName = null;
            String email = null;
            List<String> clubNames = new ArrayList<>();

            while (rs.next()) {
                if (firstName == null) {
                    firstName = rs.getString("first_name");
                    lastName = rs.getString("last_name");
                    email = rs.getString("email");
                }

                String clubName = rs.getString("club_name");
                if (clubName != null) {
                    clubNames.add(clubName);
                }
            }

            if (firstName != null) {
                System.out.println("Student ID: " + studentId +
                        ", Name: " + firstName + " " + lastName +
                        ", Email: " + email +
                        ", Clubs: " + (clubNames.isEmpty() ? "No clubs assigned" : String.join(", ", clubNames)));
            } else {
                System.out.println("No student found with ID: " + studentId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getStudentWithScholarshipInfo(Long studentId) {
        String query = "SELECT s.id AS student_id, s.first_name, s.last_name, s.email, " +
                "sch.id AS scholarship_id, sch.scholarship_amount AS scholarship_amount " +
                "FROM students s " +
                "LEFT JOIN scholarships sch ON s.id = sch.student_id " +
                "WHERE s.id = ?";

        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, studentId);
            ResultSet rs = stmt.executeQuery();

            String firstName = null;
            String lastName = null;
            String email = null;
            List<String> scholarshipDetails = new ArrayList<>();

            while (rs.next()) {
                if (firstName == null) { // Store student info only once
                    firstName = rs.getString("first_name");
                    lastName = rs.getString("last_name");
                    email = rs.getString("email");
                }

                Long scholarshipId = rs.getLong("scholarship_id");
                if (scholarshipId != null) { // Use '!= null' to check for existing scholarships
                    double scholarshipAmount = rs.getDouble("scholarship_amount");
                    scholarshipDetails.add("Scholarship ID: " + scholarshipId + ", Amount: $" + scholarshipAmount);
                }
            }

            if (firstName != null) {
                System.out.println("Student ID: " + studentId +
                        ", Name: " + firstName + " " + lastName +
                        ", Email: " + email +
                        ", Scholarships: " + (scholarshipDetails.isEmpty() ? "No scholarships assigned" : String.join(" | ", scholarshipDetails)));
            } else {
                System.out.println("No student found with ID: " + studentId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
