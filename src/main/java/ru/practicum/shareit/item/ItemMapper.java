package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        if (item == null) return null;
        return new ItemDto(
                item.getItemId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public Item toItemModel(ItemDto itemDto, Long ownerId, ItemRequest request) {
        if (itemDto == null) return null;
        return new Item(
                null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                ownerId,
                request
        );
    }
}
