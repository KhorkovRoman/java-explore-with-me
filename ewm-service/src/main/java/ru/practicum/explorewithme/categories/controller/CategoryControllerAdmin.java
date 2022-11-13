package ru.practicum.explorewithme.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.categories.dto.CategoryDto;
import ru.practicum.explorewithme.categories.dto.NewCategoryDto;
import ru.practicum.explorewithme.categories.mapper.CategoryMapper;
import ru.practicum.explorewithme.categories.service.CategoryService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @Autowired
    public CategoryControllerAdmin(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос к эндпоинту POST/admin/categories");
        return CategoryMapper.toCategoryDto(categoryService.createCategory(newCategoryDto));
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос к эндпоинту PATCH/admin/categories");
        return CategoryMapper.toCategoryDto(categoryService.updateCategory(categoryDto));
    }

    @GetMapping("/{catId}")
    public CategoryDto getUserById(@PathVariable Long catId) {
        return CategoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    public PageRequest findPageRequest(Integer from, Integer size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }

}
