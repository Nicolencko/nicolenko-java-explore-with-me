package ru.practicum.mainsvc.category.service;

import ru.practicum.mainsvc.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    void deleteCategory(Long catId);

    CategoryDto saveCategory(CategoryDto categoryDto);

    CategoryDto patchCategory(Long catId, CategoryDto categoryDto);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);
}
