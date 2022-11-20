package ru.practicum.explorewithme.categories.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.categories.dto.CategoryDto;
import ru.practicum.explorewithme.categories.dto.NewCategoryDto;
import ru.practicum.explorewithme.categories.mapper.CategoryMapper;
import ru.practicum.explorewithme.categories.model.Category;
import ru.practicum.explorewithme.categories.repository.CategoryRepository;
import ru.practicum.explorewithme.exeption.BadRequestException;
import ru.practicum.explorewithme.exeption.NotFoundException;

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
        validateCategory(category);
        log.info("Category id " + category.getId() + " has successfully created.");
        return category;
    }

    @Override
    public Category updateCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        validateCategory(category);
//        if (category.getName() == null) {
//            category.setName(getCategoryById(category.getId()).getName());
//        }
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
                .orElseThrow(() -> new NotFoundException("In DB has not found category id " + catId));
        log.info("Category id " + catId + " has found in DB.");
        return category;
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("In DB has not found category id " + catId));
        log.info("Category id " + catId + " has found in DB.");
        categoryRepository.deleteById(catId);
    }

    public void validateCategory(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new BadRequestException("Name could not be empty.");
        }
        if (categoryRepository.findAll().contains(category.getName())) {
            throw new BadRequestException("Category " + category.getName() + " has already found in DB .");
        }
    }
}
