package ru.practicum.shareit.entity;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @Test
    void testGettersAndSetters() {
        User author = new User();
        author.setId(1L);

        Item item = new Item();
        item.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        assertThat(comment.getId()).isEqualTo(1L);
        assertThat(comment.getText()).isEqualTo("Great item!");
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getCreated()).isNotNull();
    }

    @Test
    void testAllArgsConstructor() {
        User author = new User();
        Item item = new Item();
        Comment comment = new Comment(1L, "Great item!", item, author, LocalDateTime.now());
        assertThat(comment.getId()).isEqualTo(1L);
        assertThat(comment.getText()).isEqualTo("Great item!");
    }

    @Test
    void testNoArgsConstructor() {
        Comment comment = new Comment();
        assertThat(comment).isNotNull();
    }
}