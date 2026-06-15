package ru.practicum.shareit.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * Контроллер для управления пользователями (Gateway).
 * Предоставляет endpoints для CRUD операций с пользователями.
 *
 * @author ShareIt Team
 * @version 1.0
 */
@RequestMapping("/users")
public interface UserController {

    /**
     * Получение всех пользователей.
     *
     * @return список всех пользователей
     */
    @GetMapping
    ResponseEntity<Object> getAll();

    /**
     * Получение пользователя по ID.
     *
     * @param id ID пользователя
     * @return пользователь с указанным ID
     */
    @GetMapping("/{id}")
    ResponseEntity<Object> getById(@PathVariable Long id);

    /**
     * Создание нового пользователя.
     *
     * @param userDto данные пользователя
     * @return созданный пользователь
     */
    @PostMapping
    ResponseEntity<Object> create(@RequestBody UserDto userDto);

    /**
     * Обновление существующего пользователя.
     *
     * @param id ID пользователя
     * @param userDto данные для обновления
     * @return обновлённый пользователь
     */
    @PatchMapping("/{id}")
    ResponseEntity<Object> update(@PathVariable Long id, @RequestBody UserDto userDto);

    /**
     * Удаление пользователя по ID.
     *
     * @param id ID пользователя
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteUser(@PathVariable Long id);
}