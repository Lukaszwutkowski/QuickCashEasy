package com.retailtech.quickcasheasy.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findByBarcode(String barcode);

    void save(Product product);

    void delete(String barcode);
}
