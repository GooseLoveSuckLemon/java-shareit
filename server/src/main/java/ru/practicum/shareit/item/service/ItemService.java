package ru.practicum.shareit.item.service;

import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.List;

/**
 * Сервисный слой для управления вещами.
 * Предоставляет операции для создания, обновления, поиска вещей и управления комментариями.
 */
public interface ItemService {

    /**
     * Создаёт новую вещь.
     *
     * @param ownerId ID владельца вещи
     * @param itemDto данные вещи
     * @return созданная вещь
     * @throws ru.practicum.shareit.exception.NotFoundException если пользователь не найден
     */
    ItemDto createItem(Long ownerId, ItemDto itemDto);

    /**
     * Обновляет существующую вещь.
     *
     * @param ownerId ID владельца
     * @param itemId ID вещи
     * @param itemDto данные для обновления
     * @return обновлённая вещь
     */
    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);

    /**
     * Возвращает вещь по ID.
     *
     * @param itemId ID вещи
     * @return DTO вещи
     */
    ItemDto getItemById(Long itemId);

    /**
     * Возвращает вещь с бронированиями и комментариями.
     *
     * @param userId ID текущего пользователя
     * @param itemId ID вещи
     * @return вещь с информацией о бронированиях и комментариях
     */
    ItemWithBookingsDto getItemWithBookingsAndComments(Long userId, Long itemId);

    /**
     * Возвращает все вещи владельца.
     *
     * @param ownerId ID владельца
     * @return список DTO вещей
     */
    List<ItemDto> getItemsByOwner(Long ownerId);

    /**
     * Возвращает все вещи владельца с бронированиями и комментариями.
     *
     * @param ownerId ID владельца
     * @return список вещей с информацией о бронированиях и комментариях
     */
    List<ItemWithBookingsDto> getItemsWithBookingsAndCommentsByOwner(Long ownerId);

    /**
     * Поиск доступных вещей по тексту.
     *
     * @param text текст для поиска
     * @return список вещей, соответствующих запросу
     */
    List<ItemDto> searchAvailableItems(String text);

    /**
     * Добавляет комментарий к вещи.
     *
     * @param userId ID пользователя, оставляющего комментарий
     * @param itemId ID вещи
     * @param commentDto текст комментария
     * @return созданный комментарий
     * @throws ru.practicum.shareit.exception.BadRequestException если пользователь не брал вещь в аренду
     */
    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}