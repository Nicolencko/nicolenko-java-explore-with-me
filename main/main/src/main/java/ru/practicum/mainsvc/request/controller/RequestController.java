package ru.practicum.mainsvc.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.request.service.RequestService;
import ru.practicum.mainsvc.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getRequestsByUserId(
            @PathVariable Long userId
    ) {
        log.info("Request endpoint: 'GET /users/{}/requests' (Получение заявок пользователя)", userId);
        return requestService.getRequestsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto saveRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        log.info("Request endpoint: 'POST /users/{}/requests' (Создание нового запроса на участие)", userId);
        return requestService.saveRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        log.info("Request endpoint: 'PATCH /users/{}/requests/{}/cancel' " +
                "(Отмена запроса на участие)", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}
