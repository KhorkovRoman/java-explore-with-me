package ru.practicum.explorewithme.events.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.events.dto.EventShortDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.events.dto.UpdateEventRequest;
import ru.practicum.explorewithme.events.model.Event;

import java.util.Collection;

public interface EventService {

    Event createEvent(Long userId, NewEventDto newEventDto);

    Event updateEvent(Long userId, UpdateEventRequest updateEventRequest);

    Event cancelEvent(Long userId, Long eventId);

    Event publishEvent(Long eventId);

    Event rejectEvent(Long eventId);

    Collection<EventShortDto> getAllEventsByUser(Long userId, PageRequest pageRequest);

    Event getEventById(Long userId, Long eventId);

    Event getEventByIdPublic(Long id);

    void deleteEvent(Long eventId);

}
