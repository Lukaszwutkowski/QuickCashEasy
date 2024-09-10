package com.retailtech.quickcasheasy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnectionManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    // Method to get a connection
    public Connection getConnection() throws SQLException, SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
