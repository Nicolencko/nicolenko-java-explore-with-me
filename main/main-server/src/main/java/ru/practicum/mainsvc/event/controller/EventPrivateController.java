package ru.practicum.mainsvc.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.event.dto.*;
import ru.practicum.mainsvc.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByUserId(@PathVariable Long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Request endpoint: 'GET /users/{}/events' (Получение списка всех событий пользователя)", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Long userId,
                                  @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Request endpoint: 'POST /users/{}/events' (Создание события пользователем)", userId);
        return eventService.saveEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByUserIdAndEventId(@PathVariable Long userId,
                                                   @PathVariable Long eventId) {
        log.info("Request endpoint: 'GET /users/{}/events/{}' (Получение события)", userId, eventId);
        return eventService.getEventByUserIdAndEventId(userId, eventId);
    }


    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Request endpoint: 'PATCH /users/{}/events/{}' (Изменение события)", userId, eventId);
        return eventService.updateEventByUser(userId, eventId, updateEventUserRequest);
    }

    @PostMapping("/{eventId}/rate")
    @ResponseStatus(HttpStatus.CREATED)
    public EventRatingDto addLike(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @RequestParam(required = true) Boolean isLike) {
        log.info("Add like or dislike to event={}, by user={}", eventId, userId);
        return eventService.addLike(userId, eventId, isLike);
    }

    @DeleteMapping("/{eventId}/rate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long userId,
                           @PathVariable Long eventId) {
        log.info("Delete like or dislike from event={}, by user={}", eventId, userId);
        eventService.deleteLike(userId, eventId);
    }

}
