package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.database.DatabaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {

        userRepository = new UserRepositoryImpl();
        DatabaseUtils databaseUtils = new DatabaseUtils();

        // Initialize the database schema
        databaseUtils.runScript("init.sql");
    }

    @AfterEach
    void tearDown() {
        DatabaseUtils databaseUtils = new DatabaseUtils();

        // Drop tables after each test
        databaseUtils.executeUpdate("DROP TABLE IF EXISTS users");
        databaseUtils.executeUpdate("DROP TABLE IF EXISTS user_roles");
    }


    @Test
    @DisplayName("Should save and retrieve a user")
    void shouldSaveAndRetrieveUser() {
        User user = new User(1L, "john_doe", "password", UserRole.CASHIER);
        userRepository.saveUser(user);

        Optional<User> retrievedUser = userRepository.getUserByUsername("john_doe");
        assertTrue(retrievedUser.isPresent(), "User should be present");
        assertEquals("john_doe", retrievedUser.get().getUserName(), "Username should match");
    }

    @Test
    @DisplayName("Should return empty when retrieving non-existent user")
    void shouldReturnEmptyForNonExistentUser() {
        Optional<User> retrievedUser = userRepository.getUserByUsername("non_existent");
        assertFalse(retrievedUser.isPresent(), "User should not be present");
    }

    @Test
    @DisplayName("Should delete an existing user")
    void shouldDeleteExistingUser() {
        User user = new User(1L, "jane_doe", "password", UserRole.ADMIN);
        userRepository.saveUser(user);
        userRepository.deleteUserByUsername("jane_doe");

        Optional<User> retrievedUser = userRepository.getUserByUsername("jane_doe");
        assertFalse(retrievedUser.isPresent(), "User should have been deleted");
    }

    @Test
    @DisplayName("Should retrieve all users")
    void shouldRetrieveAllUsers() {
        // Arrange: Insert two users
        User user1 = new User(1L, "user1", "pass1", UserRole.CASHIER);
        User user2 = new User(2L, "user2", "pass2", UserRole.ADMIN);
        userRepository.saveUser(user1);
        userRepository.saveUser(user2);

        // Act: Retrieve all users
        List<User> allUsers = userRepository.getAllUsers();

        // Assert: Check the retrieved users
        assertEquals(2, allUsers.size(), "Should retrieve two users");
        assertTrue(allUsers.stream().anyMatch(u -> u.getUserName().equals("user1")), "Should contain user1");
        assertTrue(allUsers.stream().anyMatch(u -> u.getUserName().equals("user2")), "Should contain user2");
    }


    @Test
    @DisplayName("Should throw exception when saving null user")
    void shouldThrowExceptionWhenSavingNullUser() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.saveUser(null);
        });
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when saving user with null username")
    void shouldThrowExceptionWhenSavingUserWithNullUsername() {
        User user = new User(1L, null, "password", UserRole.CASHIER);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.saveUser(user);
        });
        assertEquals("Username cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when deleting user with null username")
    void shouldThrowExceptionWhenDeletingUserWithNullUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.deleteUserByUsername(null);
        });
        assertEquals("Username cannot be null", exception.getMessage());
    }
}
