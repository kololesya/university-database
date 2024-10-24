package com.laba.solvd.dao.impl;

import com.laba.solvd.dao.RoomDao;
import com.laba.solvd.model.Room;
import com.laba.solvd.utils.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepo implements RoomDao {
    private final static ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(RoomRepo.class.getName());

    private Room mapRoom(ResultSet rs) throws SQLException {
        return new Room(
                rs.getLong("room_id"),
                rs.getString("room_number"),
                rs.getLong("dormitory_id")
        );
    }

    @Override
    public void create(Room room) {
        String checkQuery = "SELECT COUNT(*) FROM rooms WHERE room_number = ?";
        String insertQuery = "INSERT INTO rooms (room_number, dormitory_id) VALUES (?, ?)";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, room.getRoomNumber());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                logger.warn("Room with number '{}' already exists. Room not created.", room.getRoomNumber());
                return;
            }

            PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, room.getRoomNumber());
            insertStmt.setLong(2, room.getDormitoryId()); // Добавляем dormitoryId в запрос

            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    room.setId(generatedKeys.getLong(1));
                }
            }
            logger.info("Room created: {}", room);

        } catch (SQLException e) {
            logger.error("Error creating room: {}", room, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void update(Room room) {
        String query = "UPDATE rooms SET room_number = ?, dormitory_id = ? WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, room.getRoomNumber());
            stmt.setLong(2, room.getDormitoryId()); // Добавляем dormitoryId в обновление
            stmt.setLong(3, room.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                logger.error("No room found with ID: {}", room.getId());
            } else {
                logger.info("Room updated: {}", room);
            }
        } catch (SQLException e) {
            logger.error("Error updating room: {}", room, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM rooms WHERE id = ?";
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Room with ID {} deleted", id);
            } else {
                logger.error("No room found with ID: {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error deleting room with id: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Room findById(Long id) {
        String query = "SELECT r.id as room_id, r.room_number as room_number, r.dormitory_id as dormitory_id FROM rooms r WHERE r.id = ?";
        Room room = null;
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                room = mapRoom(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding room with id: {}", id, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return room;
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.id AS room_id, r.room_number AS room_number, r.dormitory_id AS dormitory_id FROM rooms r";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
            logger.info("Retrieved all rooms");
        } catch (SQLException e) {
            logger.error("Error retrieving all rooms", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return rooms;
    }

    @Override
    public List<Room> findByDormitoryId(Long dormitoryId) {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.id AS room_id, r.room_number, r.dormitory_id " +
                "FROM rooms r WHERE r.dormitory_id = ?";
        Connection connection = CONNECTION_POOL.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, dormitoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
            logger.info("Retrieved rooms for dormitory ID: {}", dormitoryId);
        } catch (SQLException e) {
            logger.error("Error retrieving rooms for dormitory ID: {}", dormitoryId, e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return rooms;
    }
}
