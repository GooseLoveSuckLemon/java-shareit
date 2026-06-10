package ru.practicum.shareit.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.Comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItemIdOrderByCreatedDesc(Long itemId);
}