package com.retailtech.quickcasheasy.product;

import com.retailtech.quickcasheasy.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;

class ProductRepositoryImpl implements ProductRepository {

    private final DatabaseUtils dbUtils;

    public ProductRepositoryImpl(DatabaseUtils dbUtils) {
        this.dbUtils = dbUtils;
        initializeDatabase();
    }

    // Initialize the database table if it doesn't exist
    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS products (barcode VARCHAR(255) PRIMARY KEY, name VARCHAR(255), price DOUBLE, category_id BIGINT)";
        dbUtils.executeUpdate(sql);
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return dbUtils.executeQuery(sql, this::mapResultSetToProductList);
    }
    @Override
    public Optional<Product> findByBarcode(String barcode) {
        String sql = "SELECT * FROM products WHERE barcode = ?";
        return dbUtils.executeQuery(sql, this::mapResultSetToOptionalProduct, barcode);
    }

    @Override
    public void save(Product product) {
        String sql = "INSERT INTO products (barcode, name, price, category_id) VALUES (?, ?, ?, ?)";
        dbUtils.executeUpdate(sql, product.getBarcode(), product.getName(), product.getPrice(), product.getCategoryId());
    }

    @Override
    public void delete(String barcode) {
        String sql = "DELETE FROM products WHERE barcode = ?";
        dbUtils.executeUpdate(sql, barcode);
    }

    @Override
    public boolean existsByBarcode(String barcode) {
        String sql = "SELECT * FROM products WHERE barcode = ?";
        return dbUtils.executeQuery(sql, this::mapResultSetToOptionalProduct, barcode).isPresent();
    }

    // Helper method to map ResultSet to a list of products
    private List<Product> mapResultSetToProductList(ResultSet rs) {
        List<Product> products = new ArrayList<>();
        try {
            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping result set to product list", e);
        }
        return products;
    }

    // Helper method to map ResultSet to an optional product
    private Optional<Product> mapResultSetToOptionalProduct(ResultSet rs) {
        try {
            if (rs.next()) {
                return Optional.of(mapRowToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping result set to optional product", e);
        }
        return Optional.empty();
    }

    // Helper method to map a row to a Product object
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        return new Product(rs.getString("barcode"), rs.getString("name"), rs.getBigDecimal("price"), rs.getLong("category_id"));
    }
}
