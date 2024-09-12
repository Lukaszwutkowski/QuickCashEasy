package com.retailtech.quickcasheasy.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CartRepositoryImplTest {

    private CartRepositoryImpl cartRepository;

    @BeforeEach
    void setUp() {
        cartRepository = new CartRepositoryImpl();  // Initialize the repository
    }

    @Test
    void it_should_save_and_retrieve_cart() {
        // Given
        List<CartItem> cartItems = List.of(
                new CartItem("Apple", 2, BigDecimal.valueOf(1.5)),
                new CartItem("Banana", 3, BigDecimal.valueOf(1.2))
        );

        // When
        cartRepository.saveCart(cartItems);  // Save the cart
        Optional<List<CartItem>> retrievedCart = cartRepository.getCart();  // Retrieve the saved cart

        // Then
        assertTrue(retrievedCart.isPresent());  // Verify the cart exists
        assertEquals(2, retrievedCart.get().size());  // Verify there are 2 items in the cart
        assertEquals("Apple", retrievedCart.get().get(0).getProductName());  // Verify the first product's name
    }

    @Test
    void it_should_return_empty_if_cart_is_not_saved() {
        // When
        Optional<List<CartItem>> retrievedCart = cartRepository.getCart();  // Try to retrieve an unsaved cart

        // Then
        assertTrue(retrievedCart.isEmpty());  // Verify the result is empty
    }

    @Test
    void it_should_clear_cart() {
        // Given
        List<CartItem> cartItems = List.of(
                new CartItem("Apple", 2, BigDecimal.valueOf(1.5))
        );
        cartRepository.saveCart(cartItems);  // Save the cart

        // When
        cartRepository.clearCart();  // Clear the cart
        Optional<List<CartItem>> retrievedCart = cartRepository.getCart();  // Try to retrieve the cleared cart

        // Then
        assertTrue(retrievedCart.isEmpty());  // Verify the cart is empty after clearing
    }
}
