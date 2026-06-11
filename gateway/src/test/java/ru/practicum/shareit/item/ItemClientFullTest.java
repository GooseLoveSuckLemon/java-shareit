package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
        assertThat(client).isNotNull();
    }
}