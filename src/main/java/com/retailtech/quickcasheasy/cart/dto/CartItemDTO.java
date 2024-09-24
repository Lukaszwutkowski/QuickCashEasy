package com.retailtech.quickcasheasy.cart.dto;

import java.math.BigDecimal;

public class CartItemDTO {

    private final String productName;
    private final int quantity;
    private final BigDecimal totalPrice;

    public CartItemDTO(String productName, int quantity, BigDecimal totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
