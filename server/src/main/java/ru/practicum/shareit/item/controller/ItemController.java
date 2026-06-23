package ru.practicum.shareit.item.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.List;

/**
 * Контроллер для управления вещами.
 * Предоставляет endpoints для создания, обновления, получения вещей и комментариев.
 */
@RequestMapping("/items")
public interface ItemController {

    /**
     * Создание новой вещи.
     *
     * @param ownerId ID владельца вещи
     * @param itemDto данные вещи
     * @return созданная вещь
     */
    @PostMapping
    ResponseEntity<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                   @RequestBody ItemDto itemDto);

    /**
     * Обновление существующей вещи.
     *
     * @param ownerId ID владельца
     * @param itemId ID вещи
     * @param itemDto данные для обновления
     * @return обновлённая вещь
     */
    @PatchMapping("/{itemId}")
    ResponseEntity<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                   @PathVariable Long itemId,
                                   @RequestBody ItemDto itemDto);

    /**
     * Получение вещи по ID с информацией о бронированиях и комментариях.
     *
     * @param userId ID текущего пользователя
     * @param itemId ID вещи
     * @return вещь с бронированиями и комментариями
     */
    @GetMapping("/{itemId}")
    ResponseEntity<ItemWithBookingsDto> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId);

    /**
     * Получение всех вещей владельца.
     *
     * @param ownerId ID владельца
     * @return список вещей с бронированиями и комментариями
     */
    @GetMapping
    ResponseEntity<List<ItemWithBookingsDto>> getByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId);

    /**
     * Поиск доступных вещей по тексту.
     *
     * @param text текст для поиска
     * @return список вещей, соответствующих запросу
     */
    @GetMapping("/search")
    ResponseEntity<List<ItemDto>> search(@RequestParam String text);

    /**
     * Добавление комментария к вещи.
     *
     * @param userId ID пользователя, оставляющего комментарий
     * @param itemId ID вещи
     * @param commentDto текст комментария
     * @return созданный комментарий
     */
    @PostMapping("/{itemId}/comment")
    ResponseEntity<CommentDto> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long itemId,
                                          @RequestBody CommentDto commentDto);
}