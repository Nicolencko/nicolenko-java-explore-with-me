package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ViewStats;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT new ru.practicum.ViewStats(h.app, h.uri, count(h.uri)) " + "FROM Hit as h " + "WHERE h.timestamp >= :start AND h.timestamp <= :end " + "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsOnlyDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.ViewStats(h.app, h.uri, count(DISTINCT h.uri)) " + "FROM Hit as h " + "WHERE h.timestamp >= :start AND h.timestamp <= :end  " + "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsUniqueUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.ViewStats(h.app, h.uri, count(h.uri)) " + "FROM Hit as h " + "WHERE h.timestamp >= :start AND h.timestamp <= :end " + "AND h.uri IN :uris " + "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsUrisList(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.ViewStats(h.app, h.uri, count(DISTINCT h.uri)) " + "FROM Hit as h " + "WHERE h.timestamp >= :start AND h.timestamp <= :end " + "AND h.uri IN :uris " + "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsUniqueUrisList(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);
}
