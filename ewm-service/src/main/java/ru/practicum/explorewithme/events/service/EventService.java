package ru.practicum.explorewithme.events.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.events.model.Event;

import java.util.Collection;

public interface EventService {

    Event createEvent(NewEventDto eventDto);

    Event updateEvent(EventFullDto eventFullDto);

    Collection<Event> getAllEvents(PageRequest pageRequest);

    Event getEventById(Long eventId);

    void deleteEvent(Long eventId);

}
