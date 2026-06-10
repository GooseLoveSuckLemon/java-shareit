package ru.practicum.shareit.entity;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestEntityTest {

    @Test
    void testGettersAndSetters() {
        User requestor = new User();
        requestor.setId(1L);

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need a drill");
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());

        assertThat(request.getId()).isEqualTo(1L);
        assertThat(request.getDescription()).isEqualTo("Need a drill");
        assertThat(request.getRequestor()).isEqualTo(requestor);
        assertThat(request.getCreated()).isNotNull();
    }

    @Test
    void testAllArgsConstructor() {
        User requestor = new User();
        ItemRequest request = new ItemRequest(1L, "Need a drill", requestor, LocalDateTime.now());
        assertThat(request.getId()).isEqualTo(1L);
        assertThat(request.getDescription()).isEqualTo("Need a drill");
    }

    @Test
    void testNoArgsConstructor() {
        ItemRequest request = new ItemRequest();
        assertThat(request).isNotNull();
    }
}