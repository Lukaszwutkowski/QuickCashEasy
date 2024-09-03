package com.retailtech.quickcasheasy.category;

import java.util.List;

public class CategoryFacade {

    private final CategoryService categoryService;

    public CategoryFacade(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public Category getCategoryById(Long id) {
        return categoryService.getCategoryById(id);
    }

    public void createCategory(String name, String description) {
        Category category = new Category(null, name, description);
        categoryService.addCategory(category);
    }

    public void deleteCategory(Long id) {
        categoryService.deleteCategory(id);
    }

}
