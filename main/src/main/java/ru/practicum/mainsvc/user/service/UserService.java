package ru.practicum.mainsvc.user.service;

import ru.practicum.mainsvc.user.dto.NewUserDto;
import ru.practicum.mainsvc.user.dto.UserDto;
import ru.practicum.mainsvc.user.dto.UserShortDto;

import java.util.List;

public interface UserService {
    UserShortDto getUser(Long id);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto saveUser(NewUserDto newUserDto);

    void deleteUser(Long userId);
}
