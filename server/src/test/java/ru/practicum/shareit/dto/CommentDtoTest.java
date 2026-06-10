package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Comment.dto.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentDtoTest {

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setText("Great item!");
        dto.setAuthorName("John Doe");
        dto.setCreated(now);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Great item!");
        assertThat(dto.getAuthorName()).isEqualTo("John Doe");
        assertThat(dto.getCreated()).isEqualTo(now);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        CommentDto dto = new CommentDto(1L, "Great item!", "John Doe", now);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Great item!");
        assertThat(dto.getAuthorName()).isEqualTo("John Doe");
        assertThat(dto.getCreated()).isEqualTo(now);
    }

    @Test
    void testNoArgsConstructor() {
        CommentDto dto = new CommentDto();
        assertThat(dto).isNotNull();
    }
}