package com.retailtech.quickcasheasy.cart;

import com.retailtech.quickcasheasy.product.ProductFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartServiceTest {

    private CartService cartService;
    private ProductFacade productFacade; // Mocking ProductFacade

    @BeforeEach
    void setUp() {
        productFacade = mock(ProductFacade.class);  // Mocking the facade
        cartService = new CartService(productFacade);   // Initialize cart service with the facade
    }

    @Test
    void it_should_add_item_to_cart() {
        // Given
        when(productFacade.getProductPrice("123456")).thenReturn(1.50); // Facade returns price of product
        when(productFacade.getProductName("123456")).thenReturn("Apple"); // Facade returns name of product

        // When
        cartService.addProductToCart("123456", 2); // Adding 2 Apples to the cart

        // Then
        List<CartItem> cartItems = cartService.getCartItems();
        assertEquals(1, cartItems.size());  // Verify the cart contains 1 type of product
        assertEquals(2, cartItems.get(0).getQuantity());  // Verify the quantity is 2
        assertEquals(BigDecimal.valueOf(3.00), cartService.getTotal());  // Verify the total is correct
    }

    @Test
    void it_should_remove_item_from_cart() {
        // Given
        when(productFacade.getProductPrice("123456")).thenReturn(1.50); // Facade returns price of product
        when(productFacade.getProductName("123456")).thenReturn("Apple"); // Facade returns name of product

        cartService.addProductToCart("123456", 2); // Adding 2 Apples to the cart

        // When
        cartService.removeProductFromCart("123456");

        // Then
        List<CartItem> cartItems = cartService.getCartItems();
        assertTrue(cartItems.isEmpty());  // Cart should be empty after removal
        assertEquals(BigDecimal.ZERO, cartService.getTotal());  // Total should be zero
    }

    @Test
    void it_should_update_quantity_in_cart() {
        // Given
        when(productFacade.getProductPrice("123456")).thenReturn(1.50); // Facade returns price of product
        when(productFacade.getProductName("123456")).thenReturn("Apple"); // Facade returns name of product

        cartService.addProductToCart("123456", 2); // Adding 2 Apples to the cart

        // When
        cartService.updateProductQuantity("123456", 5);  // Updating the quantity to 5

        // Then
        List<CartItem> cartItems = cartService.getCartItems();
        assertEquals(5, cartItems.get(0).getQuantity());  // Verify the quantity was updated
        assertEquals(BigDecimal.valueOf(7.50), cartService.getTotal());  // Verify the total is correct
    }

    @Test
    void it_should_clear_the_cart() {
        // Given
        when(productFacade.getProductPrice("123456")).thenReturn(1.50); // Facade returns price of product
        when(productFacade.getProductName("123456")).thenReturn("Apple"); // Facade returns name of product

        cartService.addProductToCart("123456", 2); // Adding 2 Apples to the cart

        // When
        cartService.clearCart();  // Clear the cart

        // Then
        assertTrue(cartService.getCartItems().isEmpty());  // Verify cart is empty
        assertEquals(BigDecimal.ZERO, cartService.getTotal());  // Verify total is zero
    }
}
