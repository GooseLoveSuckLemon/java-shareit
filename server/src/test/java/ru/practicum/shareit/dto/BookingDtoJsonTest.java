package ru.practicum.shareit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemForBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStart(LocalDateTime.of(2024, 1, 1, 12, 0));
        dto.setEnd(LocalDateTime.of(2024, 1, 2, 12, 0));
        dto.setStatus(BookingStatus.WAITING);

        BookerDto booker = new BookerDto(1L, "John Doe");
        dto.setBooker(booker);

        ItemForBookingDto item = new ItemForBookingDto(1L, "Drill");
        dto.setItem(item);

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"status\":\"WAITING\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"start\":\"2024-01-01T12:00:00\",\"end\":\"2024-01-02T12:00:00\",\"status\":\"WAITING\",\"booker\":{\"id\":1,\"name\":\"John Doe\"},\"item\":{\"id\":1,\"name\":\"Drill\"}}";

        BookingDto dto = objectMapper.readValue(json, BookingDto.class);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.WAITING);
        assertThat(dto.getBooker().getName()).isEqualTo("John Doe");
        assertThat(dto.getItem().getName()).isEqualTo("Drill");
    }
}