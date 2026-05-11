package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

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
                          @Valid @RequestBody ItemDto itemDto) {
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
    public ItemDto getById(@PathVariable Long itemId) {
        log.info("GET /items/{} - получение вещи", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getByOwner(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        log.info("GET /items - получение всех вещей владельца {}", ownerId);
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("GET /items/search?text={} - поиск вещей", text);
        return itemService.searchAvailableItems(text);
    }
}
