package ru.practicum.mainsvc.compilations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainsvc.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM compilations WHERE pinned = :pinned")
    Page<Compilation> getCompilations(Boolean pinned, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM compilations WHERE title = :title")
    Compilation getCompilationByTitle(String title);
}
