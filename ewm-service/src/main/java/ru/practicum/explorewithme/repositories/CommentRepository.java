package ru.practicum.explorewithme.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.models.comment.Comment;
import ru.practicum.explorewithme.models.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c " +
            "from Comment c " +
            "where c.commentator in ?1 " +
            "and c.created between ?2 and ?3 " +
            "group by c.id " +
            "order by c.id desc")
    Page<Comment> getAllCommentsByAdmin(List<User> userIds, LocalDateTime rangeStartFormatted,
                                       LocalDateTime rangeEndFormatted, Pageable pageable);

    @Query("select c " +
            "from Comment c " +
            "where c.commentator.id = ?1 " +
            "and c.created between ?2 and ?3 " +
            "group by c.id " +
            "order by c.id desc")
    Page<Comment> getAllCommentsByUser(Long userId, LocalDateTime rangeStartFormatted,
                                       LocalDateTime rangeEndFormatted, Pageable pageable);

    @Query("select c " +
            "from Comment c " +
            "where c.event.id = ?1 " +
            "group by c.id " +
            "order by c.id desc")
    Page<Comment> getAllCommentsByEvent(Long eventId, Pageable pageable);
}
