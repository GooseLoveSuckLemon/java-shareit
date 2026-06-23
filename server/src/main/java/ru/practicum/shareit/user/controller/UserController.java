package ru.practicum.shareit.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 * Предоставляет endpoints для CRUD операций с пользователями.
 */
@RequestMapping("/users")
public interface UserController {

    /**
     * Получение всех пользователей.
     *
     * @return список всех пользователей
     */
    @GetMapping
    ResponseEntity<List<UserDto>> getAll();

    /**
     * Получение пользователя по ID.
     *
     * @param id ID пользователя
     * @return пользователь с указанным ID
     */
    @GetMapping("/{id}")
    ResponseEntity<UserDto> getById(@PathVariable Long id);

    /**
     * Создание нового пользователя.
     *
     * @param userDto данные пользователя
     * @return созданный пользователь
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UserDto> create(@RequestBody UserDto userDto);

    /**
     * Обновление существующего пользователя.
     *
     * @param id ID пользователя
     * @param userDto данные для обновления
     * @return обновлённый пользователь
     */
    @PatchMapping("/{id}")
    ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto);

    /**
     * Удаление пользователя по ID.
     *
     * @param id ID пользователя
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id);
}