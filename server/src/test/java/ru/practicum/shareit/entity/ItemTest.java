package ru.practicum.shareit.entity;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    @Test
    void testGettersAndSetters() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Drill");
        item.setDescription("Powerful drill");
        item.setAvailable(true);
        item.setOwner(1L);
        item.setRequest(request);

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("Drill");
        assertThat(item.getDescription()).isEqualTo("Powerful drill");
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getOwner()).isEqualTo(1L);
        assertThat(item.getRequest()).isEqualTo(request);
    }

    @Test
    void testAllArgsConstructor() {
        ItemRequest request = new ItemRequest();
        Item item = new Item(1L, "Drill", "Powerful drill", true, 1L, request);
        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("Drill");
        assertThat(item.getAvailable()).isTrue();
    }

    @Test
    void testNoArgsConstructor() {
        Item item = new Item();
        assertThat(item).isNotNull();
    }
}