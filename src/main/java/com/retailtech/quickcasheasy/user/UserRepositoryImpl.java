package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.database.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserRepository using a database connection.
 */
public class UserRepositoryImpl implements UserRepository {

    /**
     * Saves a user to the repository.
     *
     * @param user the user to save
     */
@Override
public void saveUser(User user) {
    if (user == null) {
        throw new IllegalArgumentException("User cannot be null");
    }
    if (user.getUserName() == null) {
        throw new IllegalArgumentException("Username cannot be null");
    }

    String sql = "MERGE INTO users (id, username, password, role) VALUES (?, ?, ?, ?)";
    try (Connection connection = DatabaseConnectionManager.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setLong(1, user.getId() != null ? user.getId() : generateNewId());
        pstmt.setString(2, user.getUserName());
        pstmt.setString(3, user.getPassword());
        pstmt.setString(4, user.getRole().name());

        pstmt.executeUpdate();
        connection.commit();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error saving user: " + e.getMessage(), e);
    }
}

    private long generateNewId() {
        String createSequenceSql = "CREATE SEQUENCE IF NOT EXISTS user_sequence START WITH 1 INCREMENT BY 1";
        String getNextValSql = "SELECT NEXTVAL('user_sequence')";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement createSeqStmt = connection.prepareStatement(createSequenceSql);
             PreparedStatement getNextValStmt = connection.prepareStatement(getNextValSql)) {
            createSeqStmt.execute();
            ResultSet rs = getNextValStmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new RuntimeException("Failed to generate new ID for user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating new ID: " + e.getMessage(), e);
        }
    }

    /**
 * Retrieves a user by username.
 *
 * @param username the username of the user to retrieve
 * @return an Optional containing the user if found, or empty if not found
 */
@Override
public Optional<User> getUserByUsername(String username) {
    if (username == null) {
        return Optional.empty();
    }
    String sql = "SELECT * FROM users WHERE username = ?";
    try (Connection connection = DatabaseConnectionManager.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            User user = new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"), UserRole.valueOf(rs.getString("role")));
            return Optional.of(user);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error retrieving user by username: " + e.getMessage(), e);
    }
    return Optional.empty();
}

/**
 * Deletes a user by username.
 *
 * @param username the username of the user to delete
 */
@Override
public void deleteUserByUsername(String username) {
    if (username == null) {
        throw new IllegalArgumentException("Username cannot be null");
    }
    String sql = "DELETE FROM users WHERE username = ?";
    try (Connection connection = DatabaseConnectionManager.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, username);
        pstmt.executeUpdate();
        connection.commit();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
    }
}

/**
 * Retrieves all users.
 *
 * @return a list of all users
 */
@Override
public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM users";
    try (Connection connection = DatabaseConnectionManager.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            User user = new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"), UserRole.valueOf(rs.getString("role")));
            users.add(user);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error retrieving all users: " + e.getMessage(), e);
    }
    return users;
}

/**
 * Retrieves a user by ID.
 *
 * @param id the ID of the user to retrieve
 * @return an Optional containing the user if found, or empty if not found
 */
@Override
public Optional<User> getUserById(Long id) {
    if (id == null) {
        return Optional.empty();
    }
    String sql = "SELECT * FROM users WHERE id = ?";
    try (Connection connection = DatabaseConnectionManager.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            User user = new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"), UserRole.valueOf(rs.getString("role")));
            return Optional.of(user);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error retrieving user by ID: " + e.getMessage(), e);
    }
    return Optional.empty();
}
}