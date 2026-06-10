package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemForBookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class AllDtosTest {

    @Test
    void testUserDto() {
        UserDto dto = new UserDto(1L, "John", "john@example.com");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("John");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testItemDto() {
        ItemDto dto = new ItemDto(1L, "Drill", "Powerful", true, 1L);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getAvailable()).isTrue();
    }

    @Test
    void testBookingDto() {
        BookerDto booker = new BookerDto(1L, "John");
        ItemForBookingDto item = new ItemForBookingDto(1L, "Drill");
        BookingDto dto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                BookingStatus.WAITING, booker, item);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void testItemRequestDto() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(1L);
        dto.setDescription("Need a drill");
        dto.setItems(Collections.emptyList());
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Need a drill");
    }

    @Test
    void testCommentDto() {
        CommentDto dto = new CommentDto(1L, "Great!", "John", LocalDateTime.now());
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Great!");
    }
}