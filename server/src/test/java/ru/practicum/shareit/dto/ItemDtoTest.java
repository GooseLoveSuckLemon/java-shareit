package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

class ItemDtoTest {

    @Test
    void testGettersAndSetters() {
        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("Drill");
        dto.setDescription("Powerful drill");
        dto.setAvailable(true);
        dto.setRequestId(1L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getDescription()).isEqualTo("Powerful drill");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getRequestId()).isEqualTo(1L);
    }

    @Test
    void testAllArgsConstructor() {
        ItemDto dto = new ItemDto(1L, "Drill", "Powerful drill", true, 1L);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getRequestId()).isEqualTo(1L);
    }

    @Test
    void testNoArgsConstructor() {
        ItemDto dto = new ItemDto();
        assertThat(dto).isNotNull();
    }
}