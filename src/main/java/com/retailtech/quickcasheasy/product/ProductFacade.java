package com.retailtech.quickcasheasy.product;

import com.retailtech.quickcasheasy.category.CategoryFacade;
import com.retailtech.quickcasheasy.product.dto.ProductDTO;

import java.util.List;
import java.util.stream.Collectors;

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
        return productService.getAllProducts().stream()
                .map(this::mapToDTO)    // Convert each Product to ProductDTO
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a product by its barcode as a ProductDTO.
     *
     * @param barcode the barcode of the product
     * @return the ProductDTO if found, or null if not found
     */
    public ProductDTO getProductByBarcode(String barcode) {
        Product product = productService.getProductByBarcode(barcode);  // Retrieve the Product from ProductService
        return (product != null) ? mapToDTO(product) : null;            // Convert to ProductDTO if not null
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
    public void createProduct(String barcode, String name, double price, Long categoryId) {
        // Check if the category exists using the CategoryFacade
        if (!categoryFacade.categoryExists(categoryId)) {
            throw new RuntimeException("Category not found for id: " + categoryId);
        }

        // Create a new product instance with the provided details
        Product product = new Product(barcode, name, price, categoryId);

        // Add the product using the ProductService
        productService.addProduct(product);
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
     * @return the name of the product
     */
    public String getProductName(String barcode) {
        Product product = productService.getProductByBarcode(barcode);  // Use ProductService to get the product
        return (product != null) ? product.getName() : null;            // Return name or null if not found
    }

    /**
     * Retrieves the price of a product given its barcode.
     *
     * @param barcode the barcode of the product
     * @return the price of the product
     */
    public double getProductPrice(String barcode) {
        Product product = productService.getProductByBarcode(barcode);  // Use ProductService to get the product
        return (product != null) ? product.getPrice() : 0.0;            // Return price or 0.0 if not found
    }

    /**
     * Private helper method to convert a Product to a ProductDTO.
     *
     * @param product the Product to convert
     * @return the corresponding ProductDTO
     */
    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(
                product.getBarcode(),
                product.getName(),
                product.getPrice(),
                product.getCategoryId()
        );
    }
}
