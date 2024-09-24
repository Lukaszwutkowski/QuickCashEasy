package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.exception.UserNotFoundException;

import java.util.List;

/**
 * Internal service handling user business logic.
 * This class jest public, aby być dostępny dla UserFacade.
 */
class UserService {

    private final UserRepository userRepository;

    // Constructor injecting the repository
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Register a new user.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param role     the role of the user
     */
    void registerUser(String username, String password, UserRole role) {
        if (userRepository.getUserByUsername(username).isPresent()) {
            throw new RuntimeException("User with this username already exists!");
        }
        User newUser = new User(null, username, password, role);
        userRepository.saveUser(newUser);
    }

    /**
     * Authenticate a user by username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return true if authentication is successful, false otherwise
     */
    boolean authenticateUser(String username, String password) {
        return userRepository.getUserByUsername(username)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    /**
     * Retrieve a user by username.
     *
     * @param username the username of the user
     * @return the user if found, or null if not found
     */
    User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username).orElse(null);
    }

    /**
     * Delete a user by username.
     *
     * @param username the username of the user to delete
     */
    void deleteUser(String username) {
        if (!userRepository.getUserByUsername(username).isPresent()) {
            throw new UserNotFoundException(username);
        }
        userRepository.deleteUserByUsername(username);
    }

    /**
     * Retrieve all users.
     *
     * @return a list of all Users
     */
    List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
