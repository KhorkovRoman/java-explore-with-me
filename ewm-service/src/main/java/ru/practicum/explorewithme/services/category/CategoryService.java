package ru.practicum.explorewithme.services.category;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.dtos.category.CategoryDto;
import ru.practicum.explorewithme.dtos.category.NewCategoryDto;
import ru.practicum.explorewithme.models.category.Category;

import java.util.Collection;

public interface CategoryService {

    Category createCategory(NewCategoryDto newCategoryDto);

    Category updateCategory(CategoryDto categoryDto);

    Collection<Category> getAllCategories(PageRequest pageRequest);

    Category getCategoryById(Long catId);

    void deleteCategory(Long catId);
}
