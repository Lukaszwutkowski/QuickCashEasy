package com.retailtech.quickcasheasy.user;

public class UserFacade {

    private final UserService userService;

    // Constructor
    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user in the system.
     *
     * @param username the username
     * @param password the password
     * @param role     the user's role (e.g., cashier, customer)
     */
    public void registerUser(String username, String password, UserRole role) {
        userService.registerUser(username, password, role);  // Delegate to userService
    }

    /**
     * Authenticate a user by their username and password.
     *
     * @param username the username
     * @param password the password
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        return userService.authenticateUser(username, password);  // Delegate to userService
    }

    /**
     * Get user details by username.
     *
     * @param username the username
     * @return the user details, or null if not found
     */
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);  // Delegate to userService
    }

    /**
     * Delete a user by username.
     *
     * @param username the username of the user to delete
     */
    public void deleteUser(String username) {
        userService.deleteUser(username);  // Delegate to userService
    }
}
