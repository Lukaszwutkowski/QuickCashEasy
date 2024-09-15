package com.retailtech.quickcasheasy.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserFacadeTest {

    private UserService userService;
    private UserFacade userFacade;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);  // Mock the UserService
        userFacade = new UserFacade(userService);  // Initialize UserFacade with the mocked service
    }

    @Test
    void it_should_register_user_via_facade() {
        // When
        userFacade.registerUser("john_doe", "password123", UserRole.CASHIER);

        // Then
        verify(userService, times(1)).registerUser("john_doe", "password123", UserRole.CASHIER);  // Verify service call
    }

    @Test
    void it_should_authenticate_user_via_facade() {
        // Given
        when(userService.authenticateUser("john_doe", "password123")).thenReturn(true);  // Mock authentication

        // When
        boolean result = userFacade.authenticateUser("john_doe", "password123");

        // Then
        assertTrue(result);  // Verify the user was authenticated
        verify(userService, times(1)).authenticateUser("john_doe", "password123");  // Verify service call
    }

    @Test
    void it_should_get_user_by_username_via_facade() {
        // Given
        User user = new User(1L, "john_doe", "password123", UserRole.CASHIER);
        when(userService.getUserByUsername("john_doe")).thenReturn(user);  // Mock retrieval

        // When
        User result = userFacade.getUserByUsername("john_doe");

        // Then
        assertNotNull(result);  // Verify that user is not null
        assertEquals("john_doe", result.getUserName());  // Verify the username
        verify(userService, times(1)).getUserByUsername("john_doe");  // Verify service call
    }

    @Test
    void it_should_delete_user_via_facade() {
        // When
        userFacade.deleteUser("john_doe");

        // Then
        verify(userService, times(1)).deleteUser("john_doe");  // Verify service call
    }
}
