package com.retailtech.quickcasheasy.product;

import com.retailtech.quickcasheasy.category.CategoryFacade;
import com.retailtech.quickcasheasy.product.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductFacadeTest {

    @Mock
    private ProductService productService;

    @Mock
    private CategoryFacade categoryFacade;

    @InjectMocks
    private ProductFacade productFacade;

    @Test
    void it_should_return_product_by_barcode_via_facade() {
        // Given
        String barcode = "123456";
        ProductDTO productDTO = new ProductDTO(barcode, "Product A", BigDecimal.valueOf(10.99), 1L);
        when(productService.getProductByBarcode(barcode)).thenReturn(productDTO);

        // When
        ProductDTO result = productFacade.getProductByBarcode(barcode);

        // Then
        assertNotNull(result);
        assertEquals("Product A", result.getName());
        assertEquals("123456", result.getBarcode());
        assertEquals(BigDecimal.valueOf(10.99), result.getPrice());
    }

    @Test
    void it_should_create_and_return_all_products_via_facade() {
        // Given
        ProductDTO product1 = new ProductDTO("123456", "Product A", BigDecimal.valueOf(10.99), 1L);
        ProductDTO product2 = new ProductDTO("789012", "Product B", BigDecimal.valueOf(12.99), 2L);
        when(productService.getAllProducts()).thenReturn(List.of(product1, product2));

        // When
        List<ProductDTO> products = productFacade.getAllProducts();

        // Then
        assertNotNull(products);
        assertEquals(2, products.size());

        ProductDTO foundProduct1 = products.stream()
                .filter(p -> p.getName().equals("Product A"))
                .findFirst()
                .orElse(null);
        assertNotNull(foundProduct1);
        assertEquals("123456", foundProduct1.getBarcode());
        assertEquals(BigDecimal.valueOf(10.99), foundProduct1.getPrice());

        ProductDTO foundProduct2 = products.stream()
                .filter(p -> p.getName().equals("Product B"))
                .findFirst()
                .orElse(null);
        assertNotNull(foundProduct2);
        assertEquals("789012", foundProduct2.getBarcode());
        assertEquals(BigDecimal.valueOf(12.99), foundProduct2.getPrice());
    }

    @Test
    void it_should_check_if_product_exists_via_facade() {
        // Given
        String barcode = "123456";
        when(productService.productExists(barcode)).thenReturn(true);

        // When
        boolean exists = productFacade.productExists(barcode);

        // Then
        assertTrue(exists);
    }

    @Test
    void it_should_delete_product_via_facade() {
        // Given
        String barcode = "123456";

        // When
        productFacade.deleteProduct(barcode);

        // Then
        verify(productService, times(1)).deleteProduct(barcode);
    }

    @Test
    void it_should_create_product_via_facade() {
        // Given
        String barcode = "123456";
        String name = "Product A";
        BigDecimal price = BigDecimal.valueOf(10.99);
        Long categoryId = 1L;
        when(categoryFacade.categoryExists(categoryId)).thenReturn(true);

        // When
        productFacade.createProduct(barcode, name, price, categoryId);

        // Then
        verify(productService, times(1)).createProduct(barcode, name, price, categoryId);
    }
}
