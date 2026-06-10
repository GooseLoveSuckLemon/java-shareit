package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

class ItemClientTest {

    private ItemClient itemClient;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        itemClient = new ItemClient("http://localhost:9090", builder);
    }

    @Test
    void testConstructor() {
        assertThat(itemClient).isNotNull();
    }
}