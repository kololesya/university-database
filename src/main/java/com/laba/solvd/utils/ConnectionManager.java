package com.laba.solvd.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final String CONFIG_FILE = "db.properties";
    private static String url;
    private static String username;
    private static String password;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    static {
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.error("Unable to find " + CONFIG_FILE);
                throw new RuntimeException("Database configuration file not found.");
            }

            Properties properties = new Properties();
            properties.load(input);
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");

            logger.info("Database URL: {}", url);
            logger.info("Database Username: {}", username);
        } catch (IOException e) {
            logger.error("Error loading database properties", e);
            throw new RuntimeException("Failed to load database properties.", e);
        }
    }

    public static Connection getConnection() {
        if (url == null || username == null || password == null) {
            logger.error("Database connection details are not properly set.");
            throw new RuntimeException("Database connection details are not properly set.");
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            logger.info("Connection established successfully.");
            return connection;
        } catch (SQLException e) {
            logger.error("Error establishing database connection", e);
            throw new RuntimeException("Failed to establish database connection.", e);
        }
    }
}
