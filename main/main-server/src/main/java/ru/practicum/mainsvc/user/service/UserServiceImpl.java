package ru.practicum.mainsvc.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsvc.exception.ConflictException;
import ru.practicum.mainsvc.exception.NotFoundException;
import ru.practicum.mainsvc.user.dto.UserDto;
import ru.practicum.mainsvc.user.mapper.UserMapper;
import ru.practicum.mainsvc.user.model.User;
import ru.practicum.mainsvc.user.repository.UserRepository;
import ru.practicum.mainsvc.util.Pagination;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        try {
            User user = userMapper.userFromDto(userDto);
            User userSaved = userRepository.save(user);
            return userMapper.userToDto(userSaved);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Пользователь с указанным адресом электронной почты уже существует.");
        }
    }

    @Override
    public List<UserDto> getUsers(Long[] ids, Integer from, Integer size) {
        Pagination page = new Pagination(from, size);

        if (ids == null) {
            return userRepository.findAll(page)
                    .stream()
                    .map(userMapper::userToDto)
                    .collect(Collectors.toList());
        }

        return userRepository.findAllByIdIn(ids, page)
                .stream()
                .map(userMapper::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        getUser(userId);
        userRepository.deleteById(userId);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
