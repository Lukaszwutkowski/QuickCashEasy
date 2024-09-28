package com.retailtech.quickcasheasy.category;

import com.retailtech.quickcasheasy.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CategoryRepositoryImpl implements CategoryRepository {

    private final DatabaseUtils dbUtils;

    public CategoryRepositoryImpl(DatabaseUtils dbUtils) {
        this.dbUtils = dbUtils;
        initializeDatabase();
    }

    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS categories (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), description VARCHAR(255))";
        dbUtils.executeUpdate(sql);
    }

    @Override
    public List<Category> getCategories() {
        String sql = "SELECT * FROM categories";
        return dbUtils.executeQuery(sql, this::mapResultSetToCategoryList);
    }

    @Override
    public Optional<Category> findById(Long id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        return dbUtils.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    return Optional.of(new Category(rs.getLong("id"), rs.getString("name"), rs.getString("description")));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        }, id);
    }

    @Override
    public void save(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        dbUtils.executeUpdate(sql, category.getName(), category.getDescription());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        dbUtils.executeUpdate(sql, id);
    }

    private List<Category> mapResultSetToCategoryList(ResultSet rs) {
        List<Category> categories = new ArrayList<>();
        try {
            while (rs.next()) {
                categories.add(new Category(rs.getLong("id"), rs.getString("name"), rs.getString("description")));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error mapping result set to category list", e);
        }
        return categories;
    }
}
