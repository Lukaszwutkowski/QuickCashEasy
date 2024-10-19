package com.retailtech.quickcasheasy.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> getAllProducts();

    Optional<Product> getProductByBarcode(String barcode);

    void saveProduct(Product product);

    void deleteProductByBarcode(String barcode);

    boolean existsByBarcode(String barcode);
}
