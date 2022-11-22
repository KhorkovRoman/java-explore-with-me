package ru.practicum.explorewithme.services.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dtos.category.CategoryDto;
import ru.practicum.explorewithme.dtos.category.NewCategoryDto;
import ru.practicum.explorewithme.mappers.CategoryMapper;
import ru.practicum.explorewithme.models.category.Category;
import ru.practicum.explorewithme.repositories.CategoryRepository;
import ru.practicum.explorewithme.exeptions.BadRequestException;
import ru.practicum.explorewithme.exeptions.NotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String NOT_FOUND_CATEGORY = "In DB has not found category id ";
    private static final String OBJECT_EMPTY = "Object can't be empty. Need the object: ";

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(NewCategoryDto newCategoryDto) {
        if (newCategoryDto == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "New Category.");
        }
        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
        validateCategory(category);
        log.info("Category id " + category.getId() + " has successfully created.");
        return category;
    }

    @Override
    public Category updateCategory(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "Category.");
        }
        Category category = CategoryMapper.toCategory(categoryDto);
        validateCategory(category);
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
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY + catId));
        log.info("Category id " + catId + " has found in DB.");
        return category;
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY + catId));
        log.info("Category id " + catId + " has found in DB.");
        categoryRepository.deleteById(catId);
    }

    public void validateCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "Category.");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            throw new BadRequestException("Name could not be empty.");
        }
        if (categoryRepository.findAll().contains(category.getName())) {
            throw new BadRequestException("Category " + category.getName() + " has already found in DB .");
        }
    }
}
