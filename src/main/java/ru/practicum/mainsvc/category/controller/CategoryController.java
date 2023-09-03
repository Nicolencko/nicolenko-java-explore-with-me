package ru.practicum.mainsvc.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.service.CategoryService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        log.info("Request endpoint: 'GET /categories' (получение списка всех категорий)");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        log.info("Request endpoint: 'GET /categories/{} (Получение категории по catId)", catId);
        return categoryService.getCategory(catId);
    }
}
