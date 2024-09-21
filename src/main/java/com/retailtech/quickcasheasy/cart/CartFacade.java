package com.retailtech.quickcasheasy.cart;

import com.retailtech.quickcasheasy.cart.dto.CartItemDTO;
import com.retailtech.quickcasheasy.product.ProductFacade;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade for cart-related operations.
 * Provides a simplified interface to interact with the shopping cart.
 */
public class CartFacade {

    private final CartService cartService;  // Corrected type from ProductFacade to CartService

    /**
     * Constructor initializing the CartService with the provided ProductFacade.
     *
     * @param productFacade the product facade to retrieve product information
     */
    public CartFacade(ProductFacade productFacade) {
        this.cartService = new CartService(productFacade);  // Instantiate CartService with ProductFacade
    }

    /**
     * Add a product to the cart.
     *
     * @param barcode  the product's barcode
     * @param quantity the quantity to add
     */
    public void addProductToCart(String barcode, int quantity) {
        cartService.addProductToCart(barcode, quantity);
    }

    /**
     * Remove a product from the cart.
     *
     * @param barcode the product's barcode
     */
    public void removeProductFromCart(String barcode) {
        cartService.removeProductFromCart(barcode);
    }

    /**
     * Update the quantity of a product in the cart.
     *
     * @param barcode     the product's barcode
     * @param newQuantity the new quantity to set
     */
    public void updateProductQuantity(String barcode, int newQuantity) {
        cartService.updateProductQuantity(barcode, newQuantity);
    }

    /**
     * Clear the cart.
     */
    public void clearCart() {
        cartService.clearCart();
    }

    /**
     * Get all items in the cart as DTOs.
     *
     * @return a list of CartItemDTOs
     */
    public List<CartItemDTO> getCartItems() {
        return cartService.getCartItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProductName(),
                        item.getQuantity(),
                        item.getTotalPrice()))
                .collect(Collectors.toList());
    }

    /**
     * Get the total price of the cart.
     *
     * @return the total price
     */
    public BigDecimal getTotal() {
        return cartService.getTotal();
    }
}
