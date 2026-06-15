package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemControllerImpl implements ItemController {

    private final ItemClient itemClient;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @Override
    public ResponseEntity<Object> create(Long ownerId, ItemDto itemDto) {
        log.info("POST /items - создание вещи владельцем {}", ownerId);
        return itemClient.createItem(ownerId, itemDto);
    }

    @Override
    public ResponseEntity<Object> update(Long ownerId, Long itemId, ItemDto itemDto) {
        log.info("PATCH /items/{} - обновление вещи владельцем {}", itemId, ownerId);
        return itemClient.updateItem(ownerId, itemId, itemDto);
    }

    @Override
    public ResponseEntity<Object> getById(Long userId, Long itemId) {
        log.info("GET /items/{} - получение вещи пользователем {}", itemId, userId);
        return itemClient.getItemById(userId, itemId);
    }

    @Override
    public ResponseEntity<Object> getByOwner(Long ownerId) {
        log.info("GET /items - получение всех вещей владельца {}", ownerId);
        return itemClient.getItemsByOwner(ownerId);
    }

    @Override
    public ResponseEntity<Object> search(String text) {
        log.info("GET /items/search?text={} - поиск вещей", text);
        return itemClient.searchItems(text);
    }

    @Override
    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentDto commentDto) {
        log.info("POST /items/{}/comment - добавление комментария пользователем {}", itemId, userId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}