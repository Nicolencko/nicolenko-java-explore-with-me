package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStats;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT new ru.practicum.ViewStats(h.app, h.uri, count(h.uri)) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= ?1 AND h.timestamp <= ?2 " +
            "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsOnlyDates(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.ViewStats(h.app, h.uri, count(DISTINCT h.uri)) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= ?1 AND h.timestamp <= ?2 " +
            "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsUniqueUris(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.ViewStats(h.app, h.uri, count(h.uri)) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= ?1 AND h.timestamp <= ?2 " +
            "AND h.uri IN ?3 " +
            "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsUrisList(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.ViewStats(h.app, h.uri, count(DISTINCT h.uri)) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= ?1 AND h.timestamp <= ?2 " +
            "AND h.uri IN ?3" +
            "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsUniqueUrisList(LocalDateTime start, LocalDateTime end, List<String> uris);

}
