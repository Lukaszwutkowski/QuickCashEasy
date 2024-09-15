package com.retailtech.quickcasheasy.user;

import java.util.Optional;

class UserService {

    private final UserRepository userRepository;

    // Constructor injecting the repository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Register a new user.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param role     the role of the user
     */
    public void registerUser(String username, String password, UserRole role) {
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
    public boolean authenticateUser(String username, String password) {
        Optional<User> user = userRepository.getUserByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    /**
     * Retrieve a user by username.
     *
     * @param username the username of the user
     * @return the user if found, or null if not found
     */
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username).orElse(null);
    }

    /**
     * Delete a user by username.
     *
     * @param username the username of the user to delete
     */
    public void deleteUser(String username) {
        userRepository.deleteUserByUsername(username);
    }
}
