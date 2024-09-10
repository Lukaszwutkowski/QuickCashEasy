package com.retailtech.quickcasheasy.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void it_should_return_all_products() {
        // Given
        Product product1 = new Product("barcode1", "Product 1", 10.0, 1L);
        Product product2 = new Product("barcode2", "Product 2", 20.0, 2L);
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // When
        List<Product> products = productService.getAllProducts();

        // Then
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getName());
    }

    @Test
    void it_should_return_product_by_barcode() {
        // Given
        Product product = new Product("barcode1", "Product 1", 10.0, 1L);
        when(productRepository.findByBarcode("barcode1")).thenReturn(Optional.of(product));

        // When
        Product foundProduct = productService.getProductByBarcode("barcode1");

        // Then
        assertNotNull(foundProduct);
        assertEquals("Product 1", foundProduct.getName());
    }

    @Test
    void it_should_throw_exception_when_product_not_found_by_barcode() {
        // Given
        when(productRepository.findByBarcode("invalid_barcode")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductByBarcode("invalid_barcode");
        });

        assertEquals("Product not found for barcode: invalid_barcode", exception.getMessage());
    }

    @Test
    void it_should_add_new_product() {
        // Given
        Product product = new Product("barcode1", "Product 1", 10.0, 1L);

        // When
        productService.addProduct(product);

        // Then
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void it_should_delete_product_by_barcode() {
        // Given
        String barcode = "barcode1";

        // When
        productService.deleteProduct(barcode);

        // Then
        verify(productRepository, times(1)).delete(barcode);
    }

}
