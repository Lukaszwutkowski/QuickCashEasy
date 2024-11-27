package com.retailtech.quickcasheasy.database;

import java.sql.*;

/**
 * Manager for managing database connections.
 */
public class DatabaseConnectionManager {

    // SQLite database connection details
    private static final String JDBC_URL = "jdbc:sqlite:QCE.db";
    /**
     * Returns a new instance of the database connection.
     *
     * @return The connection to the SQLite database.
     */
    public static Connection getConnection() {
        try {
            // Always create a new connection
            Connection connection = DriverManager.getConnection(JDBC_URL);
            connection.setAutoCommit(false); // Setting manual commit to ensure data persistence

            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the SQLite database", e);
        }
    }

    /**
     * Closes the given database connection.
     *
     * @param connection The connection to close.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
