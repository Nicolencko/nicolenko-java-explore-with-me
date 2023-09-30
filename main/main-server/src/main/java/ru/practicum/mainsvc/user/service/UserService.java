package ru.practicum.mainsvc.user.service;

import ru.practicum.mainsvc.user.dto.UserDto;
import ru.practicum.mainsvc.user.model.User;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);

    List<UserDto> getUsers(Long[] ids, Integer from, Integer size);

    void deleteUser(Long userId);

    User getUser(Long userId);
}
