package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("GET /users - получение всех пользователей");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserDto> getById(Long id) {
        log.info("GET /users/{} - получение пользователя", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<UserDto> create(UserDto userDto) {
        log.info("POST /users - создание пользователя: {}", userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @Override
    public ResponseEntity<UserDto> update(Long id, UserDto userDto) {
        log.info("PATCH /users/{} - обновление пользователя: {}", id, userDto);
        return ResponseEntity.ok(userService.updateUser(userDto, id));
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        log.info("DELETE /users/{} - удаление пользователя", id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}