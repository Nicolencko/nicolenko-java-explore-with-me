package ru.practicum.mainsvc.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.mainsvc.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(nativeQuery = true, value = "SELECT * FROM events WHERE id IN (:idList)")
    List<Event> getEventsListByIdList(List<Long> idList);

    @Query(nativeQuery = true, value = "SELECT id FROM events")
    List<Integer> getAllIds();

    @Query(nativeQuery = true, value = "SELECT * FROM events WHERE events.categoryId = :catId")
    boolean existsAllByCategory(Long catId);
}
