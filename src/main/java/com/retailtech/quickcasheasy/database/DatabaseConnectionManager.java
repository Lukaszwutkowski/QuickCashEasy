package com.retailtech.quickcasheasy.database;

import java.sql.*;

/**
 * Manager for managing database connections.
 */
public class DatabaseConnectionManager {

    // Database connection details for H2 server mode
    private static final String JDBC_URL = "jdbc:h2:file:./src/main/resources/QCE";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    /**
     * Returns a new instance of the database connection.
     *
     * @return The connection to the database.
     */
    public static Connection getConnection() {
        try {
            // Always create a new connection
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            connection.setAutoCommit(false); // Setting manual commit to ensure data persistence

            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database", e);
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
