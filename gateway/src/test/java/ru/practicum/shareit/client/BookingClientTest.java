package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.shareit.booking.BookingClient;

import static org.assertj.core.api.Assertions.assertThat;

class BookingClientTest {

    private BookingClient bookingClient;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        bookingClient = new BookingClient("http://localhost:9090", builder);
    }

    @Test
    void testConstructor() {
        assertThat(bookingClient).isNotNull();
    }
}