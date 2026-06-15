package ru.practicum.shareit.request.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * Контроллер для управления запросами на вещи.
 * Предоставляет endpoints для создания и получения запросов.
 */
@RequestMapping("/requests")
public interface ItemRequestController {

    /**
     * Создание нового запроса на вещь.
     *
     * @param userId ID пользователя, создающего запрос
     * @param requestDto данные запроса
     * @return созданный запрос
     */
    @PostMapping
    ResponseEntity<ItemRequestDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody ItemRequestDto requestDto);

    /**
     * Получение всех запросов текущего пользователя.
     *
     * @param userId ID пользователя
     * @return список запросов пользователя
     */
    @GetMapping
    ResponseEntity<List<ItemRequestDto>> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long userId);

    /**
     * Получение всех запросов других пользователей с пагинацией.
     *
     * @param userId ID текущего пользователя
     * @param from начальная позиция (по умолчанию 0)
     * @param size количество элементов (по умолчанию 10)
     * @return список запросов
     */
    @GetMapping("/all")
    ResponseEntity<List<ItemRequestDto>> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size);

    /**
     * Получение запроса по ID.
     *
     * @param userId ID текущего пользователя
     * @param requestId ID запроса
     * @return запрос с указанным ID
     */
    @GetMapping("/{requestId}")
    ResponseEntity<ItemRequestDto> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable Long requestId);
}