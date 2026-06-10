package ru.practicum.shareit.client;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class BaseClientCoverageTest {

    @Test
    void testConstructor() {
        RestTemplate restTemplate = new RestTemplate();
        BaseClient baseClient = new BaseClient(restTemplate);
        assertThat(baseClient).isNotNull();
    }
}