package com.retailtech.quickcasheasy.cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository {

    /**
     * Save the current cart to the repository.
     *
     * @param cartItems the list of items to save
     */
    void saveCart(List<CartItem> cartItems);

    /**
     * Retrieve the cart from the repository.
     *
     * @return a list of CartItems if the cart exists
     */
    Optional<List<CartItem>> getCart();

    /**
     * Clear the cart from the repository.
     */
    void clearCart();
}
