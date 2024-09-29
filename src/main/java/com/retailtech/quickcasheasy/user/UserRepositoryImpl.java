package com.retailtech.quickcasheasy.user;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of UserRepository using in-memory storage.
 */
public class UserRepositoryImpl implements UserRepository {

    // In-memory storage for users (keyed by ID)
    private final Map<Long, User> userStorageById = new HashMap<>();
    // Secondary map to allow lookup by username
    private final Map<String, User> userStorageByUsername = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

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
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement()); // Assign a unique ID
        }
        userStorageById.put(user.getId(), user);  // Save user by ID
        userStorageByUsername.put(user.getUserName(), user);  // Save user by username
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
        return Optional.ofNullable(userStorageByUsername.get(username));  // Retrieve user by username
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
        User user = userStorageByUsername.remove(username);  // Remove user by username
        if (user != null) {
            userStorageById.remove(user.getId());  // Also remove from the ID map
        }
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorageById.values());  // Return a new list containing all users
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user to retrieve
     * @return an Optional containing the user if found, or empty if not found
     */
    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userStorageById.get(id));
    }

}
