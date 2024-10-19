package com.retailtech.quickcasheasy.product;

import com.retailtech.quickcasheasy.database.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ProductRepository using a database connection.
 */
public class ProductRepositoryImpl implements ProductRepository {

    /**
     * Saves a product to the repository.
     *
     * @param product the product to save
     */
    @Override
    public void saveProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getBarcode() == null) {
            throw new IllegalArgumentException("Barcode cannot be null");
        }

        String sql = "MERGE INTO products (barcode, name, price, category_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getBarcode());
            pstmt.setString(2, product.getName());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setLong(4, product.getCategoryId());

            pstmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving product: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a product by barcode.
     *
     * @param barcode the barcode of the product to retrieve
     * @return an Optional containing the product if found, or empty if not found
     */
    @Override
    public Optional<Product> getProductByBarcode(String barcode) {
        if (barcode == null) {
            return Optional.empty();
        }
        String sql = "SELECT * FROM products WHERE barcode = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, barcode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Product product = new Product(rs.getString("barcode"), rs.getString("name"), rs.getBigDecimal("price"), rs.getLong("category_id"));
                return Optional.of(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving product by barcode: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Deletes a product by barcode.
     *
     * @param barcode the barcode of the product to delete
     */
    @Override
    public void deleteProductByBarcode(String barcode) {
        if (barcode == null) {
            throw new IllegalArgumentException("Barcode cannot be null");
        }
        String sql = "DELETE FROM products WHERE barcode = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, barcode);
            pstmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting product: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all products.
     *
     * @return a list of all products
     */
    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product(rs.getString("barcode"), rs.getString("name"), rs.getBigDecimal("price"), rs.getLong("category_id"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all products: " + e.getMessage(), e);
        }
        return products;
    }

    /**
     * Checks if a product exists by barcode.
     *
     * @param barcode the barcode of the product to check
     * @return true if the product exists, false otherwise
     */
    @Override
    public boolean existsByBarcode(String barcode) {
        return getProductByBarcode(barcode).isPresent();
    }
}
