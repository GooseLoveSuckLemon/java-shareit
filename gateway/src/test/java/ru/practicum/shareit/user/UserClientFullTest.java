package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.shareit.client.UserClient;

import static org.assertj.core.api.Assertions.assertThat;

class UserClientFullTest {

    @Test
    void constructor_ShouldCreateClient() {
        UserClient client = new UserClient("http://localhost:8080", new RestTemplateBuilder());
        assertThat(client).isNotNull();
    }
}