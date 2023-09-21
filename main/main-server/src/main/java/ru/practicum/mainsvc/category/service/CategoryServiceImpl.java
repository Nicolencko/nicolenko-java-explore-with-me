package ru.practicum.mainsvc.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.mapper.CategoryMapper;
import ru.practicum.mainsvc.category.model.Category;
import ru.practicum.mainsvc.category.repository.CategoryRepository;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.event.repository.EventRepository;
import ru.practicum.mainsvc.exception.ConflictException;
import ru.practicum.mainsvc.exception.NotFoundException;
import ru.practicum.mainsvc.util.Pagination;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        try {
            Category category = categoryMapper.categoryFromDto(categoryDto);
            Category categorySaved = categoryRepository.save(category);
            return categoryMapper.categoryToDto(categorySaved);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Категория с указанным названием уже существует.");
        }
    }

    @Override
    @Transactional
    public CategoryDto patchCategory(Long catId, CategoryDto categoryDto) {
        Category category = getOrThrow(catId);
        categoryMapper.updateCategoryFromDto(categoryDto, category);

        try {
            category = categoryRepository.saveAndFlush(category);
        } catch (RuntimeException e) {
            throw new ConflictException(e.getMessage());
        }

        return categoryMapper.categoryToDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        getOrThrow(catId);

        List<Event> events = eventRepository.findAllByCategoryId(catId);

        if (events.isEmpty()) {
            categoryRepository.deleteById(catId);
        } else {
            throw new ConflictException("Категория не пуста");
        }
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pagination page = new Pagination(from, size);

        return categoryRepository.findAll(page)
                .stream()
                .map(categoryMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        return categoryMapper.categoryToDto(getOrThrow(catId));
    }

    private Category getOrThrow(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }
}
