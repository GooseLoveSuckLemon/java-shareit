package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemMapper = new ItemMapper();
    }

    @Test
    void toItemDto_ShouldMapItemToDto() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Drill");
        item.setDescription("Powerful drill");
        item.setAvailable(true);
        item.setOwner(1L);
        item.setRequest(request);

        ItemDto dto = itemMapper.toItemDto(item);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getDescription()).isEqualTo("Powerful drill");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getRequestId()).isEqualTo(1L);
    }

    @Test
    void toItemDto_ShouldReturnNull_WhenItemIsNull() {
        assertThat(itemMapper.toItemDto(null)).isNull();
    }

    @Test
    void toItemModel_ShouldMapDtoToItem() {
        ItemDto dto = new ItemDto();
        dto.setName("Hammer");
        dto.setDescription("Heavy hammer");
        dto.setAvailable(true);
        dto.setRequestId(1L);

        ItemRequest request = new ItemRequest();
        request.setId(1L);

        Item item = itemMapper.toItemModel(dto, 2L, request);

        assertThat(item.getName()).isEqualTo("Hammer");
        assertThat(item.getDescription()).isEqualTo("Heavy hammer");
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getOwner()).isEqualTo(2L);
        assertThat(item.getRequest()).isEqualTo(request);
    }

    @Test
    void toItemModel_ShouldReturnNull_WhenDtoIsNull() {
        assertThat(itemMapper.toItemModel(null, 1L, null)).isNull();
    }
}