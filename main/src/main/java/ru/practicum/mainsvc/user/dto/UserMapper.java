package ru.practicum.mainsvc.user.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.user.dto.NewUserDto;
import ru.practicum.mainsvc.user.dto.UserDto;
import ru.practicum.mainsvc.user.dto.UserShortDto;
import ru.practicum.mainsvc.user.model.User;

@Service
@RequiredArgsConstructor
public class UserMapper {
    public UserShortDto mapToUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()
        );
    }

    public User mapToUser(NewUserDto userDto) {
        return new User(
                null,
                userDto.getEmail(),
                userDto.getName()
        );
    }
}
