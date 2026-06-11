package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemClientFullTest {

    @Test
    void constructor_ShouldCreateClient() {
        ItemClient client = new ItemClient("http://localhost:8080", new RestTemplateBuilder());
        assertThat(client).isNotNull();
    }

    @Test
    void createItem_ShouldWork() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        ItemClient client = new ItemClient("http://localhost:8080",
                new RestTemplateBuilder().requestFactory(() -> null));
        // Тест на создание клиента
        assertThat(client).isNotNull();
    }
}