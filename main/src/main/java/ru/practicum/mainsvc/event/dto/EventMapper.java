package ru.practicum.mainsvc.event.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.category.service.CategoryService;
import ru.practicum.mainsvc.category.model.Category;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.user.service.UserService;
import ru.practicum.mainsvc.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryService categoryService;
    private final UserService userService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]");
    private final DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public EventFullDto mapToFullDto(Event event, Category category, User user) {
        return new EventFullDto(
                event.getAnnotation(),
                new EventFullDto.CategoryDto(category.getId(), category.getName()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate().format(outFormatter),
                event.getId(),
                new EventFullDto.UserShortDto(user.getId(), user.getName()),
                new EventFullDto.Location(event.getLocationLat(), event.getLocationLon()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public EventFullDto mapToFullDto(Event event) {
        return new EventFullDto(
                event.getAnnotation(),
                new EventFullDto.CategoryDto(
                        categoryService.getCategory(event.getCategory()).getId(),
                        categoryService.getCategory(event.getCategory()).getName()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate().format(outFormatter),
                event.getId(),
                new EventFullDto.UserShortDto(
                        userService.getUser(event.getInitiator()).getId(),
                        userService.getUser(event.getInitiator()).getName()),
                new EventFullDto.Location(event.getLocationLat(), event.getLocationLon()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public EventShortDto mapToShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                new EventFullDto.CategoryDto(
                        categoryService.getCategory(event.getCategory()).getId(),
                        categoryService.getCategory(event.getCategory()).getName()),
                event.getConfirmedRequests(),
                event.getEventDate().format(outFormatter),
                event.getId(),
                new EventFullDto.UserShortDto(
                        userService.getUser(event.getInitiator()).getId(),
                        userService.getUser(event.getInitiator()).getName()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public Event mapNewToEvent(NewEventDto newEventDto, Long userId) {
        return new Event(
                null,
                newEventDto.getAnnotation(),
                newEventDto.getCategory(),
                0,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                parseLocalDateTime(newEventDto.getEventDate(), formatter),
                userId,
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon(),
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration(),
                "PENDING",
                newEventDto.getTitle(),
                0L
        );
    }

    private static LocalDateTime parseLocalDateTime(CharSequence text, DateTimeFormatter formatter) {
        if (text == null) {
            return null;
        }
        return formatter.parse(text, LocalDateTime::from);
    }
}
