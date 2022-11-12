package ru.practicum.explorewithme.categories.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.categories.dto.CategoryDto;
import ru.practicum.explorewithme.categories.dto.NewCategoryDto;
import ru.practicum.explorewithme.categories.model.Category;

import java.util.Collection;

public interface CategoryService {

    Category createCategory(NewCategoryDto newCategoryDto);

    Category updateCategory(CategoryDto categoryDto);

    Collection<Category> getAllCategories(PageRequest pageRequest);
    Category getCategoryById(Long catId);

    void deleteCategory(Long catId);
}
