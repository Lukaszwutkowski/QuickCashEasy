package com.retailtech.quickcasheasy.cart;

import com.retailtech.quickcasheasy.cart.dto.CartItemDTO;
import com.retailtech.quickcasheasy.product.ProductFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartFacadeTest {

    private ProductFacade productFacade;
    private CartFacade cartFacade;

    @BeforeEach
    void setUp() {
        productFacade = mock(ProductFacade.class);
        cartFacade = new CartFacade(productFacade);
    }

    @Test
    void it_should_add_product_to_cart() {
        // Given
        when(productFacade.getProductName("123456")).thenReturn("Apple");
        when(productFacade.getProductPrice("123456")).thenReturn(BigDecimal.valueOf(1.50));

        // When
        cartFacade.addProductToCart("123456", 2);

        // Then
        List<CartItemDTO> cartItems = cartFacade.getCartItems();
        assertEquals(1, cartItems.size());
        CartItemDTO item = cartItems.get(0);
        assertEquals("Apple", item.getProductName());
        assertEquals(2, item.getQuantity());
        assertEquals(BigDecimal.valueOf(3.00), item.getTotalPrice());
    }

    @Test
    void it_should_remove_product_from_cart() {
        // Given
        when(productFacade.getProductName("123456")).thenReturn("Apple");
        when(productFacade.getProductPrice("123456")).thenReturn(BigDecimal.valueOf(1.50));
        cartFacade.addProductToCart("123456", 2);

        // When
        cartFacade.removeProductFromCart("123456");

        // Then
        List<CartItemDTO> cartItems = cartFacade.getCartItems();
        assertTrue(cartItems.isEmpty());
    }

    @Test
    void it_should_update_product_quantity_in_cart() {
        // Given
        when(productFacade.getProductName("123456")).thenReturn("Apple");
        when(productFacade.getProductPrice("123456")).thenReturn(BigDecimal.valueOf(1.50));
        cartFacade.addProductToCart("123456", 2);

        // When
        cartFacade.updateProductQuantity("123456", 5);

        // Then
        List<CartItemDTO> cartItems = cartFacade.getCartItems();
        CartItemDTO item = cartItems.get(0);
        assertEquals(5, item.getQuantity());
        assertEquals(BigDecimal.valueOf(7.50), item.getTotalPrice());
    }

    @Test
    void it_should_clear_cart() {
        // Given
        when(productFacade.getProductName("123456")).thenReturn("Apple");
        when(productFacade.getProductPrice("123456")).thenReturn(BigDecimal.valueOf(1.50));
        cartFacade.addProductToCart("123456", 2);

        // When
        cartFacade.clearCart();

        // Then
        List<CartItemDTO> cartItems = cartFacade.getCartItems();
        assertTrue(cartItems.isEmpty());
        BigDecimal total = cartFacade.getTotal();
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void it_should_return_cart_total() {
        // Given
        when(productFacade.getProductName("123456")).thenReturn("Apple");
        when(productFacade.getProductPrice("123456")).thenReturn(BigDecimal.valueOf(1.50));
        when(productFacade.getProductName("789101")).thenReturn("Banana");
        when(productFacade.getProductPrice("789101")).thenReturn(BigDecimal.valueOf(0.75));

        cartFacade.addProductToCart("123456", 2);
        cartFacade.addProductToCart("789101", 3);

        // When
        BigDecimal total = cartFacade.getTotal();

        // Then
        assertEquals(BigDecimal.valueOf(5.25), total);
    }
}
