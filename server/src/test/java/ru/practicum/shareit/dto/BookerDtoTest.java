package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booker.dto.BookerDto;

import static org.assertj.core.api.Assertions.assertThat;

class BookerDtoTest {

    @Test
    void testGettersAndSetters() {
        BookerDto dto = new BookerDto();
        dto.setId(1L);
        dto.setName("John Doe");

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("John Doe");
    }

    @Test
    void testAllArgsConstructor() {
        BookerDto dto = new BookerDto(1L, "John Doe");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("John Doe");
    }
}