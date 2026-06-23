package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemControllerImpl implements ItemController {

    private final ItemService itemService;

    @Override
    public ResponseEntity<ItemDto> create(Long ownerId, ItemDto itemDto) {
        log.info("POST /items - создание вещи владельцем {}", ownerId);
        return ResponseEntity.ok(itemService.createItem(ownerId, itemDto));
    }

    @Override
    public ResponseEntity<ItemDto> update(Long ownerId, Long itemId, ItemDto itemDto) {
        log.info("PATCH /items/{} - обновление вещи владельцем {}", itemId, ownerId);
        return ResponseEntity.ok(itemService.updateItem(ownerId, itemId, itemDto));
    }

    @Override
    public ResponseEntity<ItemWithBookingsDto> getById(Long userId, Long itemId) {
        log.info("GET /items/{} - получение вещи пользователем {}", itemId, userId);
        return ResponseEntity.ok(itemService.getItemWithBookingsAndComments(userId, itemId));
    }

    @Override
    public ResponseEntity<List<ItemWithBookingsDto>> getByOwner(Long ownerId) {
        log.info("GET /items - получение всех вещей владельца {}", ownerId);
        return ResponseEntity.ok(itemService.getItemsWithBookingsAndCommentsByOwner(ownerId));
    }

    @Override
    public ResponseEntity<List<ItemDto>> search(String text) {
        log.info("GET /items/search?text={} - поиск вещей", text);
        return ResponseEntity.ok(itemService.searchAvailableItems(text));
    }

    @Override
    public ResponseEntity<CommentDto> addComment(Long userId, Long itemId, CommentDto commentDto) {
        log.info("POST /items/{}/comment - добавление комментария пользователем {}", itemId, userId);
        return ResponseEntity.ok(itemService.addComment(userId, itemId, commentDto));
    }
}