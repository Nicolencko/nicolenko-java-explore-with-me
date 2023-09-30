package ru.practicum.mainsvc.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.EventShortDto;
import ru.practicum.mainsvc.event.model.EventRatingView;
import ru.practicum.mainsvc.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mainsvc.util.Constants.FORMATTER;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        log.info("Request endpoint: 'GET /events' (получение списка всех событий)");
        LocalDateTime start = (rangeStart == null) ? null : LocalDateTime.parse(rangeStart, FORMATTER);
        LocalDateTime end = (rangeEnd == null) ? null : LocalDateTime.parse(rangeEnd, FORMATTER);
        return eventService.getEvents(text, categories, paid, start, end, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Request endpoint: 'GET /events/{} (Получение события по eventId)", eventId);
        return eventService.getEvent(eventId, request);
    }

    @GetMapping("/ratings")
    @ResponseStatus(HttpStatus.OK)
    public List<EventRatingView> getEventsRatings(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Request endpoint: 'GET /events/ratings (Получение событий с рейтингом)");
        return eventService.getEventsRatings(from, size);
    }


}
