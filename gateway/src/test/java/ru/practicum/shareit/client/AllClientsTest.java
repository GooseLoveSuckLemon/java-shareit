package ru.practicum.shareit.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AllClientsTest {

    @Mock
    private RestTemplate restTemplate;

    @Test
    void bookingClientCreateTest() {
        BookingClient client = new BookingClient("http://localhost:8080", new RestTemplateBuilder());
        assertThat(client).isNotNull();
    }

    @Test
    void itemClientCreateTest() {
        ItemClient client = new ItemClient("http://localhost:8080", new RestTemplateBuilder());
        assertThat(client).isNotNull();
    }

    @Test
    void userClientCreateTest() {
        UserClient client = new UserClient("http://localhost:8080", new RestTemplateBuilder());
        assertThat(client).isNotNull();
    }
}