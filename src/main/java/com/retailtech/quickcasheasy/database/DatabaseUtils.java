package com.retailtech.quickcasheasy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class DatabaseUtils {
    private final DatabaseConnectionManager connectionManager;

    public DatabaseUtils(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    // Utility method for executing queries (SELECT)
    public <T> T executeQuery(String sql, Function<ResultSet, T> mapper, Object... params) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement pstmt = prepareStatement(connection, sql, params);
             ResultSet rs = pstmt.executeQuery()) {
            return mapper.apply(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }
    }

    // Utility method for executing updates (INSERT, UPDATE, DELETE)
    public int executeUpdate(String sql, Object... params) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement pstmt = prepareStatement(connection, sql, params)) {
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing update", e);
        }
    }

    // Helper method to prepare a statement with parameters
    private PreparedStatement prepareStatement(Connection connection, String sql, Object[] params) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt;
    }
}
