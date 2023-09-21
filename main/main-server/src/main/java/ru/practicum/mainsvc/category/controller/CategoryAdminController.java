package ru.practicum.mainsvc.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Request endpoint: 'POST /admin/categories' (Создание категории)");
        return categoryService.saveCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Request endpoint: 'DELETE /admin/categories/{}' (Удаление категории)", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto patchCategory(@PathVariable Long catId,
                                     @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Request endpoint: 'PATCH /admin/categories' (Изменение категории)");
        return categoryService.patchCategory(catId, categoryDto);
    }
}
