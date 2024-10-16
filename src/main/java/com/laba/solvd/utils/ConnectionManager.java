package com.laba.solvd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.DriverManager;

public class ConnectionManager {
    private static final String CONFIG_FILE = "db.properties"; // Файл должен находиться в resources
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                //return; // Если файл не найден, выходим
            }

            Properties properties = new Properties();
            properties.load(input);
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");

            // Выводим значения для проверки
            System.out.println("URL: " + url);
            System.out.println("Username: " + username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (url == null || username == null || password == null) {
            System.out.println("Database connection details are not properly set.");
            return null;
        }
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
