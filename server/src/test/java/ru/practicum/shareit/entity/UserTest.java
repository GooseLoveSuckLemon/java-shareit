package ru.practicum.shareit.entity;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(1L, "John Doe", "john@example.com");
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertThat(user).isNotNull();
    }
}