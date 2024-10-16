package com.laba.solvd.dao;

import com.laba.solvd.model.Scholarship;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipRepo implements GenericDao<Scholarship> {
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ScholarshipRepo.class.getName());

    private Scholarship mapScholarship(ResultSet rs) throws SQLException {
        return new Scholarship(
                rs.getLong("id"),
                rs.getLong("student_id"),
                rs.getDouble("scholarship_amount"),
                rs.getTimestamp("award_date").toLocalDateTime()
        );
    }

    @Override
    public void create(Scholarship scholarship) {
        String query = "INSERT INTO scholarships (student_id, scholarship_amount, award_date) VALUES (?, ?, ?)";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, scholarship.getStudentId());
            stmt.setDouble(2, scholarship.getScholarshipAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(scholarship.getAwardDate()));

            stmt.executeUpdate();

            resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()) {
                scholarship.setId(resultSet.getLong(1));
            }
            logger.info("Scholarship created: {}", scholarship);
        } catch (SQLException e) {
            logger.error("Error creating scholarship: {}", scholarship, e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
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
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM scholarships WHERE id = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.info("Scholarship with ID {} deleted", id);
        } catch (SQLException e) {
            logger.error("Error deleting scholarship with ID: {}", id, e);
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

    @Override
    public Scholarship findById(Long id) {
        String query = "SELECT * FROM scholarships WHERE id = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                logger.info("Scholarship found with ID: {}", id);
                return mapScholarship(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding scholarship with ID: {}", id, e);
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
    public List<Scholarship> findAll() {
        String query = "SELECT * FROM scholarships";
        List<Scholarship> scholarships = new ArrayList<>();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            connection = connectionPool.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                scholarships.add(mapScholarship(rs));
            }
            logger.info("Retrieved all scholarships");
        } catch (SQLException e) {
            logger.error("Error retrieving all scholarships", e);
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
        return scholarships;
    }

    public Scholarship findByStudentId(Long studentId) {
        String query = "SELECT * FROM scholarships WHERE student_id = ? LIMIT 1";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, studentId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                logger.info("Retrieved scholarship for student ID: {}", studentId);
                return mapScholarship(rs);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving scholarship for student ID: {}", studentId, e);
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
    public void update(Scholarship scholarship) {
        String query = "UPDATE scholarships SET student_id = ?, scholarship_amount = ?, award_date = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, scholarship.getStudentId());
            stmt.setDouble(2, scholarship.getScholarshipAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(scholarship.getAwardDate()));
            stmt.setLong(4, scholarship.getId());
            stmt.executeUpdate();
            logger.info("Scholarship updated: {}", scholarship);
        } catch (SQLException e) {
            logger.error("Error updating scholarship: {}", scholarship, e);
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
