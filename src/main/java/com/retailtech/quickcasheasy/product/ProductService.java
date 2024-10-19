package com.retailtech.quickcasheasy.product;

import com.retailtech.quickcasheasy.category.CategoryFacade;
import com.retailtech.quickcasheasy.product.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for managing product-related operations.
 * Handles the business logic for creating, retrieving, deleting, and verifying products.
 */
class ProductService {

    private final ProductRepository productRepository;
    private final CategoryFacade categoryFacade;

    /**
     * Constructor for ProductService.
     *
     * @param productRepository The repository for product data persistence.
     * @param categoryFacade    The facade for handling category-related operations.
     */
    public ProductService(ProductRepository productRepository, CategoryFacade categoryFacade) {
        this.productRepository = productRepository;
        this.categoryFacade = categoryFacade;
    }

    /**
     * Retrieves a list of all products and maps them to ProductDTOs.
     *
     * @return A list of all products as ProductDTO objects.
     */
    public List<ProductDTO> getAllProducts() {
        // Fetch all products from the repository and map them to ProductDTO
        return productRepository.getAllProducts().stream()
                .map(product -> new ProductDTO(product.getBarcode(), product.getName(), product.getPrice(), product.getCategoryId()))
                .toList();
    }

    /**
     * Retrieves a product by its barcode and maps it to a ProductDTO.
     *
     * @param barcode The barcode of the product to retrieve.
     * @return The ProductDTO for the given barcode.
     * @throws RuntimeException if the product with the given barcode is not found.
     */
    public ProductDTO getProductByBarcode(String barcode) {
        // Find a product by its barcode and map it to ProductDTO, throw exception if not found
        return productRepository.getProductByBarcode(barcode)
                .map(product -> new ProductDTO(product.getBarcode(), product.getName(), product.getPrice(), product.getCategoryId()))
                .orElseThrow(() -> new RuntimeException("Product not found for barcode: " + barcode));
    }

    /**
     * Creates a new product.
     *
     * @param barcode    The barcode of the new product.
     * @param name       The name of the new product.
     * @param price      The price of the new product.
     * @param categoryId The ID of the category the product belongs to.
     * @throws RuntimeException if the category with the given ID does not exist.
     */
    public void createProduct(String barcode, String name, BigDecimal price, Long categoryId) {
        // Check if the category exists before creating the product
        if (!categoryFacade.categoryExists(categoryId)) {
            throw new RuntimeException("Category not found for id: " + categoryId);
        }

        // Create a new product using the provided details
        Product product = new Product(barcode, name, price, categoryId);

        // Save the product to the repository
        productRepository.saveProduct(product);
    }

    /**
     * Deletes a product by its barcode.
     *
     * @param barcode The barcode of the product to delete.
     */
    public void deleteProduct(String barcode) {
        // Delete the product by its barcode using the repository
        productRepository.deleteProductByBarcode(barcode);
    }

    /**
     * Checks if a product exists by its barcode.
     *
     * @param barcode The barcode of the product to check.
     * @return True if the product exists, false otherwise.
     */
    public boolean productExists(String barcode) {
        // Check if a product exists in the repository by its barcode
        return productRepository.existsByBarcode(barcode);
    }
}
