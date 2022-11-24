package ru.practicum.explorewithme.mappers;

import lombok.Data;
import ru.practicum.explorewithme.dtos.comment.CommentDto;
import ru.practicum.explorewithme.dtos.comment.NewCommentDto;
import ru.practicum.explorewithme.models.comment.Comment;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class CommentMapper {

    public static Collection<CommentDto> toCommentDtoCollection(Collection<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .comment(comment.getComment())
                .event(comment.getEvent().getId())
                .commentator(comment.getCommentator().getId())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .comment(newCommentDto.getComment())
                .created(newCommentDto.getCreated())
                .build();
    }
}
