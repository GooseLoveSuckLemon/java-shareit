package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRequestDtoTest {

    @Test
    void testGettersAndSetters() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(1);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(start);
        dto.setEnd(end);

        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(1);
        BookingRequestDto dto = new BookingRequestDto(1L, start, end);

        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
    }

    @Test
    void testNoArgsConstructor() {
        BookingRequestDto dto = new BookingRequestDto();
        assertThat(dto).isNotNull();
    }
}