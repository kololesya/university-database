package com.laba.solvd.dao.impl;

import com.laba.solvd.dao.ScholarshipDao;
import com.laba.solvd.model.Scholarship;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipRepo implements ScholarshipDao {
    private final static ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ClubRepo.class.getName());

    private static Scholarship mapScholarship(ResultSet rs) throws SQLException {
        return new Scholarship(
                rs.getLong("id"),
                rs.getDouble("scholarship_amount"),
                rs.getTimestamp("award_date")
        );
    }

    @Override
    public void create(Scholarship scholarship) {
        String insertQuery = "INSERT INTO scholarships (scholarship_amount, award_date) VALUES (?, ?)";

        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setDouble(1, scholarship.getScholarshipAmount());
            insertStmt.setTimestamp(2, Timestamp.valueOf(scholarship.getAwardDate().toLocalDateTime()));

            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    scholarship.setId(generatedKeys.getLong(1));
                }
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
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Scholarship with ID {} deleted", id);
            } else {
                logger.error("No Scholarship found with ID: {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error deleting Scholarship with id: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Scholarship findById(Long id) {
        String query = "SELECT * FROM scholarships WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        Scholarship scholarship = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                scholarship = mapScholarship(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding scholarship with ID: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return scholarship;
    }

    @Override
    public List<Scholarship> findAll() {
        List<Scholarship> scholarships = new ArrayList<>();
        String query = "SELECT s.id AS id, s.scholarship_amount AS scholarship_amount, s.award_date AS award_date FROM scholarships s";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            Statement stmt = connection.prepareStatement(query);
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
    public void update(Scholarship scholarship) {
        String query = "UPDATE scholarships SET scholarship_amount = ?, award_date = ? WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setDouble(1, scholarship.getScholarshipAmount());
            stmt.setTimestamp(2, Timestamp.valueOf(scholarship.getAwardDate().toLocalDateTime()));
            stmt.setLong(3, scholarship.getId());
            stmt.executeUpdate();
            logger.info("Scholarship updated: {}", scholarship);
        } catch (SQLException e) {
            logger.error("Error updating scholarship: {}", scholarship, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }
}
