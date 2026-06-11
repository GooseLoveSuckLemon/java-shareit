package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class ItemRequestIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long userId;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto(null, "Test User", "test@example.com");
        ResponseEntity<UserDto> userResponse = restTemplate.postForEntity("/users", userDto, UserDto.class);
        userId = userResponse.getBody().getId();

        headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", String.valueOf(userId));
    }

    @Test
    void createAndGetRequest_ShouldWork() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");

        HttpEntity<ItemRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);
        ResponseEntity<ItemRequestDto> createResponse = restTemplate.exchange(
                "/requests",
                HttpMethod.POST,
                requestEntity,
                ItemRequestDto.class
        );

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody().getId()).isNotNull();
        assertThat(createResponse.getBody().getDescription()).isEqualTo("Need a drill");

        Long requestId = createResponse.getBody().getId();

        ResponseEntity<ItemRequestDto[]> getOwnResponse = restTemplate.exchange(
                "/requests",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ItemRequestDto[].class
        );

        assertThat(getOwnResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getOwnResponse.getBody()).hasSize(1);

        ResponseEntity<ItemRequestDto> getByIdResponse = restTemplate.exchange(
                "/requests/" + requestId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ItemRequestDto.class
        );

        assertThat(getByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getByIdResponse.getBody().getDescription()).isEqualTo("Need a drill");
    }
}