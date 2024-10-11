package com.laba.solvd.dao;

import com.laba.solvd.model.Student;
import com.laba.solvd.utils.ConnectionPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepo implements GenericDao<Student> {
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(StudentRepo.class);

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
        String query = "INSERT INTO students (first_name, last_name, email, enrollment_date, university_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setTimestamp(4, Timestamp.valueOf(student.getEnrollmentDate()));
            stmt.setLong(5, student.getUniversityId());

            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()) {
                student.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM students WHERE id = ?";
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Student findById(Long id) {
        String query = "SELECT * FROM students WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapStudent(rs);
            } else {
                logger.warn("Student with id {} not found", id);
            }
        } catch (SQLException e) {
            logger.error("Error finding student by id: {}", id, e);
        }
        return null;
    }


    @Override
    public List<Student> findAll() {
        String query = "SELECT * FROM students";
        List<Student> students = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return students;
    }

    @Override
    public void update(Student student) {
        String query = "UPDATE students SET first_name = ?, last_name = ?, email = ?, enrollment_date = ?, university_id = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, student.getFirstName());
                stmt.setString(2, student.getLastName());
                stmt.setString(3, student.getEmail());
                stmt.setTimestamp(4, Timestamp.valueOf(student.getEnrollmentDate()));
                stmt.setLong(5, student.getUniversityId());
                stmt.setLong(6, student.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
