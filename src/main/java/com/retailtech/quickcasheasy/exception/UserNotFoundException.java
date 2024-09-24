package com.retailtech.quickcasheasy.exception;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified username.
     *
     * @param username the username of the user that was not found
     */
    public UserNotFoundException(String username) {
        super("User not found for username: " + username);
    }
}
