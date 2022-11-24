package ru.practicum.explorewithme.services.comment;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.dtos.comment.CommentDto;
import ru.practicum.explorewithme.dtos.comment.NewCommentDto;
import ru.practicum.explorewithme.dtos.comment.UpdateCommentDto;
import ru.practicum.explorewithme.models.comment.Comment;

import java.util.Collection;
import java.util.List;

public interface CommentService {

    Comment createComment(Long userId, NewCommentDto newCommentDto);

    Comment updateComment(UpdateCommentDto updateCommentDto);

    Comment getCommentById(Long commId);

    Collection<CommentDto> getCommentsByAdmin(List<Long> users,
                                              String rangeStart,
                                              String rangeEnd,
                                              PageRequest pageRequest);

    Collection<Comment> getCommentsByEvent(Long eventId, PageRequest pageRequest);

    Collection<CommentDto> getAllCommentsByUser(Long userId,
                                          String rangeStart,
                                          String rangeEnd,
                                          PageRequest pageRequest);

    void deleteComment(Long commId);
}
