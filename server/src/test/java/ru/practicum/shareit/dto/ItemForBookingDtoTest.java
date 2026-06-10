package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemForBookingDto;

import static org.assertj.core.api.Assertions.assertThat;

class ItemForBookingDtoTest {

    @Test
    void testGettersAndSetters() {
        ItemForBookingDto dto = new ItemForBookingDto();
        dto.setId(1L);
        dto.setName("Drill");

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
    }

    @Test
    void testAllArgsConstructor() {
        ItemForBookingDto dto = new ItemForBookingDto(1L, "Drill");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
    }
}