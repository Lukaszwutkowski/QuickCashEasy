package com.retailtech.quickcasheasy.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);  // Mock the repository
        userService = new UserService(userRepository);  // Inject mock into service
    }

    @Test
    void it_should_register_user() {
        // Given
        when(userRepository.getUserByUsername("john_doe")).thenReturn(Optional.empty());

        // When
        userService.registerUser(1L, "john_doe", "password123", UserRole.CASHIER);

        // Then
        verify(userRepository, times(1)).saveUser(any(User.class));  // Verify saveUser was called once
    }

    @Test
    void it_should_throw_exception_if_user_already_exists() {
        // Given
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(new User(1L, "john_doe", "password123", UserRole.CASHIER)));

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.registerUser(1L, "john_doe", "password123", UserRole.CASHIER));
    }

    @Test
    void it_should_authenticate_user() {
        // Given
        User user = new User(1L, "john_doe", "password123", UserRole.CASHIER);
        when(userRepository.getUserByUsername("john_doe")).thenReturn(Optional.of(user));

        // When
        boolean result = userService.authenticateUser("john_doe", "password123");

        // Then
        assertTrue(result);  // Authentication should succeed
    }

    @Test
    void it_should_not_authenticate_user_with_wrong_password() {
        // Given
        User user = new User(1L, "john_doe", "password123", UserRole.CASHIER);
        when(userRepository.getUserByUsername("john_doe")).thenReturn(Optional.of(user));

        // When
        boolean result = userService.authenticateUser("john_doe", "wrong_password");

        // Then
        assertFalse(result);  // Authentication should fail
    }
}
