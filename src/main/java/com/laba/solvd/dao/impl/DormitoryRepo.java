package com.laba.solvd.dao.impl;

import com.laba.solvd.dao.DormitoryDao;
import com.laba.solvd.model.Dormitory;
import com.laba.solvd.model.Room;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DormitoryRepo implements DormitoryDao {
    private final static ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(RoomRepo.class.getName());

    private Dormitory mapDormitory(ResultSet rs) throws SQLException{
        return new Dormitory(
                rs.getLong("dormitory_id"),
                rs.getString("name"),
                rs.getInt("capacity"),
                rs.getString("address"),
                new ArrayList<>()
        );
    }

    @Override
    public void create(Dormitory dormitory) {
        String checkQuery = "SELECT COUNT(*) FROM dormitories WHERE name = ?";
        String insertQuery = "INSERT INTO dormitories (name, capacity, address) VALUES (?, ?, ?)";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, dormitory.getName());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                logger.warn("Dormitory with name '{}' already exists. Dormitory not created.", dormitory.getName());
                return;
            }

            PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, dormitory.getName());
            insertStmt.setInt(2, dormitory.getCapacity());
            insertStmt.setString(3, dormitory.getAddress());

            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dormitory.setId(generatedKeys.getLong(1));
                }
            }
            logger.info("Dormitory created: {}", dormitory);

        } catch (SQLException e) {
            logger.error("Error creating dormitory: {}", dormitory, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void update(Dormitory dormitory) {
        String query = "UPDATE dormitories SET name = ?, capacity = ?, address = ? WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dormitory.getName());
            stmt.setInt(2, dormitory.getCapacity());
            stmt.setString(3, dormitory.getAddress());
            stmt.setLong(4, dormitory.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                logger.error("No dormitory found with ID: {}", dormitory.getId());
            } else {
                logger.info("Dormitory updated: {}", dormitory);
            }
        } catch (SQLException e) {
            logger.error("Error updating dormitory: {}", dormitory, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM dormitories WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Dormitory with ID {} deleted", id);
            } else {
                logger.error("No dormitory found with ID: {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error deleting dormitory with id: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Dormitory findById(Long id) {
        String query = "SELECT d.id as dormitory_id, d.name, d.capacity, d.address FROM dormitories d WHERE d.id = ?";
        Dormitory dormitory = null;
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dormitory = mapDormitory(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding dormitory with id: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return dormitory;
    }

    @Override
    public List<Dormitory> findAll() {
        List<Dormitory> dormitories = new ArrayList<>();
        String query = "SELECT d.id AS dormitory_id, d.name, d.capacity, d.address, "
                + "r.id AS room_id, r.room_number, r.dormitory_id "
                + "FROM dormitories d "
                + "LEFT JOIN rooms r ON d.id = r.dormitory_id";

        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            Long currentDormitoryId = null;
            Dormitory currentDormitory = null;

            while (rs.next()) {
                Long dormitoryId = rs.getLong("dormitory_id");

                if (!dormitoryId.equals(currentDormitoryId)) {
                    if (currentDormitory != null) {
                        dormitories.add(currentDormitory);
                    }

                    currentDormitory = new Dormitory(
                            dormitoryId,
                            rs.getString("name"),
                            rs.getInt("capacity"),
                            rs.getString("address"),
                            new ArrayList<>()
                    );

                    currentDormitoryId = dormitoryId;
                }

                Long roomId = rs.getLong("room_id");
                if (roomId != null) {
                    currentDormitory.getRooms().add(new Room(
                            roomId,
                            rs.getString("room_number"),
                            dormitoryId
                    ));
                }
            }

            if (currentDormitory != null) {
                dormitories.add(currentDormitory);
            }

            logger.info("Retrieved all dormitories with their rooms");
        } catch (SQLException e) {
            logger.error("Error retrieving all dormitories", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return dormitories;
    }
}
