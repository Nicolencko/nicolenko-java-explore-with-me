package ru.practicum.mainsvc.user.mapper;

import org.mapstruct.*;
import ru.practicum.mainsvc.user.dto.UserDto;
import ru.practicum.mainsvc.user.dto.UserShortDto;
import ru.practicum.mainsvc.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToDto(User user);

    User userFromDto(UserDto userDto);

    UserShortDto userToShortDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);
}
