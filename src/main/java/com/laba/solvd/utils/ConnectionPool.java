package com.laba.solvd.utils;

import com.laba.solvd.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static final int POOL_SIZE = 5;
    private static ConnectionPool instance;
    private static final List<Connection> availableConnections = new ArrayList<>();
    private static final List<Connection> usedConnections = new ArrayList<>();

    public ConnectionPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            availableConnections.add(ConnectionManager.getConnection());
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public synchronized Connection getConnection() {
        if (availableConnections.isEmpty()) {
            throw new RuntimeException("No available connections");
        }
        Connection connection = availableConnections.remove(0);
        usedConnections.add(connection);
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        usedConnections.remove(connection);
        availableConnections.add(connection);
    }

    public void closeAllConnections() {
        for (Connection connection : usedConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (Connection connection : availableConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
