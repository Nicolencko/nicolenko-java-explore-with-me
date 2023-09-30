package ru.practicum.mainsvc.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainsvc.compilation.model.Compilation;
import ru.practicum.mainsvc.util.Pagination;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("SELECT c FROM Compilation c WHERE (:pinned is null or c.pinned = :pinned)")
    List<Compilation> findAllByPinned(Boolean pinned, Pagination page);
}
