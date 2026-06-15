package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserClient userClient;

    @Override
    public ResponseEntity<Object> getAll() {
        log.info("GET /users - получение всех пользователей");
        return userClient.getAllUsers();
    }

    @Override
    public ResponseEntity<Object> getById(Long id) {
        log.info("GET /users/{} - получение пользователя", id);
        return userClient.getUserById(id);
    }

    @Override
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("POST /users - создание пользователя: {}", userDto);
        return userClient.createUser(userDto);
    }

    @Override
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("PATCH /users/{} - обновление пользователя: {}", id, userDto);
        return userClient.updateUser(id, userDto);
    }

    @Override
    public ResponseEntity<Object> deleteUser(Long id) {
        log.info("DELETE /users/{} - удаление пользователя", id);
        return userClient.deleteUser(id);
    }
}