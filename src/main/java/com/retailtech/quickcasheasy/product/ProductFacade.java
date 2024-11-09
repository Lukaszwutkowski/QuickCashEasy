package com.retailtech.quickcasheasy.product;

import com.retailtech.quickcasheasy.category.CategoryFacade;
import com.retailtech.quickcasheasy.product.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Facade for product-related operations.
 * Provides a simplified interface to interact with products.
 */
public class ProductFacade {

    private final ProductService productService;     // Internal service handling product logic
    private final CategoryFacade categoryFacade;     // Facade for category-related operations

    /**
     * Constructor initializing the ProductService and CategoryFacade.
     *
     * @param productService  the product service to handle business logic
     * @param categoryFacade  the category facade to handle category operations
     */
    public ProductFacade(ProductService productService, CategoryFacade categoryFacade) {
        this.productService = productService;
        this.categoryFacade = categoryFacade;
    }

    /**
     * Retrieves all products as ProductDTOs.
     *
     * @return a list of all products
     */
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Retrieves a product by its barcode as a ProductDTO.
     *
     * @param barcode the barcode of the product
     * @return the ProductDTO if found, or null if not found
     */
    public ProductDTO getProductByBarcode(String barcode) {
        return productService.getProductByBarcode(barcode);  // No need to map, already returning ProductDTO
    }

    /**
     * Creates a new product with the given details.
     *
     * @param barcode     the barcode of the new product
     * @param name        the name of the new product
     * @param price       the price of the new product
     * @param categoryId  the ID of the category the product belongs to
     * @throws RuntimeException if the category does not exist
     */
    public void createProduct(String barcode, String name, BigDecimal price, Long categoryId) {
        // Check if the category exists using the CategoryFacade
        if (!categoryFacade.categoryExists(categoryId)) {
            throw new RuntimeException("Category not found for id: " + categoryId);
        }

        // Add the product using the ProductService directly with separate arguments
        productService.createProduct(barcode, name, price, categoryId);
    }

    /**
     * Deletes a product by its barcode.
     *
     * @param barcode the barcode of the product to delete
     */
    public void deleteProduct(String barcode) {
        productService.deleteProduct(barcode);   // Delegates to ProductService
    }

    /**
     * Retrieves the name of a product given its barcode.
     *
     * @param barcode the barcode of the product
     * @return the name of the product, or null if not found
     */
    public String getProductName(String barcode) {
        ProductDTO product = productService.getProductByBarcode(barcode);  // Use ProductService to get the product
        return (product != null) ? product.getName() : null;            // Return name or null if not found
    }

    /**
     * Retrieves the price of a product given its barcode.
     *
     * @param barcode the barcode of the product
     * @return the price of the product, or throws RuntimeException if not found
     */
    public BigDecimal getProductPrice(String barcode) {
        ProductDTO product = productService.getProductByBarcode(barcode);  // Use ProductService to get the product
        if (product != null) {
            return product.getPrice();  // Directly return BigDecimal from ProductDTO
        } else {
            throw new RuntimeException("Product not found for barcode: " + barcode);
        }
    }

    /**
     * Checks if a product exists by its barcode.
     *
     * @param barcode The barcode of the product to check.
     * @return True if the product exists, false otherwise.
     */
    public boolean productExists(String barcode) {
        return productService.productExists(barcode);   // Delegates to ProductService
    }
}
