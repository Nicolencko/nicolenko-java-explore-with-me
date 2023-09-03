package ru.practicum.mainsvc.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.user.service.UserService;
import ru.practicum.mainsvc.user.dto.NewUserDto;
import ru.practicum.mainsvc.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        log.info("Request endpoint: 'GET /admin/users' (Получение информации о пользователях)");
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto saveUser(
            @RequestBody @Valid NewUserDto newUserDto
    ) {
        log.info("Request endpoint: 'POST /admin/users' (Добавление пользователя)");
        return userService.saveUser(newUserDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Request endpoint: 'DELETE /admin/users/{}' (Удаление пользователя)", userId);
        userService.deleteUser(userId);
    }
}
