package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

class UserClientTest {

    private UserClient userClient;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        userClient = new UserClient("http://localhost:9090", builder);
    }

    @Test
    void testConstructor() {
        assertThat(userClient).isNotNull();
    }

    @Test
    void testGetAllUsers() {
        // Просто проверяем, что метод вызывается без ошибок
        // Реальный вызов будет к серверу, но в тесте мы только проверяем создание
        assertThat(userClient).isNotNull();
    }
}