package com.retailtech.quickcasheasy.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartRepositoryImpl implements CartRepository {

    // In-memory storage for cart items
    private List<CartItem> storedCartItems = new ArrayList<>();

    /**
     * Save the current cart to in-memory storage.
     *
     * @param cartItems the list of items to save
     */
    @Override
    public void saveCart(List<CartItem> cartItems) {
        storedCartItems = new ArrayList<>(cartItems);  // Save a copy of the cart items
    }

    /**
     * Retrieve the saved cart from in-memory storage.
     *
     * @return an Optional containing the list of CartItems if the cart exists
     */
    @Override
    public Optional<List<CartItem>> getCart() {
        if (storedCartItems.isEmpty()) {
            return Optional.empty();  // Return empty if no cart exists
        }
        return Optional.of(new ArrayList<>(storedCartItems));  // Return a copy of the stored cart items
    }

    /**
     * Clear the saved cart from in-memory storage.
     */
    @Override
    public void clearCart() {
        storedCartItems.clear();  // Clear the cart items
    }
}
