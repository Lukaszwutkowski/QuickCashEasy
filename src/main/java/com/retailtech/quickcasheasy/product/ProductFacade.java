package com.retailtech.quickcasheasy.product;

import com.retailtech.quickcasheasy.category.CategoryFacade;

import java.util.List;

public class ProductFacade {

    private final ProductService productService;
    private final CategoryFacade categoryFacade;

    public ProductFacade(ProductService productService, CategoryFacade categoryFacade) {
        this.productService = productService;
        this.categoryFacade = categoryFacade;
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public Product getProductByBarcode(String barcode) {
        return productService.getProductByBarcode(barcode);
    }

    public void createProduct(String barcode, String name, double price, Long categoryId) {
        // Resolve category through the CategoryFacade, if needed
        if (!categoryFacade.categoryExists(categoryId)) {
            throw new RuntimeException("Category not found for id: " + categoryId);
        }

        // Now create the product with the categoryId
        Product product = new Product(barcode, name, price, categoryId);
        productService.addProduct(product);
    }

    public void deleteProduct(String barcode) {
        productService.deleteProduct(barcode);
    }
}
