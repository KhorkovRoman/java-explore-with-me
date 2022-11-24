package ru.practicum.explorewithme.services.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dtos.comment.CommentDto;
import ru.practicum.explorewithme.dtos.comment.NewCommentDto;
import ru.practicum.explorewithme.dtos.comment.UpdateCommentDto;
import ru.practicum.explorewithme.exeptions.NotFoundException;
import ru.practicum.explorewithme.mappers.CommentMapper;
import ru.practicum.explorewithme.models.comment.Comment;
import ru.practicum.explorewithme.models.event.Event;
import ru.practicum.explorewithme.models.user.User;
import ru.practicum.explorewithme.repositories.CommentRepository;
import ru.practicum.explorewithme.repositories.EventRepository;
import ru.practicum.explorewithme.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String OBJECT_EMPTY = "Object can't be empty. Need the object: ";
    private static final String NOT_FOUND_USER = "In DB has not found user id ";
    private static final String NOT_FOUND_EVENT = "In DB has not found event id ";
    private static final String NOT_FOUND_COMMENT = "In DB has not found comment id ";
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Comment createComment(Long userId, NewCommentDto newCommentDto) {
        if (newCommentDto == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "New Comment.");
        }
        Comment comment = CommentMapper.toComment(newCommentDto);
        Event event = eventRepository.findById(newCommentDto.getEvent())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + newCommentDto.getEvent()));
        comment.setEvent(event);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + userId));
        comment.setCommentator(user);

        Comment commentDB = commentRepository.save(comment);
        log.info("Comment id " + commentDB.getId() + " has successfully created.");
        return commentDB;
    }

    @Override
    public Comment updateComment(UpdateCommentDto updateCommentDto) {
        if (updateCommentDto == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "Update Comment.");
        }
        Long commentId = updateCommentDto.getId();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT + commentId));
        if (updateCommentDto.getComment() != null) {
            comment.setComment(updateCommentDto.getComment());
        }
        Comment commentDB = commentRepository.save(comment);
        log.info("Comment id " + commentDB.getId() + " has successfully updated by user.");
        return commentDB;
    }

    @Override
    public Comment getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT + commentId));
        log.info("Comment id " + comment.getId() + " has found in DB.");
        return comment;
    }

    @Override
    public Collection<CommentDto> getCommentsByAdmin(List<Long> users, String rangeStart, String rangeEnd,
                                                  PageRequest pageRequest) {
        LocalDateTime rangeStartFormatted;
        if (rangeStart.isBlank()) {
            rangeStartFormatted = LocalDateTime.now();
        } else {
            rangeStartFormatted = LocalDateTime.parse(rangeStart, FORMATTER);
        }
        LocalDateTime rangeEndFormatted;
        if (rangeEnd.isBlank()) {
            rangeEndFormatted = LocalDateTime.now();
        } else {
            rangeEndFormatted = LocalDateTime.parse(rangeEnd, FORMATTER);
        }
        List<User> userEntities = userRepository.getUsersFromIds(users);

        Page<Comment> comments = commentRepository.getAllCommentsByAdmin(userEntities, rangeStartFormatted, rangeEndFormatted,
                                                                pageRequest);

        return CommentMapper.toCommentDtoCollection(comments.getContent());
    }

    @Override
    public Collection<Comment> getCommentsByEvent(Long eventId, PageRequest pageRequest) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
        return commentRepository.getAllCommentsByEvent(eventId, pageRequest).stream()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommentDto> getAllCommentsByUser(Long userId, String rangeStart, String rangeEnd,
                                                 PageRequest pageRequest) {
        LocalDateTime rangeStartFormatted;
        if (rangeStart.isBlank()) {
            rangeStartFormatted = LocalDateTime.now();
        } else {
            rangeStartFormatted = LocalDateTime.parse(rangeStart, FORMATTER);
        }
        LocalDateTime rangeEndFormatted;
        if (rangeEnd.isBlank()) {
            rangeEndFormatted = LocalDateTime.now();
        } else {
            rangeEndFormatted = LocalDateTime.parse(rangeEnd, FORMATTER);
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + userId));
        Collection<Comment> comments = commentRepository.getAllCommentsByUser(userId, rangeStartFormatted,
                        rangeEndFormatted, pageRequest).stream()
                .collect(Collectors.toList());

        return CommentMapper.toCommentDtoCollection(comments);
    }

    @Override
    public void deleteComment(Long commId) {
        commentRepository.findById(commId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT + commId));
        log.info("Comment id " + commId + " has deleted from DB.");
        commentRepository.deleteById(commId);
    }
}
