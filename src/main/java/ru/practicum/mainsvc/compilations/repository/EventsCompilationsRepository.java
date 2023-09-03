package ru.practicum.mainsvc.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainsvc.compilations.model.EventsCompilations;
import ru.practicum.mainsvc.compilations.model.EventsCompilationsId;

import java.util.List;

public interface EventsCompilationsRepository extends JpaRepository<EventsCompilations, EventsCompilationsId> {
    @Query(nativeQuery = true, value = "SELECT * FROM events_compilations WHERE compilation_id IN (:compilationIds)")
    List<EventsCompilations> getCompilationsByIds(List<Long> compilationIds);

    List<EventsCompilations> getAllByCompilationId(Long compilationId);
}
