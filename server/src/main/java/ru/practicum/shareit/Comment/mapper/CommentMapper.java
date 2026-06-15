package ru.practicum.shareit.Comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.Comment.model.Comment;

/**
 * Маппер для преобразования между сущностью Comment и CommentDto.
 */
@Component
public class CommentMapper {

    /**
     * Преобразует сущность Comment в CommentDto.
     *
     * @param comment сущность комментария
     * @return DTO комментария
     */
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