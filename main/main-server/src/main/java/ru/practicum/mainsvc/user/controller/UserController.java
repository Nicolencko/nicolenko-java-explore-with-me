package ru.practicum.mainsvc.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.user.model.InitiatorRateView;
import ru.practicum.mainsvc.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/ratings")
    @ResponseStatus(HttpStatus.OK)
    public List<InitiatorRateView> getUsersRatings(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Request endpoint: 'GET /users/ratings (Получение событий с рейтингом)");
        return userService.getUsersRatings(from, size);
    }
}
