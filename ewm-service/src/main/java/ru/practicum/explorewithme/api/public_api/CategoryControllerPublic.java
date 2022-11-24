package ru.practicum.explorewithme.api.public_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dtos.category.CategoryDto;
import ru.practicum.explorewithme.mappers.CategoryMapper;
import ru.practicum.explorewithme.services.category.CategoryService;
import ru.practicum.explorewithme.common.ValidationPageParam;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
public class CategoryControllerPublic {

    private final CategoryService categoryService;

    @Autowired
    public CategoryControllerPublic(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Collection<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        validatePage(from, size);
        log.info("Has received request to endpoint GET/categories?from={}size={}", from, size);
        final PageRequest pageRequest = findPageRequest(from, size);
        return CategoryMapper.toCategoryDtoCollection(categoryService.getAllCategories(pageRequest));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Has received request to endpoint GET/categories/{}", catId);
        return CategoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
    }

    public PageRequest findPageRequest(Integer from, Integer size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }

    private void validatePage(Integer from, Integer size) {
        ValidationPageParam validationPageParam = new ValidationPageParam(from, size);
        validationPageParam.validatePageParam();
    }
}
