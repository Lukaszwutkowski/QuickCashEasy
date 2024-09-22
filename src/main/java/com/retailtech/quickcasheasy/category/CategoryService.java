package com.retailtech.quickcasheasy.category;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal service handling category-related business logic.
 * This class is package-private.
 */
class CategoryService {

    private final List<Category> categoryList = new ArrayList<>();

    /**
     * Adds a new category.
     *
     * @param category the Category to add
     */
    void addCategory(Category category) {
        categoryList.add(category);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category
     * @return the Category if found, or null if not found
     */
    Category getCategoryById(Long id) {
        return categoryList.stream()
                .filter(category -> category.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of all Categories
     */
    List<Category> getAllCategories() {
        return new ArrayList<>(categoryList);
    }

    // Additional internal methods can be added here
}
