package ru.practicum.explorewithme.API.adminAPI;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dtos.category.CategoryDto;
import ru.practicum.explorewithme.dtos.category.NewCategoryDto;
import ru.practicum.explorewithme.mappers.CategoryMapper;
import ru.practicum.explorewithme.services.category.CategoryService;

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
