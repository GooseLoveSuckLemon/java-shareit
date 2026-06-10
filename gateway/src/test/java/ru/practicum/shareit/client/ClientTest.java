package ru.practicum.shareit.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.request.RequestClient;

import static org.assertj.core.api.Assertions.assertThat;

class ClientTest {

    @Test
    void testUserClientCreation() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        UserClient client = new UserClient("http://localhost:9090", builder);
        assertThat(client).isNotNull();
    }

    @Test
    void testItemClientCreation() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        ItemClient client = new ItemClient("http://localhost:9090", builder);
        assertThat(client).isNotNull();
    }

    @Test
    void testBookingClientCreation() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        BookingClient client = new BookingClient("http://localhost:9090", builder);
        assertThat(client).isNotNull();
    }

    @Test
    void testRequestClientCreation() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RequestClient client = new RequestClient("http://localhost:9090", builder);
        assertThat(client).isNotNull();
    }
}