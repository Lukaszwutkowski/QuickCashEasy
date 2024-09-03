package com.retailtech.quickcasheasy.category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CategoryRepositoryImpl implements CategoryRepository {

    private final String url = "jdbc:h2:~/test";
    private final String user = "sa";
    private final String password = "";

    public CategoryRepositoryImpl() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS categories (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), description VARCHAR(255))");
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database", e);
        }
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM categories")) {
            while (rs.next()) {
                Category category = new Category(rs.getLong("id"), rs.getString("name"), rs.getString("description"));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching categories", e);
        }
        return categories;
    }

    @Override
    public Optional<Category> findById(Long id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM categories WHERE id = ?")) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Category(rs.getLong("id"), rs.getString("name"), rs.getString("description")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching category by id", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Category category) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO categories (name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                category.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving category", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = connection.prepareStatement("DELETE FROM categories WHERE id = ?")) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting category", e);
        }
    }
}
