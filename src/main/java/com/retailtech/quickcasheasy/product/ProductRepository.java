package com.retailtech.quickcasheasy.product;

import com.retailtech.quickcasheasy.product.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> getAllProducts();

    Optional<ProductDTO> getProductByBarcode(String barcode);

    void saveProduct(Product product);

    void deleteProductByBarcode(String barcode);

    boolean existsByBarcode(String barcode);
}
