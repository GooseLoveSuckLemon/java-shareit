package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * Контроллер для управления вещами (Gateway).
 *
 * @author ShareIt Team
 * @version 1.0
 */
@RequestMapping("/items")
public interface ItemController {

    /**
     * Создание новой вещи.
     *
     * @param ownerId ID владельца
     * @param itemDto данные вещи
     * @return созданная вещь
     */
    @PostMapping
    ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                  @Valid @RequestBody ItemDto itemDto);

    /**
     * Обновление вещи.
     *
     * @param ownerId ID владельца
     * @param itemId ID вещи
     * @param itemDto данные для обновления
     * @return обновлённая вещь
     */
    @PatchMapping("/{itemId}")
    ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                  @PathVariable Long itemId,
                                  @RequestBody ItemDto itemDto);

    /**
     * Получение вещи по ID.
     *
     * @param userId ID пользователя
     * @param itemId ID вещи
     * @return вещь
     */
    @GetMapping("/{itemId}")
    ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long itemId);

    /**
     * Получение всех вещей владельца.
     *
     * @param ownerId ID владельца
     * @return список вещей
     */
    @GetMapping
    ResponseEntity<Object> getByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId);

    /**
     * Поиск вещей по тексту.
     *
     * @param text текст для поиска
     * @return список найденных вещей
     */
    @GetMapping("/search")
    ResponseEntity<Object> search(@RequestParam String text);

    /**
     * Добавление комментария к вещи.
     *
     * @param userId ID пользователя
     * @param itemId ID вещи
     * @param commentDto комментарий
     * @return созданный комментарий
     */
    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long itemId,
                                      @Valid @RequestBody CommentDto commentDto);
}