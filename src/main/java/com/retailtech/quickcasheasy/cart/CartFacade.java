package com.retailtech.quickcasheasy.cart;

import java.math.BigDecimal;
import java.util.List;

public class CartFacade {

    private final CartService cartService;

    // Constructor
    public CartFacade(CartService cartService) {
        this.cartService = cartService;
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
     * Get all items in the cart.
     *
     * @return a list of CartItems
     */
    public List<CartItem> getCartItems() {
        return cartService.getCartItems();
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
