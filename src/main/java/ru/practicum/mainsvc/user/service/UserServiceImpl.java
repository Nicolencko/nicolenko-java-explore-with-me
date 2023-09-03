package ru.practicum.mainsvc.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.exception.ConflictException;
import ru.practicum.mainsvc.user.dto.NewUserDto;
import ru.practicum.mainsvc.user.dto.UserDto;
import ru.practicum.mainsvc.user.dto.UserMapper;
import ru.practicum.mainsvc.user.dto.UserShortDto;
import ru.practicum.mainsvc.user.model.User;
import ru.practicum.mainsvc.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserShortDto getUser(Long id) {
        return userMapper.mapToUserShortDto(userRepository.getReferenceById(id));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Iterable<User> users;
        if (ids != null) {
            users = userRepository.getUsersListByIdList(ids, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return StreamSupport.stream(users.spliterator(), false)
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(NewUserDto newUserDto) {
        try {
            User user = userMapper.mapToUser(newUserDto);
            User userSaved = userRepository.save(user);
            return userMapper.mapToUserDto(userSaved);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Имя уже существует");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
