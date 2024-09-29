package com.retailtech.quickcasheasy.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    /**
     * Save a user to the repository.
     *
     * @param user the user to save
     */
    void saveUser(User user);

    /**
     * Retrieve a user by username.
     *
     * @param username the username of the user to retrieve
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> getUserByUsername(String username);

    /**
     * Delete a user by username.
     *
     * @param username the username of the user to delete
     */
    void deleteUserByUsername(String username);

    /**
     * Retrieve all users.
     *
     * @return a list of all users
     */
    List<User> getAllUsers();

    public Optional<User> getUserById(Long id);

}
