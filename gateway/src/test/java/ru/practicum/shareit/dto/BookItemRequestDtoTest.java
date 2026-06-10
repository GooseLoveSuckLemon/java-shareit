package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookItemRequestDtoTest {

    @Test
    void testGettersAndSetters() {
        BookItemRequestDto dto = new BookItemRequestDto();
        assertThat(dto).isNotNull();
    }

    @Test
    void testConstructor() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(1);
        BookItemRequestDto dto = new BookItemRequestDto(1L, start, end);
        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
    }
}