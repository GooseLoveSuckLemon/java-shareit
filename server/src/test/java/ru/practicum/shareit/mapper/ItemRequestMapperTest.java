package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    private ItemRequestMapper itemRequestMapper;

    @BeforeEach
    void setUp() {
        itemRequestMapper = new ItemRequestMapper();
    }

    @Test
    void toDto_ShouldMapRequestToDto() {
        User requestor = new User();
        requestor.setId(1L);

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need a drill");
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());

        ItemRequestDto dto = itemRequestMapper.toDto(request);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Need a drill");
        assertThat(dto.getRequestorId()).isEqualTo(1L);
        assertThat(dto.getCreated()).isNotNull();
    }

    @Test
    void toDto_ShouldReturnNull_WhenRequestIsNull() {
        assertThat(itemRequestMapper.toDto(null)).isNull();
    }

    @Test
    void toEntity_ShouldCreateRequestFromParameters() {
        String description = "Need a hammer";
        User requestor = new User();
        requestor.setId(1L);
        LocalDateTime created = LocalDateTime.now();

        ItemRequest request = itemRequestMapper.toEntity(description, requestor, created);

        assertThat(request).isNotNull();
        assertThat(request.getDescription()).isEqualTo("Need a hammer");
        assertThat(request.getRequestor()).isEqualTo(requestor);
        assertThat(request.getCreated()).isEqualTo(created);
    }
}