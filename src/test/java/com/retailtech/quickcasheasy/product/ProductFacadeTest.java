package com.retailtech.quickcasheasy.product;

import com.retailtech.quickcasheasy.category.CategoryFacade;
import com.retailtech.quickcasheasy.product.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductFacadeTest {

    private ProductFacade productFacade;
    private ProductService productService;
    private CategoryFacade categoryFacade;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        categoryFacade = mock(CategoryFacade.class);
        productFacade = new ProductFacade(productService, categoryFacade);
    }

    @Test
    void it_should_return_all_products_via_facade() {
        // Given
        Product product1 = new Product("barcode1", "Product 1", 10.0, 1L);
        Product product2 = new Product("barcode2", "Product 2", 20.0, 2L);
        when(productService.getAllProducts()).thenReturn(List.of(product1, product2));

        // When
        List<ProductDTO> products = productFacade.getAllProducts();

        // Then
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getName());
    }

    @Test
    void it_should_return_product_by_barcode_via_facade() {
        // Given
        Product product = new Product("barcode1", "Product 1", 10.0, 1L);
        when(productService.getProductByBarcode("barcode1")).thenReturn(product);

        // When
        ProductDTO foundProduct = productFacade.getProductByBarcode("barcode1");

        // Then
        assertNotNull(foundProduct);
        assertEquals("Product 1", foundProduct.getName());
    }

    @Test
    void it_should_create_new_product_via_facade_when_category_exists() {
        // Given
        String barcode = "barcode1";
        String name = "Product 1";
        double price = 10.0;
        Long categoryId = 1L;

        when(categoryFacade.categoryExists(categoryId)).thenReturn(true);

        // When
        productFacade.createProduct(barcode, name, price, categoryId);

        // Then
        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    void it_should_throw_exception_when_creating_product_with_nonexistent_category() {
        // Given
        String barcode = "barcode1";
        String name = "Product 1";
        double price = 10.0;
        Long categoryId = 99L;

        when(categoryFacade.categoryExists(categoryId)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productFacade.createProduct(barcode, name, price, categoryId);
        });

        assertEquals("Category not found for id: 99", exception.getMessage());
    }

    @Test
    void it_should_delete_product_by_barcode_via_facade() {
        // Given
        String barcode = "barcode1";

        // When
        productFacade.deleteProduct(barcode);

        // Then
        verify(productService, times(1)).deleteProduct(barcode);
    }

    @Test
    void it_should_return_product_name() {
        // Given
        Product product = new Product("123", "Apple", 1.5, 1L);
        when(productService.getProductByBarcode("123")).thenReturn(product);

        // When
        String productName = productFacade.getProductName("123");

        // Then
        assertEquals("Apple", productName);
        verify(productService, times(1)).getProductByBarcode("123");
    }

    @Test
    void it_should_return_product_price() {
        // Given
        Product product = new Product("123", "Apple", 1.5, 1L);
        when(productService.getProductByBarcode("123")).thenReturn(product);

        // When
        double productPrice = productFacade.getProductPrice("123");

        // Then
        assertEquals(1.5, productPrice);
        verify(productService, times(1)).getProductByBarcode("123");
    }

}
