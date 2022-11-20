package ru.practicum.explorewithme.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.categories.model.Category;
import ru.practicum.explorewithme.events.model.Event;
import ru.practicum.explorewithme.events.model.EventStatus;
import ru.practicum.explorewithme.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e " +
            "from Event e " +
            "where e.initiator.id = ?1 " +
            "group by e.id " +
            "order by e.id desc")
    Page<Event> getAllEventsByUser(Long userId, Pageable pageable);

    @Query("select e " +
            "from Event e " +
            "where e.initiator in ?1 " +
            "and e.state in ?2 "+
            "and e.category in ?3 "+
            "and e.eventDate between ?4 and ?5 " +
            "group by e.id " +
            "order by e.eventDate desc")
    Page<Event> getAllEventsByAdmin(List<User> users, List<EventStatus> states, List<Category> categoryEntities,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    PageRequest pageRequest);

    @Query("select e " +
            "from Event e " +
            "where upper(e.annotation) like upper(concat('%',?1,'%')) " +
            "or upper(e.description) like upper(concat('%',?1,'%')) " +
            "and e.category in ?2 "+
            "and e.paid = ?3 "+
            "and e.eventDate between ?4 and ?5 " +
            "and (e.participantLimit - e.confirmedRequests) > 0 " +
            "and e.state = ?6 " +
            "group by e.id " +
            "order by e.eventDate desc")
    Page<Event> getAllEventsPublicByEventDateAvailable(String text, List<Category> categoryEntities, Boolean paid,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                       PageRequest pageRequest);

    @Query("select e " +
            "from Event e " +
            "where upper(e.annotation) like upper(concat('%',?1,'%')) " +
            "or upper(e.description) like upper(concat('%',?1,'%')) " +
            "and e.category in ?2 "+
            "and e.paid = ?3 "+
            "and e.eventDate between ?4 and ?5 " +
            "and (e.participantLimit - e.confirmedRequests) > 0 " +
            "and e.state = ?6 " +
            "group by e.id " +
            "order by e.views desc")
    Page<Event> getAllEventsPublicByViewsAvailable(String text, List<Category> categoryEntities, Boolean paid,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   PageRequest pageRequest);

    @Query("select e " +
            "from Event e " +
            "where e.category in ?2 "+
            "and e.paid = ?3 "+
            "and e.eventDate between ?4 and ?5 " +
            "and (e.participantLimit - e.confirmedRequests) > 0 " +
            "and e.state = ?6 " +
            "group by e.id " +
            "order by e.eventDate desc")
    Page<Event> getAllEventsPublicByEventDateAvailableAllText(String text, List<Category> categoryEntities, Boolean paid,
                                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                              PageRequest pageRequest);

    @Query("select e " +
            "from Event e " +
            "where e.category in ?2 "+
            "and e.paid = ?3 "+
            "and e.eventDate between ?4 and ?5 " +
            "and (e.participantLimit - e.confirmedRequests) > 0 " +
            "and e.state = ?6 " +
            "group by e.id " +
            "order by e.views desc")
    Page<Event> getAllEventsPublicByViewsAvailableAllText(String text, List<Category> categoryEntities, Boolean paid,
                                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                          PageRequest pageRequest);

    @Query("select e " +
            "from Event e " +
            "where upper(e.annotation) like upper(concat('%',?1,'%')) " +
            "or upper(e.description) like upper(concat('%',?1,'%')) " +
            "and e.category in ?2 "+
            "and e.paid = ?3 "+
            "and e.eventDate between ?4 and ?5 " +
            "group by e.id " +
            "order by e.eventDate desc")
    Page<Event> getAllEventsPublicByEventDate(String text, List<Category> categoryEntities, Boolean paid,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                              PageRequest pageRequest);

    @Query("select e " +
            "from Event e " +
            "where upper(e.annotation) like upper(concat('%',?1,'%')) " +
            "or upper(e.description) like upper(concat('%',?1,'%')) " +
            "and e.category in ?2 "+
            "and e.paid = ?3 "+
            "and e.eventDate between ?4 and ?5 " +
            "group by e.id " +
            "order by e.views desc")
    Page<Event> getAllEventsPublicByViews(String text, List<Category> categoryEntities, Boolean paid,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          PageRequest pageRequest);

    @Query("select e " +
            "from Event e " +
            "where e.category in ?2 "+
            "and e.paid = ?3 "+
            "and e.eventDate between ?4 and ?5 " +
            "group by e.id " +
            "order by e.eventDate desc")
    Page<Event> getAllEventsPublicByEventDateAllText(String text, List<Category> categoryEntities, Boolean paid,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     PageRequest pageRequest);

    @Query("select e " +
            "from Event e " +
            "where e.category in ?2 "+
            "and e.paid = ?3 "+
            "and e.eventDate between ?4 and ?5 " +
            "group by e.id " +
            "order by e.views desc")
    Page<Event> getAllEventsPublicByViewsAllText(String text, List<Category> categoryEntities, Boolean paid,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 PageRequest pageRequest);
}
