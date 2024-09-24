package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.user.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade for user-related operations.
 * Provides a simplified interface to interact with users.
 */
public class UserFacade {

    private final UserService userService;

    /**
     * Constructor initializing the UserService.
     */
    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param role     the role of the user
     * @return the created UserDTO
     */
    public UserDTO registerUser(String username, String password, UserRole role) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }
        userService.registerUser(username, password, role);
        User user = userService.getUserByUsername(username);
        return mapToDTO(user);
    }

    /**
     * Authenticates a user by username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        return userService.authenticateUser(username, password);
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username of the user
     * @return the UserDTO if found, or null if not found
     */
    public UserDTO getUserByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return (user != null) ? mapToDTO(user) : null;
    }

    /**
     * Deletes a user by username.
     *
     * @param username the username of the user to delete
     */
    public void deleteUser(String username) {
        userService.deleteUser(username);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of UserDTOs
     */
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the User entity
     * @return the corresponding UserDTO
     */
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getRole()
        );
    }
}
