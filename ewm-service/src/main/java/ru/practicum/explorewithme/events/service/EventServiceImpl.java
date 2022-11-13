package ru.practicum.explorewithme.events.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.categories.repository.CategoryRepository;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.events.model.Event;
import ru.practicum.explorewithme.events.repository.EventRepository;

import java.util.Collection;

@Slf4j
@Service
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Event createEvent(NewEventDto eventDto) {
        return null;
    }

    @Override
    public Event updateEvent(EventFullDto eventFullDto) {
        return null;
    }

    @Override
    public Collection<Event> getAllEvents(PageRequest pageRequest) {
        return null;
    }

    @Override
    public Event getEventById(Long eventId) {
        return null;
    }

    @Override
    public void deleteEvent(Long eventId) {

    }
}
