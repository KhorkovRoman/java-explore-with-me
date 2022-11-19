package ru.practicum.explorewithme.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.requests.model.Request;

import java.util.Collection;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select r " +
            "from Request r " +
            "where r.requester.id = ?1 " +
            "order by r.created desc")
    Collection<Request> getAllRequestsByUser(Long userId);

    @Query("select r " +
            "from Request r " +
            "where (r.event.initiator.id = ?1) and (r.event.id = ?2)  " +
            "order by r.created desc")
    Collection<Request> getAllRequestsOnEventByUser(Long userId, Long eventId);
}
