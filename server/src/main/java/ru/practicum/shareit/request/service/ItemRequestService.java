package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * Сервисный слой для управления запросами на вещи.
 * Предоставляет операции для создания и получения запросов.
 */
public interface ItemRequestService {

    /**
     * Создаёт новый запрос на вещь.
     *
     * @param userId ID пользователя, создающего запрос
     * @param itemRequestDto данные запроса
     * @return созданный запрос
     */
    ItemRequestDto createRequest(Long userId, ItemRequestDto itemRequestDto);

    /**
     * Возвращает все запросы текущего пользователя.
     *
     * @param userId ID пользователя
     * @return список запросов пользователя
     */
    List<ItemRequestDto> getOwnRequests(Long userId);

    /**
     * Возвращает все запросы других пользователей с пагинацией.
     *
     * @param userId ID текущего пользователя
     * @param from начальная позиция
     * @param size количество элементов
     * @return список запросов
     */
    List<ItemRequestDto> getAllRequests(Long userId, int from, int size);

    /**
     * Возвращает запрос по ID.
     *
     * @param userId ID текущего пользователя
     * @param requestId ID запроса
     * @return запрос с указанным ID
     */
    ItemRequestDto getRequestById(Long userId, Long requestId);
}