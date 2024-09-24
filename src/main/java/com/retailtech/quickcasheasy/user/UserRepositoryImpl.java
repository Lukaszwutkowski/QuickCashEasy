package com.retailtech.quickcasheasy.user;

import java.util.*;

/**
 * Implementation of UserRepository using in-memory storage.
 */
public class UserRepositoryImpl implements UserRepository {

    // In-memory storage for users
    private final Map<String, User> userStorage = new HashMap<>();

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
            throw new IllegalArgumentException("User name cannot be null");
        }
        userStorage.put(user.getUserName(), user);  // Save user by username
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
        return Optional.ofNullable(userStorage.get(username));  // Retrieve user by username
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
        userStorage.remove(username);  // Remove user by username
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());  // Return a new list containing all users
    }
}
