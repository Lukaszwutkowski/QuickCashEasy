package com.retailtech.quickcasheasy.product.dto;

/**
 * Data Transfer Object for Product information.
 */
public class ProductDTO {

    private final String barcode;
    private final String name;
    private final double price;
    private final Long categoryId;

    public ProductDTO(String barcode, String name, double price, Long categoryId) {
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

    public double getPrice() {
        return price;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
