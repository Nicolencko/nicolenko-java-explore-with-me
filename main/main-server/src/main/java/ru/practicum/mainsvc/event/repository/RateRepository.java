package ru.practicum.mainsvc.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainsvc.event.model.EventRatingView;
import ru.practicum.mainsvc.event.model.EventUserRating;
import ru.practicum.mainsvc.user.model.InitiatorRateView;
import ru.practicum.mainsvc.util.Pagination;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<EventUserRating, Long> {

    @Query(value = "SELECT event.id as eventId, event.title as title, SUM(e.rating) AS rating " +
            "FROM EventUserRating e " +
            "JOIN e.event event " +
            "WHERE e.event.id = :eventId " +
            "GROUP BY event.id " +
            "ORDER BY rating DESC")
    Optional<EventRatingView> getEventRateView(Long eventId);

    @Query(value = "SELECT event.id as eventId, event.title as title, SUM(e.rating) AS rating " +
            "FROM EventUserRating e " +
            "JOIN e.event event " +
            "GROUP BY eventId " +
            "ORDER BY rating DESC")
    List<EventRatingView> getAllEventsRateViews(Pagination page);

    @Query(value = "SELECT event.initiator.id as initiatorId, event.initiator.name as name, SUM(e.rating) AS rating " +
            "FROM EventUserRating e " +
            "JOIN e.event event " +
            "GROUP BY initiatorId, name " +
            "ORDER BY rating DESC")
    List<InitiatorRateView> getAllUsersRateViews(Pagination page);

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

}
