package ru.practicum.shareit.item;

import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long ownerId, ItemDto itemDto);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemsByOwner(Long ownerId);

    List<ItemDto> searchAvailableItems(String s);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

    List<ItemWithBookingsDto> getItemsWithBookingsAndCommentsByOwner(Long ownerId);

    ItemWithBookingsDto getItemWithBookingsAndComments(Long userId, Long itemId);
}
