package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.shareit.request.RequestClient;

import static org.assertj.core.api.Assertions.assertThat;

class RequestClientTest {

    private RequestClient requestClient;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        requestClient = new RequestClient("http://localhost:9090", builder);
    }

    @Test
    void testConstructor() {
        assertThat(requestClient).isNotNull();
    }
}