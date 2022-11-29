package ru.practicum.explorewithme.services.comment;

import ru.practicum.explorewithme.dtos.comment.CommentDto;
import ru.practicum.explorewithme.dtos.comment.NewCommentDto;
import ru.practicum.explorewithme.dtos.comment.UpdateCommentDto;
import ru.practicum.explorewithme.models.comment.Comment;

import java.util.Collection;
import java.util.List;

public interface CommentService {

    Comment createComment(Long userId, NewCommentDto newCommentDto);

    Comment updateCommentByAdmin(UpdateCommentDto updateCommentDto);

    Comment updateCommentByUser(Long userId, UpdateCommentDto updateCommentDto);

    Comment getCommentById(Long commId);

    Collection<CommentDto> getCommentsByAdmin(List<Long> users,
                                              String rangeStart,
                                              String rangeEnd,
                                              Integer from, Integer size);

    Collection<Comment> getAllCommentsByEvent(Long eventId, Integer from, Integer size);

    Collection<CommentDto> getAllCommentsByUser(Long userId,
                                          String rangeStart,
                                          String rangeEnd,
                                                Integer from, Integer size);

    void deleteCommentByAdmin(Long commId);

    void deleteCommentByUser(Long userId, Long commId);
}
