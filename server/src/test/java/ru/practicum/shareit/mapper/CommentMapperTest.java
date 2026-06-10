package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Comment.CommentMapper;
import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.Comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        commentMapper = new CommentMapper();
    }

    @Test
    void toCommentDto_ShouldMapCommentToDto() {
        User author = new User();
        author.setId(1L);
        author.setName("John Doe");

        Item item = new Item();
        item.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        CommentDto dto = commentMapper.toCommentDto(comment);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Great item!");
        assertThat(dto.getAuthorName()).isEqualTo("John Doe");
        assertThat(dto.getCreated()).isNotNull();
    }

    @Test
    void toCommentDto_ShouldReturnNull_WhenCommentIsNull() {
        assertThat(commentMapper.toCommentDto(null)).isNull();
    }
}