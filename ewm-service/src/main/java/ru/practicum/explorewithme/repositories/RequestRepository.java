package ru.practicum.explorewithme.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.models.request.Request;
import ru.practicum.explorewithme.models.request.RequestStatus;

import java.util.Collection;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select r " +
            "from Request r " +
            "where r.requester.id = ?1 " +
            "order by r.created desc")
    Collection<Request> getAllRequestsByUser(Long userId);

    @Query("select r " +
            "from Request r " +
            "where (r.event.initiator.id = ?1) and (r.event.id = ?2) " +
            "order by r.created desc")
    Collection<Request> getAllRequestsOnEventByUser(Long userId, Long eventId);

    @Query("select count(r.id) " +
            "from Request r " +
            "where (r.event.id = ?1) " +
            "and (r.status = ?2) " +
            "group by r.created " +
            "order by r.created desc")
    List<Object[]> getConfirmedRequestsByEvent(Long eventId, RequestStatus status);
}
