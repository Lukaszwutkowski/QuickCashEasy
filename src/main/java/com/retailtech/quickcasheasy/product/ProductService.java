package com.retailtech.quickcasheasy.product;

import java.util.List;

class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found for barcode: " + barcode));
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(String barcode) {
        productRepository.delete(barcode);
    }
}
