package ru.practicum.shareit.Comment;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.Comment.model.Comment;

@Component
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setCreated(comment.getCreated());

        if (comment.getAuthor() != null) {
            dto.setAuthorName(comment.getAuthor().getName());
        }

        return dto;
    }
}