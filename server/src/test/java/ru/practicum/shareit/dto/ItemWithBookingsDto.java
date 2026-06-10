package ru.practicum.shareit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemWithBookingsDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(1L);
        dto.setName("Drill");
        dto.setDescription("Powerful drill");
        dto.setAvailable(true);

        BookingShortDto lastBooking = new BookingShortDto(1L, 2L, "Booker",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1));
        dto.setLastBooking(lastBooking);

        BookingShortDto nextBooking = new BookingShortDto(2L, 2L, "Booker",
                LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1));
        dto.setNextBooking(nextBooking);

        dto.setComments(Collections.emptyList());

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Drill\"");
        assertThat(json).contains("\"available\":true");
    }
}