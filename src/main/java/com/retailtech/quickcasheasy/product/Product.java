package com.retailtech.quickcasheasy.product;

import java.math.BigDecimal;

class Product {

    private String barcode;
    private String name;
    private BigDecimal price;
    private Long categoryId;

    public Product(String barcode, String name, BigDecimal price, Long categoryId) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = BigDecimal.valueOf(price);
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
