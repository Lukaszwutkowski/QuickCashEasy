package com.retailtech.quickcasheasy.category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<Category> getCategories();

    Optional<Category> findById(Long id);

    void save(Category category);

    void delete(Long id);
}
