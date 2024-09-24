package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.exception.UserNotFoundException;
import com.retailtech.quickcasheasy.user.dto.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for UserFacade.
 * Focuses on testing the facade's public methods using DTOs.
 */
@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserFacade userFacade;

    /**
     * Tests registering a new user and verifying its properties.
     */
    @Test
    @DisplayName("Should register and return a new user")
    void it_should_register_and_return_user() {
        // Given
        String username = "testuser";
        String password = "password123";
        UserRole role = UserRole.CASHIER;

        // Mock the behavior: registerUser does not return anything, but getUserByUsername returns the created user
        doNothing().when(userService).registerUser(username, password, role);
        User registeredUser = new User(1L, username, password, role);
        when(userService.getUserByUsername(username)).thenReturn(registeredUser);

        // When
        UserDTO userDTO = userFacade.registerUser(username, password, role);

        // Then
        assertNotNull(userDTO, "UserDTO should not be null");
        assertEquals(1L, userDTO.getId(), "User ID should match");
        assertEquals(username, userDTO.getUserName(), "Username should match");
        assertEquals(password, userDTO.getPassword(), "Password should match");
        assertEquals(role, userDTO.getRole(), "User role should match");

        verify(userService, times(1)).registerUser(username, password, role);
        verify(userService, times(1)).getUserByUsername(username);
    }

    /**
     * Tests that registering a user with an existing username throws an exception.
     */
    @Test
    @DisplayName("Should throw exception when registering user with existing username")
    void it_should_throw_exception_when_registering_existing_user() {
        // Given
        String username = "existinguser";
        String password = "password123";
        UserRole role = UserRole.CASHIER;

        // Mock the behavior: registerUser throws RuntimeException when user exists
        doThrow(new RuntimeException("User with this username already exists!"))
                .when(userService).registerUser(username, password, role);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userFacade.registerUser(username, password, role);
        });

        assertEquals("User with this username already exists!", exception.getMessage());

        verify(userService, times(1)).registerUser(username, password, role);
        verify(userService, never()).getUserByUsername(anyString());
    }

    /**
     * Tests that registering a user with null username throws IllegalArgumentException.
     */
    @Test
    @DisplayName("Should throw IllegalArgumentException when registering user with null username")
    void it_should_throw_illegal_argument_exception_when_registering_user_with_null_username() {
        // Given
        String username = null;
        String password = "password123";
        UserRole role = UserRole.CASHIER;

        // Assuming UserFacade checks for nulls before calling UserService
        // Thus, UserService.registerUser should never be called
        // So, no need to mock UserService

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userFacade.registerUser(username, password, role);
        });

        assertEquals("Username cannot be null", exception.getMessage());

        verify(userService, never()).registerUser(anyString(), anyString(), any());
        verify(userService, never()).getUserByUsername(anyString());
    }

    /**
     * Tests that registering a user with null password throws IllegalArgumentException.
     */
    @Test
    @DisplayName("Should throw IllegalArgumentException when registering user with null password")
    void it_should_throw_illegal_argument_exception_when_registering_user_with_null_password() {
        // Given
        String username = "testuser";
        String password = null;
        UserRole role = UserRole.CASHIER;

        // Assuming UserFacade checks for nulls before calling UserService
        // Thus, UserService.registerUser should never be called
        // So, no need to mock UserService

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userFacade.registerUser(username, password, role);
        });

        assertEquals("Password cannot be null", exception.getMessage());

        verify(userService, never()).registerUser(anyString(), anyString(), any());
        verify(userService, never()).getUserByUsername(anyString());
    }

    /**
     * Tests that registering a user with null role throws IllegalArgumentException.
     */
    @Test
    @DisplayName("Should throw IllegalArgumentException when registering user with null role")
    void it_should_throw_illegal_argument_exception_when_registering_user_with_null_role() {
        // Given
        String username = "testuser";
        String password = "password123";
        UserRole role = null;

        // Assuming UserFacade checks for nulls before calling UserService
        // Thus, UserService.registerUser should never be called
        // So, no need to mock UserService

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userFacade.registerUser(username, password, role);
        });

        assertEquals("User role cannot be null", exception.getMessage());

        verify(userService, never()).registerUser(anyString(), anyString(), any());
        verify(userService, never()).getUserByUsername(anyString());
    }

    /**
     * Tests authenticating a user successfully.
     */
    @Test
    @DisplayName("Should authenticate user successfully")
    void it_should_authenticate_user_successfully() {
        // Given
        String username = "testuser";
        String password = "password123";

        when(userService.authenticateUser(username, password)).thenReturn(true);

        // When
        boolean isAuthenticated = userFacade.authenticateUser(username, password);

        // Then
        assertTrue(isAuthenticated, "User should be authenticated successfully");

        verify(userService, times(1)).authenticateUser(username, password);
    }

    /**
     * Tests authenticating a user with incorrect password.
     */
    @Test
    @DisplayName("Should fail authentication with incorrect password")
    void it_should_fail_authentication_with_incorrect_password() {
        // Given
        String username = "testuser";
        String password = "wrongpassword";

        when(userService.authenticateUser(username, password)).thenReturn(false);

        // When
        boolean isAuthenticated = userFacade.authenticateUser(username, password);

        // Then
        assertFalse(isAuthenticated, "User should fail authentication with incorrect password");

        verify(userService, times(1)).authenticateUser(username, password);
    }

    /**
     * Tests retrieving a user by username.
     */
    @Test
    @DisplayName("Should retrieve user by username")
    void it_should_retrieve_user_by_username() {
        // Given
        String username = "testuser";
        User existingUser = new User(1L, username, "password123", UserRole.CASHIER);
        when(userService.getUserByUsername(username)).thenReturn(existingUser);

        // When
        UserDTO userDTO = userFacade.getUserByUsername(username);

        // Then
        assertNotNull(userDTO, "UserDTO should not be null");
        assertEquals(1L, userDTO.getId(), "User ID should match");
        assertEquals(username, userDTO.getUserName(), "Username should match");
        assertEquals("password123", userDTO.getPassword(), "Password should match");
        assertEquals(UserRole.CASHIER, userDTO.getRole(), "User role should match");

        verify(userService, times(1)).getUserByUsername(username);
    }

    /**
     * Tests retrieving a non-existent user by username.
     */
    @Test
    @DisplayName("Should return null when retrieving non-existent user")
    void it_should_return_null_when_user_not_found() {
        // Given
        String username = "nonexistentuser";
        when(userService.getUserByUsername(username)).thenReturn(null);

        // When
        UserDTO userDTO = userFacade.getUserByUsername(username);

        // Then
        assertNull(userDTO, "UserDTO should be null for non-existent user");

        verify(userService, times(1)).getUserByUsername(username);
    }

    /**
     * Tests deleting an existing user.
     */
    @Test
    @DisplayName("Should delete an existing user")
    void it_should_delete_user() {
        // Given
        String username = "testuser";

        // Mock the behavior to do nothing when deleteUser is called
        doNothing().when(userService).deleteUser(username);

        // When
        userFacade.deleteUser(username);

        // Then
        verify(userService, times(1)).deleteUser(username);
    }

    /**
     * Tests that deleting a non-existent user throws UserNotFoundException.
     */
    @Test
    @DisplayName("Should throw UserNotFoundException when deleting non-existent user")
    void it_should_throw_user_not_found_exception_when_deleting_non_existent_user() {
        // Given
        String username = "nonexistentuser";

        // Mock the behavior to throw exception when deleteUser is called with non-existent username
        doThrow(new UserNotFoundException(username))
                .when(userService).deleteUser(username);

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userFacade.deleteUser(username);
        });

        assertEquals("User not found for username: nonexistentuser", exception.getMessage());

        verify(userService, times(1)).deleteUser(username);
    }
}
