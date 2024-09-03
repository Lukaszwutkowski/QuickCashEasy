package com.retailtech.quickcasheasy.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryService categoryService;
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void it_should_return_all_categories() {
        // Given
        Category category1 = new Category(1L, "Beverages", "Drinks and beverages");
        Category category2 = new Category(2L, "Snacks", "All types of snacks");
        when(categoryRepository.getCategories()).thenReturn(List.of(category1, category2));

        // When
        List<Category> categories = categoryService.getAllCategories();

        // Then
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals("Beverages", categories.get(0).getName());

    }

    @Test
    void it_should_return_the_category_by_id() {
        // Given
        Category category = new Category(1L, "Beverages", "Drinks and beverages");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // When
        Category foundCategory = categoryService.getCategoryById(1L);

        // Then
        assertNotNull(foundCategory);
        assertEquals(category, foundCategory);
    }

    @Test
    void it_should_throw_an_exception_if_category_is_not_found() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals("Category not found for id: 1", exception.getMessage());
    }

    @Test
    void it_should_add_new_category() {
        // Given
        Category category = new Category(1L, "Beverages", "Drinks and beverages");

        // When
        categoryService.addCategory(category);

        // Then
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void it_should_delete_category() {
        // Given
        Long categoryId = 1L;

        // When
        categoryService.deleteCategory(categoryId);

        // Then
        verify(categoryRepository, times(1)).delete(categoryId);
    }

}
