package ru.practicum.explorewithme.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.events.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e " +
            "from Event e " +
            "where e.initiator.id = ?1 " +
            "group by e.id " +
            "order by e.id desc")
    Page<Event> getAllEventsByUser(Long userId, Pageable pageable);


}
