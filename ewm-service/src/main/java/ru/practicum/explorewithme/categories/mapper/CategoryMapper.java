package ru.practicum.explorewithme.categories.mapper;

import lombok.Data;
import ru.practicum.explorewithme.categories.dto.CategoryDto;
import ru.practicum.explorewithme.categories.dto.NewCategoryDto;
import ru.practicum.explorewithme.categories.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryMapper {

    public static List<CategoryDto> toCategoryDtoCollection(Collection<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }
}
