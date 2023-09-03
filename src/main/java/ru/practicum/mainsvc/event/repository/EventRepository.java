package ru.practicum.mainsvc.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainsvc.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(nativeQuery = true, value = "SELECT * FROM events WHERE id IN (:idList)")
    List<Event> getEventsListByIdList(List<Long> idList);

    @Query(nativeQuery = true, value = "SELECT id FROM events")
    List<Integer> getAllIds();

    List<Event> findAllByCategoryId(Long categoryId);

    @Query(value = "SELECT e " +
            "FROM Event AS e " +
            "WHERE (:users IS NULL OR e.initiator IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categoriesIds IS NULL OR e.categoryId IN :categoriesIds) " +
            "OR (CAST(:rangeStart AS date) IS NULL AND CAST(:rangeStart AS date) IS NULL)" +
            "OR (CAST(:rangeStart AS date) IS NULL AND e.eventDate < CAST(:rangeEnd AS date)) " +
            "OR (CAST(:rangeEnd AS date) IS NULL AND e.eventDate > CAST(:rangeStart AS date)) " +
            "GROUP BY e.id " +
            "ORDER BY e.id ASC")
    List<Event> findAllByParamsAdminUsingCategories(@Param("users") List<Long> users,
                                                    @Param("states") List<String> states,
                                                    @Param("categories") List<Long> categoriesIds,
                                                    @Param("rangeStart") LocalDateTime rangeStart,
                                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                                    Pageable page);

    @Query(value = "SELECT e " +
            "FROM Event AS e " +
            "WHERE (:users IS NULL OR e.initiator IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "OR (CAST(:rangeStart AS date) IS NULL AND CAST(:rangeStart AS date) IS NULL)" +
            "OR (CAST(:rangeStart AS date) IS NULL AND e.eventDate < CAST(:rangeEnd AS date)) " +
            "OR (CAST(:rangeEnd AS date) IS NULL AND e.eventDate > CAST(:rangeStart AS date)) " +
            "GROUP BY e.id " +
            "ORDER BY e.id ASC")
    List<Event> findAllByParamsAdmin(@Param("users") List<Long> users,
                                     @Param("states") List<String> states,
                                     @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd,
                                     Pageable page);

}
