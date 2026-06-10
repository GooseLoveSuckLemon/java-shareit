package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemForBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingDtoTest {

    @Test
    void testGettersAndSetters() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(1);
        BookerDto booker = new BookerDto();
        ItemForBookingDto item = new ItemForBookingDto();

        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStart(start);
        dto.setEnd(end);
        dto.setStatus(BookingStatus.WAITING);
        dto.setBooker(booker);
        dto.setItem(item);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.WAITING);
        assertThat(dto.getBooker()).isEqualTo(booker);
        assertThat(dto.getItem()).isEqualTo(item);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(1);
        BookerDto booker = new BookerDto();
        ItemForBookingDto item = new ItemForBookingDto();

        BookingDto dto = new BookingDto(1L, start, end, BookingStatus.WAITING, booker, item);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void testNoArgsConstructor() {
        BookingDto dto = new BookingDto();
        assertThat(dto).isNotNull();
    }
}