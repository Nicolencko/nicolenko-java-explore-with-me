package ru.practicum.mainsvc.event.service;

import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.EventShortDto;
import ru.practicum.mainsvc.event.dto.NewEventDto;
import ru.practicum.mainsvc.event.dto.UpdateEventUserRequest;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.event.model.EventRatingView;
import ru.practicum.mainsvc.event.model.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EventService {
    EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    Set<EventShortDto> getAllShortDto(Set<Long> ids);

    Set<Event> getAll(Set<Long> ids);

    EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    Event getOrThrow(Long eventId);

    Map<String, Long> getViewsFromStatServer(List<Event> events);

    void increaseConfirmedRequests(Event event);

    void decreaseConfirmedRequests(Event event);

    void updateEvent(Event event);

    List<EventShortDto> getEvents(String text,
                                  List<Long> categories,
                                  Boolean paid,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  Boolean onlyAvailable,
                                  String sort,
                                  Integer from,
                                  Integer size,
                                  HttpServletRequest request);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);

    List<EventFullDto> getEvents(List<Long> users,
                                 List<State> states,
                                 List<Long> categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 Integer from,
                                 Integer size);

    EventFullDto updateEvent(Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventRatingView addLike(Long userId, Long eventId, Boolean isLike);

    void deleteLike(Long userId, Long eventId);

    List<EventRatingView> getEventsRatings(Integer from, Integer size);
}
