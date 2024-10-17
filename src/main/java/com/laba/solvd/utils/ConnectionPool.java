package com.laba.solvd.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionPool {
    private static final int POOL_SIZE = 10;
    private static ConnectionPool instance;
    private final ConcurrentLinkedQueue<Connection> availableConnections = new ConcurrentLinkedQueue<>();
    private final List<Connection> usedConnections = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private ConnectionPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            availableConnections.add(createNewConnection());
        }
        logger.info("Initialized connection pool with {} connections", POOL_SIZE);
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private Connection createNewConnection() {
        Connection connection = ConnectionManager.getConnection();
        logger.info("New database connection created");
        return connection;
    }

    public Connection getConnection() {
        Connection connection = availableConnections.poll();
        if (connection == null) {
            if (usedConnections.size() < POOL_SIZE) {
                connection = createNewConnection();
            } else {
                logger.warn("No available connections in the pool. Used: {}, Available: {}", usedConnections.size(), availableConnections.size());
                // Optionally wait for a connection to become available
                return null; // or throw an exception
            }
        }

        usedConnections.add(connection);
        logger.info("Connection acquired. Available: {}, Used: {}", availableConnections.size(), usedConnections.size());
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            usedConnections.remove(connection);
            availableConnections.offer(connection);
            logger.info("Connection returned to the pool. Available: {}, Used: {}", availableConnections.size(), usedConnections.size());
        }
    }

    public void closeAllConnections() {
        logger.info("Closing all connections in the pool...");
        for (Connection connection : usedConnections) {
            closeConnection(connection);
        }
        usedConnections.clear();

        for (Connection connection : availableConnections) {
            closeConnection(connection);
        }
        availableConnections.clear();
        logger.info("All connections closed");
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Connection closed successfully");
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        } else {
            logger.warn("Attempted to close a null connection");
        }
    }

    public String getPoolState() {
        return String.format("Available: %d, Used: %d", availableConnections.size(), usedConnections.size());
    }

    public void shutdown() {
        closeAllConnections();
    }

    // Новый метод для получения количества активных соединений
    public int getActiveConnectionsCount() {
        return usedConnections.size();
    }
}
