package ru.practicum.explorewithme.events.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.events.dto.*;
import ru.practicum.explorewithme.events.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

public interface EventService {

    Event createEvent(Long userId, NewEventDto newEventDto);

    Event updateEvent(Long userId, UpdateEventRequest updateEventRequest);

    Event editEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    Event cancelEvent(Long userId, Long eventId);

    Event publishEvent(Long eventId);

    Event rejectEvent(Long eventId);

    Collection<EventShortDto> getAllEventsByUser(Long userId, PageRequest pageRequest);

    Collection<EventFullDto> getAllEventsByAdmin(List<Long> users,
                                                 List<String> states,
                                                 List<Long> categories,
                                                 String rangeStart,
                                                 String rangeEnd,
                                                 PageRequest pageRequest);

    Collection<EventShortDto> getAllEventsByPublic(String text, List<Long> categories, Boolean paid,
                                                         String rangeStart, String rangeEnd,
                                                         Boolean onlyAvailable, String sort,
                                                         PageRequest pageRequest, HttpServletRequest request);

    Event getEventById(Long eventId);

    Event getEventByIdPublic(Long id, HttpServletRequest request);

}
