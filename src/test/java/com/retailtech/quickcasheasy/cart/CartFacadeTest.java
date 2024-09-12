package com.retailtech.quickcasheasy.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartFacadeTest {

    private CartService cartService;
    private CartFacade cartFacade;

    @BeforeEach
    void setUp() {
        cartService = mock(CartService.class);  // Mocking CartService
        cartFacade = new CartFacade(cartService);  // Initializing CartFacade with mocked CartService
    }

    @Test
    void it_should_add_product_to_cart() {
        // When
        cartFacade.addProductToCart("123456", 2);

        // Then
        verify(cartService, times(1)).addProductToCart("123456", 2);  // Verify that CartService's method was called
    }

    @Test
    void it_should_remove_product_from_cart() {
        // When
        cartFacade.removeProductFromCart("123456");

        // Then
        verify(cartService, times(1)).removeProductFromCart("123456");  // Verify that CartService's method was called
    }

    @Test
    void it_should_update_product_quantity_in_cart() {
        // When
        cartFacade.updateProductQuantity("123456", 5);

        // Then
        verify(cartService, times(1)).updateProductQuantity("123456", 5);  // Verify that CartService's method was called
    }

    @Test
    void it_should_clear_cart() {
        // When
        cartFacade.clearCart();

        // Then
        verify(cartService, times(1)).clearCart();  // Verify that CartService's method was called
    }

    @Test
    void it_should_return_all_cart_items() {
        // Given
        List<CartItem> cartItems = List.of(
                new CartItem("Apple", 2, BigDecimal.valueOf(1.5)),
                new CartItem("Banana", 3, BigDecimal.valueOf(1.2))
        );
        when(cartService.getCartItems()).thenReturn(cartItems);  // Mocking cart items

        // When
        List<CartItem> result = cartFacade.getCartItems();

        // Then
        assertEquals(2, result.size());  // Verify the cart contains 2 items
        verify(cartService, times(1)).getCartItems();  // Verify that CartService's method was called
    }

    @Test
    void it_should_return_cart_total() {
        // Given
        when(cartService.getTotal()).thenReturn(BigDecimal.valueOf(5.4));  // Mocking total price

        // When
        BigDecimal total = cartFacade.getTotal();

        // Then
        assertEquals(BigDecimal.valueOf(5.4), total);  // Verify the total price
        verify(cartService, times(1)).getTotal();  // Verify that CartService's method was called
    }
}
