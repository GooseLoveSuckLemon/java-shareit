package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

/**
 * Контроллер для управления запросами на вещи (Gateway).
 *
 * @author ShareIt Team
 * @version 1.0
 */
@RequestMapping("/requests")
public interface ItemRequestController {

    /**
     * Создание нового запроса.
     *
     * @param userId ID пользователя
     * @param requestDto данные запроса
     * @return созданный запрос
     */
    @PostMapping
    ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @Valid @RequestBody ItemRequestDto requestDto);

    /**
     * Получение своих запросов.
     *
     * @param userId ID пользователя
     * @return список запросов
     */
    @GetMapping
    ResponseEntity<Object> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long userId);

    /**
     * Получение всех запросов с пагинацией.
     *
     * @param userId ID пользователя
     * @param from начальная позиция
     * @param size количество элементов
     * @return список запросов
     */
    @GetMapping("/all")
    ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size);

    /**
     * Получение запроса по ID.
     *
     * @param userId ID пользователя
     * @param requestId ID запроса
     * @return запрос
     */
    @GetMapping("/{requestId}")
    ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long requestId);
}