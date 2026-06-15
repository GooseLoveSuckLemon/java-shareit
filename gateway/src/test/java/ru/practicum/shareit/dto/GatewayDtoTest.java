package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booker.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemForBookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayDtoTest {

    @Test
    void testUserDtoAllArgsConstructor() {
        UserDto dto = new UserDto(1L, "John", "john@example.com");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("John");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testUserDtoNoArgsConstructor() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("John");
        dto.setEmail("john@example.com");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("John");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testItemDtoAllArgsConstructor() {
        ItemDto dto = new ItemDto(1L, "Drill", "Powerful", true, 1L);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getDescription()).isEqualTo("Powerful");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getRequestId()).isEqualTo(1L);
    }

    @Test
    void testItemDtoNoArgsConstructor() {
        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("Drill");
        dto.setDescription("Powerful");
        dto.setAvailable(true);
        dto.setRequestId(1L);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
    }

    @Test
    void testBookingDtoAllArgsConstructor() {
        BookerDto booker = new BookerDto(1L, "John");
        ItemForBookingDto item = new ItemForBookingDto(1L, "Drill");
        BookingDto dto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                BookingStatus.WAITING, booker, item);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void testBookingDtoNoArgsConstructor() {
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStatus(BookingStatus.APPROVED);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void testBookerDto() {
        BookerDto dto = new BookerDto(1L, "John");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("John");
    }

    @Test
    void testItemForBookingDto() {
        ItemForBookingDto dto = new ItemForBookingDto(1L, "Drill");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
    }

    @Test
    void testCommentDto() {
        CommentDto dto = new CommentDto(1L, "Great!", "John", LocalDateTime.now());
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Great!");
        assertThat(dto.getAuthorName()).isEqualTo("John");
        assertThat(dto.getCreated()).isNotNull();
    }

    @Test
    void testItemRequestDto() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(1L);
        dto.setDescription("Need a drill");
        dto.setCreated(LocalDateTime.now());
        dto.setRequestorId(1L);
        dto.setItems(Collections.emptyList());
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Need a drill");
        assertThat(dto.getRequestorId()).isEqualTo(1L);
    }

    @Test
    void testBookingStatus() {
        assertThat(BookingStatus.WAITING).isNotNull();
        assertThat(BookingStatus.APPROVED).isNotNull();
        assertThat(BookingStatus.REJECTED).isNotNull();
        assertThat(BookingStatus.CANCELED).isNotNull();
    }

    @Test
    void testCommentDtoNoArgsConstructor() {
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setText("Great!");
        dto.setAuthorName("John");
        dto.setCreated(LocalDateTime.now());
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Great!");
    }

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
}