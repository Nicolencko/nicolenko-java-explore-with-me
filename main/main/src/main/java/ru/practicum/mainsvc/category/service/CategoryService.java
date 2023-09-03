package ru.practicum.mainsvc.category.service;

import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long id);

    CategoryDto patchCategory(Long catId, NewCategoryDto categoryDto);

    CategoryDto saveCategory(NewCategoryDto categoryDto);

    void deleteCategory(Long catId);
}
