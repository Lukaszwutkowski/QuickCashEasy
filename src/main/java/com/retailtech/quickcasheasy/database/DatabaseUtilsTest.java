package com.retailtech.quickcasheasy.database;

import java.sql.SQLException;

public class DatabaseUtilsTest {
    public static void main(String[] args) {
        DatabaseUtils dbUtils = new DatabaseUtils();
        dbUtils.executeUpdate("CREATE TABLE IF NOT EXISTS test (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255))");
        dbUtils.executeUpdate("INSERT INTO test (name) VALUES (?)", "TestName");
        String name = dbUtils.executeQuery("SELECT name FROM test WHERE id = ?", rs -> {
            try {
                if (rs.next()) {
                    return rs.getString("name");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }, 1L);
        System.out.println("Retrieved Name: " + name);
    }
}
