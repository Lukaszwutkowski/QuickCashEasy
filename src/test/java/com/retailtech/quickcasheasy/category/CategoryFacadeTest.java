package com.retailtech.quickcasheasy.category;

import com.retailtech.quickcasheasy.category.dto.CategoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CategoryFacade.
 * Focuses on testing the facade's public methods using DTOs.
 */
public class CategoryFacadeTest {

    private CategoryFacade categoryFacade;

    /**
     * Sets up the test environment before each test.
     * Initializes the CategoryFacade.
     */
    @BeforeEach
    void setUp() {
        categoryFacade = new CategoryFacade();  // Initialize with real CategoryFacade
    }

    /**
     * Tests creating and retrieving all categories via the facade.
     */
    @Test
    void it_should_create_and_return_all_categories_via_facade() {
        // Given
        String name1 = "Beverages";
        String description1 = "Drinks and beverages";

        String name2 = "Snacks";
        String description2 = "All types of snacks";

        // When
        categoryFacade.createCategory(1L, name1, description1);
        categoryFacade.createCategory(2L, name2, description2);

        // Then
        List<CategoryDTO> categories = categoryFacade.getAllCategories();
        assertNotNull(categories, "Categories list should not be null");
        assertEquals(2, categories.size(), "There should be 2 categories");

        // Verify category details
        CategoryDTO category1 = categories.stream()
                .filter(c -> c.getName().equals("Beverages"))
                .findFirst()
                .orElse(null);
        assertNotNull(category1, "Category 'Beverages' should be present");
        assertEquals(description1, category1.getDescription(), "Descriptions should match");

        CategoryDTO category2 = categories.stream()
                .filter(c -> c.getName().equals("Snacks"))
                .findFirst()
                .orElse(null);
        assertNotNull(category2, "Category 'Snacks' should be present");
        assertEquals(description2, category2.getDescription(), "Descriptions should match");
    }

    /**
     * Tests retrieving a category by its ID via the facade.
     */
    @Test
    void it_should_return_category_by_id_via_facade() {
        // Given
        categoryFacade.createCategory(1L, "Beverages", "Drinks and beverages");
        CategoryDTO createdCategory = categoryFacade.getAllCategories().get(0);
        Long categoryId = createdCategory.getId();

        // When
        CategoryDTO foundCategory = categoryFacade.getCategoryById(categoryId);

        // Then
        assertNotNull(foundCategory, "Found category should not be null");
        assertEquals("Beverages", foundCategory.getName(), "Category names should match");
        assertEquals("Drinks and beverages", foundCategory.getDescription(), "Descriptions should match");
    }

    /**
     * Tests checking if a category exists via the facade.
     */
    @Test
    void it_should_check_if_category_exists_via_facade() {
        // Given
        categoryFacade.createCategory(1L,"Snacks", "All types of snacks");
        CategoryDTO createdCategory = categoryFacade.getAllCategories().get(0);
        Long categoryId = createdCategory.getId();

        // When
        boolean exists = categoryFacade.categoryExists(categoryId);

        // Then
        assertTrue(exists, "Category should exist");
    }

    /**
     * Tests that 'categoryExists' returns false for a non-existent category ID.
     */
    @Test
    void it_should_return_false_for_nonexistent_category() {
        // Given
        Long nonExistentId = 999L;

        // When
        boolean exists = categoryFacade.categoryExists(nonExistentId);

        // Then
        assertFalse(exists, "Category should not exist");
    }

    /**
     * Tests behavior when retrieving a category that does not exist.
     */
    @Test
    void it_should_return_null_when_category_not_found_by_id() {
        // Given
        Long nonExistentId = 999L;

        // When
        CategoryDTO categoryDTO = categoryFacade.getCategoryById(nonExistentId);

        // Then
        assertNull(categoryDTO, "CategoryDTO should be null for non-existent ID");
    }
}
