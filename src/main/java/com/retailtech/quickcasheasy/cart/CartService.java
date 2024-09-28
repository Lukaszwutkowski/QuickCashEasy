package com.retailtech.quickcasheasy.cart;

import com.retailtech.quickcasheasy.product.ProductFacade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CartService {

    private final ProductFacade productFacade;
    private final Map<String, CartItem> cartItems;  // Map storing CartItems, keyed by barcode

    public CartService(ProductFacade productFacade) {
        this.productFacade = productFacade;
        this.cartItems = new HashMap<>();
    }

    /**
     * Add a product to the cart, or increase its quantity if already present
     *
     * @param barcode  the product's barcode
     * @param quantity the quantity to add
     */
    public void addProductToCart(String barcode, int quantity) {
        String productName = productFacade.getProductName(barcode);  // Get product name from facade
        BigDecimal productPrice = productFacade.getProductPrice(barcode);  // Get product price directly

        if (cartItems.containsKey(barcode)) {
            CartItem existingItem = cartItems.get(barcode);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);  // Update quantity if product already in cart
        } else {
            CartItem newItem = new CartItem(productName, quantity, productPrice);  // Create new CartItem
            cartItems.put(barcode, newItem);
        }
    }


    /**
     * Remove a product from the cart
     *
     * @param barcode the product's barcode
     */
    public void removeProductFromCart(String barcode) {
        cartItems.remove(barcode);  // Remove the product from the cart
    }

    /**
     * Update the quantity of a product in the cart
     *
     * @param barcode    the product's barcode
     * @param newQuantity the new quantity to set
     */
    public void updateProductQuantity(String barcode, int newQuantity) {
        if (cartItems.containsKey(barcode)) {
            CartItem item = cartItems.get(barcode);
            item.setQuantity(newQuantity);  // Update the quantity for the product in the cart
        }
    }

    /**
     * Clear all items from the cart
     */
    public void clearCart() {
        cartItems.clear();  // Clear all items from the cart
    }

    /**
     * Get the list of CartItems currently in the cart
     *
     * @return a list of CartItems
     */
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems.values());  // Return a list of CartItems
    }

    /**
     * Get the total price of all items in the cart
     *
     * @return the total price of the cart
     */
    public BigDecimal getTotal() {
        return cartItems.values().stream()
                .map(CartItem::getTotalPrice)  // Calculate the total for each item (price * quantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);  // Sum all totals to get the final total price
    }
}
