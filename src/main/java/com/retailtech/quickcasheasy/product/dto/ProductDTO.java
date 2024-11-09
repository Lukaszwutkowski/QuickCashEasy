package com.retailtech.quickcasheasy.product.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Product information.
 */
public class ProductDTO {

    private final String barcode;
    private final String name;
    private final BigDecimal price;
    private final Long categoryId;

    public ProductDTO(String barcode, String name, BigDecimal price, Long categoryId) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    // Getters
    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
