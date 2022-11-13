package ru.practicum.explorewithme.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        log.info("Has received request to endpoint POST/admin/categories");
        return CategoryMapper.toCategoryDto(categoryService.createCategory(newCategoryDto));
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Has received request to endpoint PATCH/admin/categories");
        return CategoryMapper.toCategoryDto(categoryService.updateCategory(categoryDto));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Has received request to endpoint GET/admin/categories/{}", catId);
        return CategoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Has received request to endpoint DELETE/admin/categories/{}", catId);
        categoryService.deleteCategory(catId);
    }
}
