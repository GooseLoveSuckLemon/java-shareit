package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemWithBookingsDtoTest {

    @Test
    void testGettersAndSetters() {
        BookingShortDto lastBooking = new BookingShortDto();
        BookingShortDto nextBooking = new BookingShortDto();
        List<CommentDto> comments = new ArrayList<>();

        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(1L);
        dto.setName("Drill");
        dto.setDescription("Powerful drill");
        dto.setAvailable(true);
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        dto.setComments(comments);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getDescription()).isEqualTo("Powerful drill");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getLastBooking()).isEqualTo(lastBooking);
        assertThat(dto.getNextBooking()).isEqualTo(nextBooking);
        assertThat(dto.getComments()).isEqualTo(comments);
    }

    @Test
    void testAllArgsConstructor() {
        BookingShortDto lastBooking = new BookingShortDto();
        BookingShortDto nextBooking = new BookingShortDto();
        List<CommentDto> comments = new ArrayList<>();

        ItemWithBookingsDto dto = new ItemWithBookingsDto(1L, "Drill", "Powerful drill",
                true, lastBooking, nextBooking, comments);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getAvailable()).isTrue();
    }

    @Test
    void testNoArgsConstructor() {
        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        assertThat(dto).isNotNull();
    }
}