package com.retailtech.quickcasheasy.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CategoryFacadeTest {

    private CategoryFacade categoryFacade;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        categoryFacade = new CategoryFacade(categoryService);
    }

    @Test
    void it_should_return_all_categories_via_facade() {
        // Given
        Category category1 = new Category(1L, "Beverages", "Drinks and beverages");
        Category category2 = new Category(2L, "Snacks", "All types of snacks");
        when(categoryService.getAllCategories()).thenReturn(List.of(category1, category2));

        // When
        List<Category> categories = categoryFacade.getAllCategories();

        // Then
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals("Beverages", categories.get(0).getName());

    }

    @Test
    void it_should_return_category_by_id_via_facade() {
        // Given
        Category category = new Category(1L, "Beverages", "Drinks and beverages");
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        // When
        Category foundCategory = categoryFacade.getCategoryById(1L);

        // Then
        assertNotNull(foundCategory);
        assertEquals(category, foundCategory);
    }

    @Test
    void it_should_create_category_via_facade() {
        // Given
        String name = "Beverages";
        String description = "Drinks and beverages";

        // When
        categoryFacade.createCategory(name, description);

        // Then
        verify(categoryService, times(1)).addCategory(any(Category.class));
    }

    @Test
    void it_should_delete_category_via_facade() {
        // Given
        Long categoryId = 1L;

        // When
        categoryFacade.deleteCategory(categoryId);

        // Then
        verify(categoryService, times(1)).deleteCategory(categoryId);
    }

}
