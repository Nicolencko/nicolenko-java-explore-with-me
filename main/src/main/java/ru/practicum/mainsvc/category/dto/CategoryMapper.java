package ru.practicum.mainsvc.category.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.dto.NewCategoryDto;
import ru.practicum.mainsvc.category.model.Category;

@Service
@RequiredArgsConstructor
public class CategoryMapper {
    public CategoryDto mapCategoryToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public Category mapNewToCategory(NewCategoryDto dto) {
        return new Category(
                null,
                dto.getName()
        );
    }
}
