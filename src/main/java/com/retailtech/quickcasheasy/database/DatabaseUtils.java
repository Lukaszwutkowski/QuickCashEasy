package com.retailtech.quickcasheasy.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import org.h2.tools.RunScript;

public class DatabaseUtils {

    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;TRACE_LEVEL_FILE=4";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("H2 JDBC Driver not found.", e);
        }
    }

    // Method to execute an insert statement and return the generated key
    public Long executeInsert(String sql, Object... params) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // Execute the insert
            pstmt.executeUpdate();

            // Retrieve the generated key
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Inserting payment failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            // Handle exceptions (you can customize this)
            e.printStackTrace();
            throw new RuntimeException("Error executing insert: " + e.getMessage(), e);
        }
    }

    // Existing executeUpdate method
    public void executeUpdate(String sql, Object... params) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // Execute the update
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            throw new RuntimeException("Error executing update: " + e.getMessage(), e);
        }
    }

    // Existing executeQuery method
    public <T> T executeQuery(String sql, ResultSetHandler<T> handler, Object... params) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                return handler.handle(rs);
            }

        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            throw new RuntimeException("Error executing query: " + e.getMessage(), e);
        }
    }

    // Interface for handling ResultSet
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }

    // New method to execute SQL scripts from the classpath
    public void runScript(String scriptPath) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             InputStream inputStream = getClass().getClassLoader().getResourceAsStream(scriptPath)) {

            if (inputStream == null) {
                throw new RuntimeException("Script file not found: " + scriptPath);
            }

            RunScript.execute(conn, new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error running script: " + e.getMessage(), e);
        }
    }
}
