package ru.practicum.explorewithme.events.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.categories.model.Category;
import ru.practicum.explorewithme.categories.repository.CategoryRepository;
import ru.practicum.explorewithme.events.dto.*;
import ru.practicum.explorewithme.events.mapper.EventMapper;
import ru.practicum.explorewithme.events.mapper.LocationMapper;
import ru.practicum.explorewithme.events.model.Event;
import ru.practicum.explorewithme.events.model.EventStatus;
import ru.practicum.explorewithme.events.model.LocationModel;
import ru.practicum.explorewithme.events.repository.EventRepository;
import ru.practicum.explorewithme.events.repository.LocationRepository;
import ru.practicum.explorewithme.exeption.ValidationException;
import ru.practicum.explorewithme.users.model.User;
import ru.practicum.explorewithme.users.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            CategoryRepository categoryRepository,
                            UserRepository userRepository,
                            LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public Event createEvent(Long userId, NewEventDto newEventDto) {
        Event event = EventMapper.toEvent(newEventDto);

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "В базе нет категории c id " + newEventDto.getCategory()));
        event.setCategory(category);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "В базе нет пользователя c id " + userId));
        event.setInitiator(user);

        Location location = newEventDto.getLocation();
        LocationModel locationModel = LocationMapper.toLocationModel(location);
        event.setLocation(locationRepository.save(locationModel));

        event.setState(EventStatus.PENDING);

        Event eventDB = eventRepository.save(event);
        log.info("Event with id " + eventDB.getId() + " has successfully created.");
        return eventDB;
    }

    @Override
    public Event updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        Long eventId = updateEventRequest.getEventId();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Не найдено событие с id " + eventId));

        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                            "В базе нет категории c id " + updateEventRequest.getCategory()));
            event.setCategory(category);
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        Event eventDB = eventRepository.save(event);
        log.info("Event with id " + eventDB.getId() + " has successfully updated.");
        return eventDB;
    }

    @Override
    public Event cancelEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Не найдено событие с id " + eventId));
        event.setState(EventStatus.CANCELED);
        Event eventDB = eventRepository.save(event);
        log.info("Event with id " + eventDB.getId() + " has successfully canceled.");
        return eventDB;
    }

    @Override
    public Event publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Не найдено событие с id " + eventId));
        event.setState(EventStatus.PUBLISHED);
        Event eventDB = eventRepository.save(event);
        log.info("Event with id " + eventDB.getId() + " has successfully published.");
        return eventDB;
    }

    @Override
    public Event rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Не найдено событие с id " + eventId));
        event.setState(EventStatus.CANCELED);
        Event eventDB = eventRepository.save(event);
        log.info("Event with id " + eventDB.getId() + " has successfully rejected.");
        return eventDB;
    }

    @Override
    public Collection<EventShortDto> getAllEventsByUser(Long userId, PageRequest pageRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "Не найден пользователь с id " + userId));
        Collection<Event> eventCollection = eventRepository.getAllEventsByUser(userId, pageRequest).stream()
                .collect(Collectors.toList());

        return EventMapper.toEventShortDtoCollection(eventCollection);
    }

    @Override
    public Event getEventById(Long userId, Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Не найдено событие с id " + eventId));
    }

    @Override
    public Event getEventByIdPublic(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найдено событие с id " + id));
    }

    @Override
    public void deleteEvent(Long eventId) {

    }
}
