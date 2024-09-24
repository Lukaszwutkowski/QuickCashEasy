package com.retailtech.quickcasheasy.category;

import com.retailtech.quickcasheasy.category.dto.CategoryDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade for category-related operations.
 * Provides a simplified interface to interact with categories.
 */
public class CategoryFacade {

    private final CategoryService categoryService;  // Internal service handling category logic

    /**
     * Constructor initializing the CategoryService.
     */
    public CategoryFacade() {
        this.categoryService = new CategoryService();
    }

    /**
     * Retrieves all categories as CategoryDTOs.
     *
     * @return a list of all categories
     */
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a category by its ID as a CategoryDTO.
     *
     * @param id the ID of the category
     * @return the CategoryDTO if found, or null if not found
     */
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryService.getCategoryById(id);
        return (category != null) ? mapToDTO(category) : null;
    }

    /**
     * Creates a new category with the given details.
     *
     * @param name        the name of the new category
     * @param description the description of the new category
     */
    public void createCategory(Long id, String name, String description) {
        Category category = new Category(id, name, description);
        categoryService.addCategory(category);
    }

    /**
     * Checks if a category exists by its ID.
     *
     * @param id the ID of the category
     * @return true if the category exists, false otherwise
     */
    public boolean categoryExists(Long id) {
        return categoryService.getCategoryById(id) != null;
    }

    /**
     * Private helper method to convert a Category to a CategoryDTO.
     *
     * @param category the Category to convert
     * @return the corresponding CategoryDTO
     */
    private CategoryDTO mapToDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
