package ru.practicum.explorewithme.api.private_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.ValidationPageParam;
import ru.practicum.explorewithme.dtos.comment.CommentDto;
import ru.practicum.explorewithme.dtos.comment.NewCommentDto;
import ru.practicum.explorewithme.dtos.comment.UpdateCommentDto;
import ru.practicum.explorewithme.mappers.CommentMapper;
import ru.practicum.explorewithme.services.comment.CommentService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class CommentControllerPrivate {

    private final CommentService commentService;

    @Autowired
    public CommentControllerPrivate(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{userId}/comments")
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestBody NewCommentDto newCommentDto) {
        log.info("Has received request to endpoint POST/users/{}/comments", userId);
        return CommentMapper.toCommentDto(commentService.createComment(userId, newCommentDto));
    }

    @PatchMapping("/{userId}/comments")
    public CommentDto updateComment(@PathVariable Long userId,
                                  @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Has received request to endpoint PATCH/users/{}/comments", userId);
        return CommentMapper.toCommentDto(commentService.updateComment(updateCommentDto));
    }

    @GetMapping("/{userId}/comments")
    public Collection<CommentDto> getAllCommentsByUser(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = "") String rangeStart,
                                                       @RequestParam(defaultValue = "") String rangeEnd,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        validatePage(from, size);
        log.info("Has received request to endpoint GET/users/{}/comments?rangeStart={}rangeEnd={}from={}size={}",
                userId, rangeStart, rangeEnd, from, size);
        final PageRequest pageRequest = findPageRequest(from, size);
        return commentService.getAllCommentsByUser(userId, rangeStart, rangeEnd, pageRequest);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable Long userId,
                                     @PathVariable Long commentId) {
        log.info("Has received request to endpoint GET/users/{}/comments/{}", userId, commentId);
        return CommentMapper.toCommentDto(commentService.getCommentById(commentId));
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public void deleteCommentByUser(@PathVariable Long userId,
                                    @PathVariable Long commentId) {
        log.info("Has received request to endpoint DELETE/users/{}/comments/{}", userId, commentId);
        commentService.deleteComment(commentId);
    }

    public PageRequest findPageRequest(Integer from, Integer size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }

    private void validatePage(Integer from, Integer size) {
        ValidationPageParam validationPageParam = new ValidationPageParam(from, size);
        validationPageParam.validatePageParam();
    }
}
