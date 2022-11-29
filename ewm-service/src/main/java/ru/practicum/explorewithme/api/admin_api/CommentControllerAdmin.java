package ru.practicum.explorewithme.api.admin_api;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.ValidationPageParam;
import ru.practicum.explorewithme.dtos.comment.CommentDto;
import ru.practicum.explorewithme.dtos.comment.UpdateCommentDto;
import ru.practicum.explorewithme.mappers.CommentMapper;
import ru.practicum.explorewithme.services.comment.CommentService;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/comments")
public class CommentControllerAdmin {

    private final CommentService commentService;

    @Autowired
    public CommentControllerAdmin(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping()
    public Collection<CommentDto> getCommentsByAdmin(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(defaultValue = "") String rangeStart,
                                                     @RequestParam(defaultValue = "") String rangeEnd,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @NotNull @RequestParam(defaultValue = "10") Integer size) {
        validatePage(from, size);
        log.info("Has received request to endpoint GET/admin/comments?users={}" +
                "rangeStart{}rangeEnd{}from={}size={}", users, rangeStart, rangeEnd, from, size);
        return commentService.getCommentsByAdmin(users, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.info("Has received request to endpoint GET/admin/comments/{}", commentId);
        return CommentMapper.toCommentDto(commentService.getCommentById(commentId));
    }

    @PutMapping("/{commentId}")
    public CommentDto updateCommentByAdmin(@PathVariable Long commentId,
                                         @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Has received request to endpoint PUT/admin/comments/{}", commentId);
        return CommentMapper.toCommentDto(commentService.updateCommentByAdmin(updateCommentDto));
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        log.info("Has received request to endpoint DELETE/admin/comments/{}", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }

    private void validatePage(Integer from, Integer size) {
        ValidationPageParam validationPageParam = new ValidationPageParam(from, size);
        validationPageParam.validatePageParam();
    }
}
