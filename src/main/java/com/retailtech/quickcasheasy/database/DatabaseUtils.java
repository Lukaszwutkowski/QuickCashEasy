package com.retailtech.quickcasheasy.database;

import org.h2.tools.RunScript;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * Utility class for performing database operations.
 */
public class DatabaseUtils {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("SQLite JDBC Driver not found.", e);
        }
    }

    /**
     * Executes an insert statement and returns the generated key.
     *
     * @param sql    The SQL insert statement.
     * @param params Parameters for the SQL statement.
     * @return The generated key.
     */
    public Long executeInsert(String sql, Object... params) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // Execute the insert
            pstmt.executeUpdate();

            // Commit the transaction
            conn.commit();

            // Retrieve the generated key
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Inserting record failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing insert: " + e.getMessage(), e);
        }
    }

    /**
     * Executes an update statement.
     *
     * @param sql    The SQL update statement.
     * @param params Parameters for the SQL statement.
     */
    public void executeUpdate(String sql, Object... params) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // Execute the update
            pstmt.executeUpdate();

            // Commit the transaction
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing update: " + e.getMessage(), e);
        }
    }

    /**
     * Executes a query and processes the result set using the provided handler.
     *
     * @param sql     The SQL query statement.
     * @param handler A handler to process the ResultSet.
     * @param params  Parameters for the SQL statement.
     * @param <T>     The type of the result returned by the handler.
     * @return The result processed by the handler.
     */
    public <T> T executeQuery(String sql, ResultSetHandler<T> handler, Object... params) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
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
            e.printStackTrace();
            throw new RuntimeException("Error executing query: " + e.getMessage(), e);
        }
    }

    /**
     * Executes a SQL script from the classpath.
     *
     * @param scriptPath The path to the script file.
     */
    public void runScript(String scriptPath) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             InputStream inputStream = getClass().getClassLoader().getResourceAsStream(scriptPath)) {

            if (inputStream == null) {
                throw new RuntimeException("Script file not found: " + scriptPath);
            }

            System.out.println("Executing script: " + scriptPath);
            RunScript.execute(conn, new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            conn.commit();

            System.out.println("Script executed successfully: " + scriptPath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error running script: " + e.getMessage(), e);
        }
    }


    /**
     * Interface for handling ResultSet processing.
     *
     * @param <T> The type of the result.
     */
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }
}
