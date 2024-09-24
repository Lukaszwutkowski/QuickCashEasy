package com.retailtech.quickcasheasy.cart.integrationTests;

import com.retailtech.quickcasheasy.cart.CartFacade;
import com.retailtech.quickcasheasy.cart.dto.CartItemDTO;
import com.retailtech.quickcasheasy.product.ProductFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for the CartFacade.
 * Verifies the integration between CartFacade and ProductFacade.
 */
class CartIntegrationTest {

    private ProductFacade productFacade;   // Mocked ProductFacade
    private CartFacade cartFacade;         // CartFacade under test

    /**
     * Sets up the test environment before each test.
     * Initializes the mocked ProductFacade and CartFacade.
     */
    @BeforeEach
    void setUp() {
        productFacade = mock(ProductFacade.class);          // Mock the ProductFacade
        cartFacade = new CartFacade(productFacade);         // Initialize CartFacade with the mocked ProductFacade
    }

    /**
     * Tests adding items to the cart and calculating the total price.
     * Verifies that the cart contains the correct items with correct quantities and total prices.
     */
    @Test
    void it_should_add_items_to_cart_and_calculate_total() {
        // Mock product information returned by the ProductFacade
        when(productFacade.getProductName("123456")).thenReturn("Apple");
        when(productFacade.getProductPrice("123456")).thenReturn(1.50);

        when(productFacade.getProductName("789101")).thenReturn("Banana");
        when(productFacade.getProductPrice("789101")).thenReturn(0.75);

        // Use CartFacade to add products to the cart
        cartFacade.addProductToCart("123456", 2);   // Add 2 Apples
        cartFacade.addProductToCart("789101", 3);   // Add 3 Bananas

        // Retrieve cart items as DTOs
        List<CartItemDTO> cartItems = cartFacade.getCartItems();
        assertEquals(2, cartItems.size(), "Cart should contain 2 items");

        // Verify Apple item in the cart
        CartItemDTO appleItem = cartItems.stream()
                .filter(item -> item.getProductName().equals("Apple"))
                .findFirst()
                .orElse(null);
        assertNotNull(appleItem, "Apple item should be present in the cart");
        assertEquals(2, appleItem.getQuantity(), "Apple quantity should be 2");
        assertEquals(BigDecimal.valueOf(3.00), appleItem.getTotalPrice(), "Apple total price should be 3.00");

        // Verify Banana item in the cart
        CartItemDTO bananaItem = cartItems.stream()
                .filter(item -> item.getProductName().equals("Banana"))
                .findFirst()
                .orElse(null);
        assertNotNull(bananaItem, "Banana item should be present in the cart");
        assertEquals(3, bananaItem.getQuantity(), "Banana quantity should be 3");
        assertEquals(BigDecimal.valueOf(2.25), bananaItem.getTotalPrice(), "Banana total price should be 2.25");

        // Verify total cart value
        BigDecimal total = cartFacade.getTotal();
        assertEquals(BigDecimal.valueOf(5.25), total, "Total cart value should be 5.25");
    }

    /**
     * Tests clearing the cart.
     * Verifies that the cart is empty and the total price is zero after clearing.
     */
    @Test
    void it_should_clear_the_cart() {
        // Mock product information returned by the ProductFacade
        when(productFacade.getProductName("123456")).thenReturn("Apple");
        when(productFacade.getProductPrice("123456")).thenReturn(1.50);

        // Use CartFacade to add a product to the cart
        cartFacade.addProductToCart("123456", 2);   // Add 2 Apples

        // Clear the cart
        cartFacade.clearCart();

        // Verify that the cart is empty
        List<CartItemDTO> cartItems = cartFacade.getCartItems();
        assertTrue(cartItems.isEmpty(), "Cart should be empty after clearing");

        // Verify that the total price is zero
        BigDecimal total = cartFacade.getTotal();
        assertEquals(BigDecimal.ZERO, total, "Total cart value should be zero after clearing");
    }
}
