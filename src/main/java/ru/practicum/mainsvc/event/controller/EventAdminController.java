package ru.practicum.mainsvc.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainsvc.event.service.EventService;
import ru.practicum.mainsvc.utils.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> userIds,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        log.info("Request endpoint: 'GET /events' (получение списка всех событий админом)");
        return eventService.getEvents(userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(
            @PathVariable Long eventId,
            @Validated({Update.class}) @RequestBody UpdateEventAdminRequest updatedEvent) {
        log.info("Request endpoint: 'PUT /admin/events/{}' (Редактирование события)", eventId);
        return eventService.updateEvent(eventId, updatedEvent);
    }
}
