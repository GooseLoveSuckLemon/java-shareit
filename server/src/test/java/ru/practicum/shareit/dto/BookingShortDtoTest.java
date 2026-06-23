package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingShortDtoTest {

    @Test
    void testGettersAndSetters() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        BookingShortDto dto = new BookingShortDto();
        dto.setId(1L);
        dto.setBookerId(2L);
        dto.setBookerName("John Doe");
        dto.setStart(start);
        dto.setEnd(end);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getBookerId()).isEqualTo(2L);
        assertThat(dto.getBookerName()).isEqualTo("John Doe");
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        BookingShortDto dto = new BookingShortDto(1L, 2L, "John Doe", start, end);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getBookerId()).isEqualTo(2L);
        assertThat(dto.getBookerName()).isEqualTo("John Doe");
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
    }

    @Test
    void testNoArgsConstructor() {
        BookingShortDto dto = new BookingShortDto();
        assertThat(dto).isNotNull();
    }
}