package ru.practicum.mainsvc.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.event.service.EventService;
import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventController {
    private final EventService eventService;
    private final HttpServletRequest request;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        log.info("Request endpoint: 'GET /events' (получение списка всех событий)");
        String ip = request.getRemoteAddr();
        return eventService.getEvents(ip, text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable Long id) {
        log.info("Request endpoint: 'GET /events/{} (Получение события по id)", id);
        String ip = request.getRemoteAddr();
        return eventService.getEvent(id, ip);
    }
}
