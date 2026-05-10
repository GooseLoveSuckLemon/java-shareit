package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collections;

@Component
public class ItemRequestMapper {

    public ItemRequestDto toDto(ItemRequest request) {
        if (request == null) return null;

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setRequestorId(request.getRequestor() != null ? request.getRequestor().getId() : null);
        dto.setItems(Collections.emptyList());
        return dto;
    }

    public ItemRequest toEntity(ItemRequestDto dto) {
        if (dto == null) return null;

        ItemRequest request = new ItemRequest();
        request.setId(dto.getId());
        request.setDescription(dto.getDescription());
        request.setCreated(dto.getCreated());
        return request;
    }
}