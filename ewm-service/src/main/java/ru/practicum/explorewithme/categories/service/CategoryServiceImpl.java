package ru.practicum.explorewithme.categories.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.categories.dto.CategoryDto;
import ru.practicum.explorewithme.categories.dto.NewCategoryDto;
import ru.practicum.explorewithme.categories.mapper.CategoryMapper;
import ru.practicum.explorewithme.categories.model.Category;
import ru.practicum.explorewithme.categories.repository.CategoryRepository;
import ru.practicum.explorewithme.exeption.ValidationException;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
        log.info("Category id " + category.getId() + " has successfully created.");
        return category;
    }

    @Override
    public Category updateCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);

        log.info("categoryDto.getId() =" + categoryDto.getId());

        if (category.getName() == null) {
            category.setName(getCategoryById(category.getId()).getName());
        }

        log.info("Category id " + category.getId() + " has successfully updated.");
        return categoryRepository.save(category);
    }

    @Override
    public Collection<Category> getAllCategories(PageRequest pageRequest) {
        return categoryRepository.getAllCategoriesByPage(pageRequest).stream()
                .collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no category id " + catId));
        log.info("Category id " + catId + " has found in DB.");
        return category;
    }

    @Override
    public void deleteCategory(Long catId) {
        validateCategory(catId);
        log.info("Category id " + catId + " has found in DB.");
        categoryRepository.deleteById(catId);
    }

    public void validateCategory(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no category id " + catId));
    }
}
