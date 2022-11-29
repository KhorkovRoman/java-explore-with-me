package ru.practicum.explorewithme.api.public_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.ValidationPageParam;
import ru.practicum.explorewithme.dtos.comment.CommentDto;
import ru.practicum.explorewithme.mappers.CommentMapper;
import ru.practicum.explorewithme.services.comment.CommentService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/events")
public class CommentControllerPublic {

    private final CommentService commentService;

    @Autowired
    public CommentControllerPublic(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{eventId}/comments/{commentId}")
    public CommentDto getCommentByEventById(@PathVariable Long eventId,
                                            @PathVariable Long commentId) {
        log.info("Has received request to endpoint GET/event/{}/comments/{}", eventId, commentId);
        return CommentMapper.toCommentDto(commentService.getCommentById(commentId));
    }

    @GetMapping("/{eventId}/comments")
    public Collection<CommentDto> getAllCommentsByEvent(@PathVariable Long eventId,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        validatePage(from, size);
        log.info("Has received request to endpoint GET/events/{}/comments?from={}size={}",
                eventId, from, size);
        return CommentMapper.toCommentDtoCollection(commentService.getAllCommentsByEvent(eventId, from, size));
    }

    private void validatePage(Integer from, Integer size) {
        ValidationPageParam validationPageParam = new ValidationPageParam(from, size);
        validationPageParam.validatePageParam();
    }
}
