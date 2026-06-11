package ru.practicum.shareit.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AllClientsTest {

    @Mock
    private RestTemplate restTemplate;

    // Тесты для BookingClient
    @Test
    void bookingClientGetBookingsTest() {
        BookingClient client = new BookingClient("http://localhost:8080",
                new org.springframework.boot.web.client.RestTemplateBuilder());

        // Проверяем, что клиент создается
        assertThat(client).isNotNull();
    }

    // Тесты для ItemClient
    @Test
    void itemClientCreateTest() {
        ItemClient client = new ItemClient("http://localhost:8080",
                new org.springframework.boot.web.client.RestTemplateBuilder());
        assertThat(client).isNotNull();
    }

    // Тесты для UserClient
    @Test
    void userClientCreateTest() {
        UserClient client = new UserClient("http://localhost:8080",
                new org.springframework.boot.web.client.RestTemplateBuilder());
        assertThat(client).isNotNull();
    }
}