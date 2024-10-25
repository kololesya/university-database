package com.laba.solvd.dao.repoImpl;

import com.laba.solvd.dao.ScholarshipDao;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipRepo implements ScholarshipDao {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
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
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, scholarship.getStudentId());
            stmt.setDouble(2, scholarship.getScholarshipAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(scholarship.getAwardDate()));

            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()) {
                scholarship.setId(resultSet.getLong(1));
            }
            logger.info("Scholarship created: {}", scholarship);
        } catch (SQLException e) {
            logger.error("Error creating scholarship: {}", scholarship, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM scholarships WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.info("Scholarship with ID {} deleted", id);
        } catch (SQLException e) {
            logger.error("Error deleting scholarship with ID: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Scholarship findById(Long id) {
        String query = "SELECT * FROM scholarships WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                logger.info("Scholarship found with ID: {}", id);
                return mapScholarship(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding scholarship with ID: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return null;
    }

    @Override
    public List<Scholarship> findAll() {
        String query = "SELECT * FROM scholarships";
        List<Scholarship> scholarships = new ArrayList<>();
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                scholarships.add(mapScholarship(rs));
            }
            logger.info("Retrieved all scholarships");
        } catch (SQLException e) {
            logger.error("Error retrieving all scholarships", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return scholarships;
    }

    @Override
    public Scholarship findByStudentId(Long studentId) {
        String query = "SELECT * FROM scholarships WHERE student_id = ? LIMIT 1";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                logger.info("Retrieved scholarship for student ID: {}", studentId);
                return mapScholarship(rs);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving scholarship for student ID: {}", studentId, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return null;
    }

    @Override
    public Scholarship update(Scholarship scholarship) {
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            if (!isIdExists(scholarship.getId(), connection)) {
                logger.error("Scholarship with ID " + scholarship.getId() + " does not exist.");
                //throw new IllegalArgumentException("Scholarship with ID " + scholarship.getId() + " does not exist.");
                return null;
            }

            String updateQuery = "UPDATE scholarships SET scholarship_amount = ?, award_date = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                stmt.setDouble(1, scholarship.getScholarshipAmount());
                stmt.setTimestamp(2, Timestamp.valueOf(scholarship.getAwardDate()));
                stmt.setLong(3, scholarship.getId());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated == 0) {
                    logger.error("Failed to update Scholarship with ID " + scholarship.getId());
                } else {
                    logger.info("Scholarship with ID " + scholarship.getId() + " was updated successfully.");
                }
            }

        } catch (SQLException e) {
            logger.error("Error updating scholarship: " + scholarship, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return null;
    }


    private boolean isIdExists(Long id, Connection connection) {
        String query = "SELECT COUNT(*) FROM scholarships WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking ID existence for scholarship with ID: {}", id, e);
        }
        return false;
    }

}
