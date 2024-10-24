package com.laba.solvd.dao.impl;

import com.laba.solvd.dao.ClubDao;
import com.laba.solvd.model.Club;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubRepo implements ClubDao {
    private final static ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ClubRepo.class.getName());

    private Club mapClub(ResultSet rs) throws SQLException {
        return new Club(
                rs.getLong("club_id"),
                rs.getString("club_name")
        );
    }
    @Override
    public Club findById(Long id) throws InterruptedException {
        String query = "Select c.id as club_id, c.name as club_name from Clubs c WHERE c.id = ?";
        Club club = null;
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                club = mapClub(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding club with id: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return club;
    }

    @Override
    public List<Club> findAll() {
        List<Club> clubs = new ArrayList<>();;
        String query = "SELECT c.id AS club_id, c.name AS club_name FROM Clubs c";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            Statement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                clubs.add(mapClub(rs));
            }
            logger.info("Retrieved all clubs");
        } catch (SQLException e) {
            logger.error("Error retrieving all clubs", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return clubs;
    }

    @Override
    public void create(Club club) {
        String checkQuery = "SELECT COUNT(*) FROM clubs WHERE name = ?";
        String insertQuery = "INSERT INTO clubs (name) VALUES (?)";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, club.getName());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                logger.warn("Club with name '{}' already exists. Club not created.", club.getName());
                return;
            }

            PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, club.getName());
            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    club.setId(generatedKeys.getLong(1));
                }
            }
            logger.info("Club created: {}", club);

        } catch (SQLException e) {
            logger.error("Error creating club: {}", club, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void update(Club club) {
        String query = "UPDATE clubs SET name = ? WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, club.getName());
            stmt.setLong(2, club.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                logger.error("No club found with ID: {}", club.getId());
            } else {
                logger.info("Club updated: {}", club);
            }
        } catch (SQLException e) {
            logger.error("Error updating club: {}", club, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM clubs WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Club with ID {} deleted", id);
            } else {
                logger.error("No club found with ID: {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error deleting club with id: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }
}
