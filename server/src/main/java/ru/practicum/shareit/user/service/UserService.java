package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Сервисный слой для управления пользователями.
 * Предоставляет CRUD операции для пользователей.
 */
public interface UserService {

    /**
     * Возвращает всех пользователей.
     *
     * @return список всех пользователей
     */
    List<UserDto> getAllUsers();

    /**
     * Возвращает пользователя по ID.
     *
     * @param userId ID пользователя
     * @return DTO пользователя
     * @throws ru.practicum.shareit.exception.NotFoundException если пользователь не найден
     */
    UserDto getUserById(Long userId);

    /**
     * Создаёт нового пользователя.
     *
     * @param userDto данные пользователя
     * @return созданный пользователь
     * @throws ru.practicum.shareit.exception.DuplicateEmailException если email уже существует
     */
    UserDto createUser(UserDto userDto);

    /**
     * Обновляет существующего пользователя.
     *
     * @param userDto данные для обновления
     * @param id ID пользователя
     * @return обновлённый пользователь
     */
    UserDto updateUser(UserDto userDto, Long id);

    /**
     * Удаляет пользователя по ID.
     *
     * @param userId ID пользователя
     */
    void deleteUser(Long userId);
}
