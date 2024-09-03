package com.retailtech.quickcasheasy.category;

import java.util.List;

class CategoryService {

    private final CategoryRepository categoryRepository;

    public  CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository  = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.getCategories();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found for id: " + id));
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.delete(id);
    }
}
