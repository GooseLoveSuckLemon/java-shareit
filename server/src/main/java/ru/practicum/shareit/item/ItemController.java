package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestHeader(SHARER_USER_ID) Long ownerId,
                          @RequestBody ItemDto itemDto) {
        log.info("POST /items - создание вещи владельцем {}", ownerId);
        return itemService.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(SHARER_USER_ID) Long ownerId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("PATCH /items/{} - обновление вещи владельцем {}", itemId, ownerId);
        return itemService.updateItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingsDto getById(@RequestHeader(SHARER_USER_ID) Long userId,
                                       @PathVariable Long itemId) {
        log.info("GET /items/{} - получение вещи пользователем {}", itemId, userId);
        return itemService.getItemWithBookingsAndComments(userId, itemId);
    }

    @GetMapping
    public List<ItemWithBookingsDto> getByOwner(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        log.info("GET /items - получение всех вещей владельца {}", ownerId);
        return itemService.getItemsWithBookingsAndCommentsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("GET /items/search?text={} - поиск вещей", text);
        return itemService.searchAvailableItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(SHARER_USER_ID) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("POST /items/{}/comment - добавление комментария пользователем {}", itemId, userId);
        return itemService.addComment(userId, itemId, commentDto);
    }
}