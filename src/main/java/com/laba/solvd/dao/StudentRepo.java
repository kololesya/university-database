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

    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM students WHERE email = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking email: " + email, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Error closing ResultSet", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
        return false;
    }


    @Override
    public void create(Student student) {
        if (isEmailExists(student.getEmail())) {
            logger.error("Student with email " + student.getEmail() + " already exists.");
            return;
        }

        // Если email не существует, открываем соединение и выполняем вставку
        String query = "INSERT INTO students (first_name, last_name, email, enrollment_date, university_id) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setTimestamp(4, Timestamp.valueOf(student.getEnrollmentDate()));
            stmt.setLong(5, student.getUniversityId());

            stmt.executeUpdate();

            // Получаем сгенерированный ключ для нового студента
            try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    student.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating student: " + student, e);
        } finally {
            // Закрываем ресурсы
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }


    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM students WHERE id = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.info("Student with ID " + id + " was deleted");
        } catch (SQLException e) {
            logger.error("Error deleting student with id: " + id, e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    public Student findById(Long id) {
        String query = "SELECT * FROM students WHERE id = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapStudent(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding student with id: " + id, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Error closing ResultSet", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
        return null;
    }

    @Override
    public List<Student> findAll() {
        String query = "SELECT * FROM students";
        List<Student> students = new ArrayList<>();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            connection = connectionPool.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                students.add(mapStudent(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all students", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Error closing ResultSet", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing Statement", e);
                }
            }
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
        return students;
    }

    @Override
    public void update(Student student) {
        String query = "UPDATE students SET first_name = ?, last_name = ?, email = ?, enrollment_date = ?, university_id = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setTimestamp(4, Timestamp.valueOf(student.getEnrollmentDate()));
            stmt.setLong(5, student.getUniversityId());
            stmt.setLong(6, student.getId());

            stmt.executeUpdate();
            logger.info("Student with ID " + student.getId() + " was updated successfully");
        } catch (SQLException e) {
            logger.error("Error updating student: " + student, e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }
}
