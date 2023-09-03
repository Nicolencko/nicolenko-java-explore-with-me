package ru.practicum.mainsvc.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.dto.CategoryMapper;
import ru.practicum.mainsvc.category.dto.NewCategoryDto;
import ru.practicum.mainsvc.category.model.Category;
import ru.practicum.mainsvc.category.repository.CategoryRepository;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.event.repository.EventRepository;
import ru.practicum.mainsvc.exception.ConflictException;
import ru.practicum.mainsvc.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::mapCategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + id + " не найдена"));

        return categoryMapper.mapCategoryToDto(category);
    }

    @Override
    public CategoryDto patchCategory(Long catId, NewCategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найдена"));
        if (categoryRepository.existsCategoryByName(categoryDto.getName())) {
            throw new ConflictException("Имя уже существует");
        }
        if (!categoryDto.getName().isEmpty())
            category.setName(categoryDto.getName());
        try {
            category = categoryRepository.saveAndFlush(category);
            return categoryMapper.mapCategoryToDto(category);
        } catch (RuntimeException e) {
            throw new ConflictException(e.getMessage());
        }

    }

    @Override
    public CategoryDto saveCategory(NewCategoryDto categoryDto) {
        try {
            if (categoryRepository.existsCategoryByName(categoryDto.getName()))
                throw new ConflictException("Имя категории уже существует");
            Category category = categoryMapper.mapNewToCategory(categoryDto);
            categoryRepository.save(category);
            return categoryMapper.mapCategoryToDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Имя категории уже существует");
        }
    }

    @Override
    public void deleteCategory(Long catId) {
        List<Event> events = eventRepository.findAllByCategoryId(catId);

        if (events.isEmpty()) {
            categoryRepository.deleteById(catId);
        } else {
            throw new ConflictException("Категория не пустая");
        }
    }
}

