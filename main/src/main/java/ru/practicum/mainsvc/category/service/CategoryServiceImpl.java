package ru.practicum.mainsvc.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.mainsvc.category.dto.CategoryMapper;
import ru.practicum.mainsvc.category.repository.CategoryRepository;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.dto.NewCategoryDto;
import ru.practicum.mainsvc.category.model.Category;
import ru.practicum.mainsvc.event.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        return StreamSupport.stream(categories.spliterator(), false)
                .map(categoryMapper::mapCategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такой категории");
        }
        Category category = categoryRepository.getReferenceById(id);
        return categoryMapper.mapCategoryToDto(category);
    }

    @Override
    public CategoryDto patchCategory(Long catId, NewCategoryDto categoryDto) {
        if (categoryRepository.existsCategoryByName(categoryDto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Имя уже существует");
        }
        Category category = categoryRepository.getReferenceById(catId);
        category.setName(categoryDto.getName());
        Category result = categoryRepository.save(category);
        return categoryMapper.mapCategoryToDto(result);
    }

    @Override
    public CategoryDto saveCategory(NewCategoryDto categoryDto) {
        if (categoryDto.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное тело запроса");
        }
        if (categoryRepository.existsCategoryByName(categoryDto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Имя уже существует");
        }
        Category category = categoryMapper.mapNewToCategory(categoryDto);
        categoryRepository.save(category);
        return categoryMapper.mapCategoryToDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        if (eventRepository.existsAllByCategory(catId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Нельзя удалить категорию со связанными событиями");
        }
        categoryRepository.deleteById(catId);
    }
}

