package ru.practicum.explorewithme.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.categories.dto.CategoryDto;
import ru.practicum.explorewithme.categories.mapper.CategoryMapper;
import ru.practicum.explorewithme.categories.service.CategoryService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
public class CategoryControllerPublic {

    private final CategoryService categoryService;

    public CategoryControllerPublic(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Collection<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен Get запрос к эндпоинту GET/categories?from={}size={}", from, size);
        final PageRequest pageRequest = findPageRequest(from, size);
        return CategoryMapper.toCategoryDtoCollection(categoryService.getAllCategories(pageRequest));
    }

    @GetMapping("/{catId}")
    public CategoryDto getUserById(@PathVariable Long catId) {
        log.info("Получен Get запрос к эндпоинту GET/categories/{}", catId);
        return CategoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
    }

    public PageRequest findPageRequest(Integer from, Integer size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }
}
