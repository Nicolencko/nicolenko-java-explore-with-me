package ru.practicum.mainsvc.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainsvc.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsCategoryByName(String name);
}
